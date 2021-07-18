package pel.core;

import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;
import pel.Pel;
import pel.math.*;
import pel.util.*;
import pel.util.Event;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static pel.core.Colors.color3i;
import static pel.core.Colors.color4i;
import static pel.util.StringToObject.BOOLEAN;
import static pel.util.StringToObject.INTEGER;

public final class Engine implements Runnable {
    // event types
    public static final Class<CanvasEvent>
        CANVAS_EVENT = CanvasEvent.class;
    public static final Class<WindowEvent>
        WINDOW_EVENT = WindowEvent.class;
    public static final Class<SceneEvent>
        SCENE_EVENT  = SceneEvent.class;
    
    // singleton instance
    protected static final Engine
        INSTANCE = new Engine();
    
    // fixed audio format, currently only 44100hz 8bit mono is supported
    public static final int
        AUDIO_SAMPLE_RATE = 44100,
        AUDIO_SAMPLE_SIZE = 8,
        AUDIO_CHANNELS    = 1;
    public static final javax.sound.sampled.AudioFormat
        AUDIO_FORMAT = new javax.sound.sampled.AudioFormat(
            AUDIO_SAMPLE_RATE,
            AUDIO_SAMPLE_SIZE,
            AUDIO_CHANNELS   ,
            true,
            true
        );
    // configuration property map
    protected final TreeMap<String, String>
        cfg  =  new TreeMap<String, String>();
    // debug property map
    protected final LinkedHashMap<String, String>
        dbg  =  new LinkedHashMap<String, String>();
    
    // configurable
    protected boolean
        debug = false;
    protected Vector4
        debug_background = new Vector4(color3i(Color.BLACK), 127);
    protected String
        debug_font_name = "monospaced";
    protected int
        debug_font_size = 12;
    protected Vector4
        debug_foreground = new Vector4(color3i(Color.WHITE), 127);
    protected int
        fps = 60;
    protected Vector3
        canvas_background = color3i(Color.WHITE);
    protected Layout
        canvas_layout = new Layout();
    protected Vector3
        window_background = color3i(Color.BLACK);
    protected boolean
        window_border = true;
    protected int
        window_device = 0;
    protected Layout
        window_layout = new Layout();
    protected String
        window_string = Pel.VERSION.toString();
    
    protected Font
        debug_font;
    protected Color
        canvas_background_color,
        debug_foreground_color,
        debug_background_color,
        window_background_color;
    protected int
        logical_canvas_w,
        logical_canvas_h,
        virtual_canvas_w,
        virtual_canvas_h;
    protected float
        virtual_canvas_scale = 1f;
    
    // events
    protected final Event.Broker
        broker;
    protected final Event.Handle
        handle;
    
    // threading
    protected Thread
        thread;
    protected volatile boolean
        running;
    
    // awt components
    protected java.awt.Frame
        window;
    protected java.awt.Canvas
        canvas;
    
    // image & audio
    protected java.awt.image.BufferedImage
        image;
    protected javax.sound.sampled.SourceDataLine
        audio;
    protected int[]
        image_buffer;
    protected byte[]
        audio_buffer;
    
    // scene
    protected Scene
        scene;
    
    // metrics
    protected int
        avg_frame_hz;
    protected float
        avg_frame_ms,
        min_frame_ms,
        max_frame_ms,
    
        avg_update_ms,
        min_update_ms,
        max_update_ms,
    
        avg_image_ms,
        min_image_ms,
        max_image_ms,
    
        avg_audio_ms,
        min_audio_ms,
        max_audio_ms;
    
    // timing
    protected static final long
        one_second = 1000000000,
        one_millis =    1000000;
    
    protected long
        nanos_per_frame,
        frame_timer,
        frame_count,
    
        avg_frame_nanos,
        min_frame_nanos,
        max_frame_nanos,
    
        avg_update_nanos,
        min_update_nanos,
        max_update_nanos,
    
        avg_image_nanos,
        min_image_nanos,
        max_image_nanos,
    
        avg_audio_nanos,
        min_audio_nanos,
        max_audio_nanos,
    
        one_second_timer;
    
    protected float
        fixed_frame_dt,
        fixed_frame_ms;
    
    protected long
        t0,
        t1,
        t2;
    
    // recording
    protected final LinkedHashSet<GIF>
        gif  =  new LinkedHashSet<GIF>();
    protected final LinkedHashSet<WAV>
        wav  =  new LinkedHashSet<WAV>();
    
    private Engine() {
        // initialize the event system
        broker = new Event.Broker();
        handle = new Event.Handle();
        broker.onAttach(handle);
    
        // connect internal events
        handle.onAttach(CANVAS_EVENT, this::onCanvasEvent);
        handle.onAttach(WINDOW_EVENT, this::onWindowEvent);
        handle.onAttach(SCENE_EVENT , this::onSceneEvent );
        
        // configure debug properties to guarantee they are posted in correct order
        Configurable.configure(dbg,
            _DEBUG             , null,
            _DEBUG_UPDATE      , null,
            _DEBUG_RENDER_IMAGE, null,
            _DEBUG_RENDER_AUDIO, null,
            _DEBUG_CANVAS      , null
        );
    }
    
    public static void init() {
        if(!INSTANCE.running) {
            INSTANCE.thread = new Thread(INSTANCE, Pel.VERSION.toString());
            INSTANCE.running = true;
            INSTANCE.thread.start();
        }
    }
    
    public static void exit() {
        if( INSTANCE.running)
            INSTANCE.running = false;
    }
    
    public static void setCurrentScene(Scene scene) {
        queue(new SceneEvent(scene));
    }
    
    public static void configure(Object... args) {
        Configurable.configure(INSTANCE.cfg, args);
    }
    
    public static void loadConfiguration(Resource resource) {
        Configurable.loadConfiguration(INSTANCE.cfg, resource);
    }
    
    public static void loadConfiguration(String   resource) {
        Configurable.loadConfiguration(INSTANCE.cfg, resource);
    }
    
    public static void loadConfiguration(Class<?> from, String path) {
        Configurable.loadConfiguration(INSTANCE.cfg, from, path);
    }
    
    public static void loadConfiguration(String   from, String path) {
        Configurable.loadConfiguration(INSTANCE.cfg, from, path);
    }
    
    public static void saveConfiguration(Resource resource) {
        Configurable.saveConfiguration(INSTANCE.cfg, resource);
    }
    
    public static void saveConfiguration(String   resource) {
        Configurable.saveConfiguration(INSTANCE.cfg, resource);
    }
    
    public static String setProperty(Object key, Object val) {
        return Configurable.setProperty(INSTANCE.cfg, key, val);
    }
    
    public static <OBJECT> String setProperty(Object key, ObjectToString<OBJECT> o2s, OBJECT val            ) {
        return Configurable.setProperty(INSTANCE.cfg, key, o2s, val     );
    }
    
    public static <OBJECT> String setProperty(Object key, ObjectToString<OBJECT> o2s, OBJECT val, String alt) {
        return Configurable.setProperty(INSTANCE.cfg, key, o2s, val, alt);
    }
    
    public static String getProperty(Object key            ) {
        return Configurable.getProperty(INSTANCE.cfg, key     );
    }
    
    public static String getProperty(Object key, Object alt) {
        return Configurable.getProperty(INSTANCE.cfg, key, alt);
    }
    
    public static <OBJECT> OBJECT getPropertyAs(Object key, StringToObject<OBJECT> s2o            ) {
        return Configurable.getPropertyAs(INSTANCE.cfg, key, s2o     );
    }
    
    public static <OBJECT> OBJECT getPropertyAs(Object key, StringToObject<OBJECT> s2o, OBJECT alt) {
        return Configurable.getPropertyAs(INSTANCE.cfg, key, s2o, alt);
    }
    
    public static Vector2 getLogicalCanvasSize() {
        return new Vector2(
            INSTANCE.logical_canvas_w,
            INSTANCE.logical_canvas_h
        );
    }
    
    public static Vector2 getVirtualCanvasSize() {
        return new Vector2(
            INSTANCE.virtual_canvas_w,
            INSTANCE.virtual_canvas_h
        );
    }
    
    public static Vector2 logicalToVirtual(float x, float y) {
        return new Vector2(
            (x - INSTANCE.logical_canvas_w / 2f) / INSTANCE.virtual_canvas_scale + INSTANCE.virtual_canvas_w / 2f,
            (y - INSTANCE.logical_canvas_h / 2f) / INSTANCE.virtual_canvas_scale + INSTANCE.virtual_canvas_h / 2f
        );
    }
    
    public static Vector2 virtualToLogical(float x, float y) {
        return new Vector2(
            (x - INSTANCE.virtual_canvas_w / 2f) * INSTANCE.virtual_canvas_scale + INSTANCE.logical_canvas_w / 2f,
            (y - INSTANCE.virtual_canvas_h / 2f) * INSTANCE.virtual_canvas_scale + INSTANCE.logical_canvas_h / 2f
        );
    }
    
    public static Vector2 logicalToVirtual(Vector2 v) {
        return logicalToVirtual(v.x(), v.y());
    }
    
    public static Vector2 virtualToLogical(Vector2 v) {
        return virtualToLogical(v.x(), v.y());
    }
    
    public static String setDebugProperty(Object key, Object val) {
        return Configurable.setProperty(INSTANCE.dbg, key, val);
    }
    
    public static <OBJECT> String setDebugProperty(Object key, ObjectToString<OBJECT> o2s, OBJECT val            ) {
        return Configurable.setProperty(INSTANCE.dbg, key, o2s, val     );
    }
    
    public static <OBJECT> String setDebugProperty(Object key, ObjectToString<OBJECT> o2s, OBJECT val, String alt) {
        return Configurable.setProperty(INSTANCE.dbg, key, o2s, val, alt);
    }
    
    public static String getDebugProperty(Object key            ) {
        return Configurable.getProperty(INSTANCE.dbg, key     );
    }
    
    public static String getDebugProperty(Object key, Object alt) {
        return Configurable.getProperty(INSTANCE.dbg, key, alt);
    }
    
    public static <OBJECT> OBJECT getDebugPropertyAs(Object key, StringToObject<OBJECT> s2o            ) {
        return Configurable.getPropertyAs(INSTANCE.dbg, key, s2o     );
    }
    
    public static <OBJECT> OBJECT getDebugPropertyAs(Object key, StringToObject<OBJECT> s2o, OBJECT alt) {
        return Configurable.getPropertyAs(INSTANCE.dbg, key, s2o, alt);
    }
    
    public static <T> void attach(Class<T> type, Event.Listener<T> listener) {
        INSTANCE.handle.attach(type, listener);
    }
    
    public static <T> void detach(Class<T> type, Event.Listener<T> listener) {
        INSTANCE.handle.detach(type, listener);
    }
    
    public static void attach(Event.Broker broker) {
        INSTANCE.broker.attach(broker);
    }
    
    public static void detach(Event.Broker broker) {
        INSTANCE.broker.detach(broker);
    }
    
    public static void attach(Event.Handle handle) {
        INSTANCE.broker.attach(handle);
    }
    
    public static void detach(Event.Handle handle) {
        INSTANCE.broker.detach(handle);
    }
    
    public static <T> void flush(T event) {
        INSTANCE.broker.flush(event);
    }
    
    public static <T> void queue(T event) {
        INSTANCE.broker.queue(event);
    }
    
    public static void capturePNG() {
        new PNG().write();
    }
    
    public static GIF recordGIF() {
        return new GIF();
    }
    
    public static WAV recordWAV() {
        return new WAV();
    }
    
    @Override
    public void run() {
        try {
            onInit();
            while(running)
                onStep();
        } catch(Exception fatal) {
            Debug.warn(new Object() { }, "A fatal exception has occurred!");
            fatal.printStackTrace();
        } finally {
            onExit();
        }
    }
    
    public void onInit() throws Exception {
        debug = getPropertyAs(DEBUG, BOOLEAN, debug);
        debug_background = getPropertyAs(DEBUG_BACKGROUND, Vector4::fromString, debug_background);
        debug_font_name  = getProperty  (DEBUG_FONT_NAME ,                      debug_font_name );
        debug_font_size  = getPropertyAs(DEBUG_FONT_SIZE , INTEGER            , debug_font_size );
        debug_foreground = getPropertyAs(DEBUG_FOREGROUND, Vector4::fromString, debug_foreground);
        
        fps   = getPropertyAs(FPS  , INTEGER, fps  );
        canvas_background = getPropertyAs(CANVAS_BACKGROUND, Vector3::fromString, canvas_background);
        canvas_layout = getPropertyAs(CANVAS_LAYOUT, Layout::fromString, canvas_layout);
        window_background = getPropertyAs(WINDOW_BACKGROUND, Vector3::fromString, window_background);
        window_border = getPropertyAs(WINDOW_BORDER, BOOLEAN           , window_border);
        window_device = getPropertyAs(WINDOW_DEVICE, INTEGER           , window_device);
        window_layout = getPropertyAs(WINDOW_LAYOUT, Layout::fromString, window_layout);
        window_string = getProperty  (WINDOW_STRING,                     window_string);
        
        debug_font = new Font(debug_font_name, Font.PLAIN, debug_font_size);
        canvas_background_color = color3i(canvas_background);
        debug_background_color  = color4i(debug_background);
        debug_foreground_color  = color4i(debug_foreground);
        window_background_color = color3i(window_background);
    
        if(window != null)
            window.dispose();
        window = null;
        canvas = null;
        b      = null;
    
        // assign new resources
        window = new java.awt.Frame() ;
        canvas = new java.awt.Canvas();
        window.add(canvas);
    
        // compute the window bounds
        Region2 window_region = Layout.regionOf(window_layout, getDeviceRegion(window_device, !window_border));
        window.setUndecorated(!window_border);
        window.setBounds(
            (int)window_region.x(), (int)window_region.y(),
            (int)window_region.w(), (int)window_region.h()
        );
        window.setTitle(window_string);
    
        // compute the logical and virtual canvas size
        Insets window_insets = window.getInsets();
        Region2
            logical_canvas_region = new Region2(
            window_region.x() + window_insets.left,
            window_region.y() + window_insets.top ,
            window_region.w() - window_insets.left - window_insets.right ,
            window_region.h() - window_insets.top  - window_insets.bottom
        ),
            virtual_canvas_region = Layout.regionOf(canvas_layout, logical_canvas_region);
    
        logical_canvas_w = (int)logical_canvas_region.w();
        logical_canvas_h = (int)logical_canvas_region.h();
        virtual_canvas_w = (int)virtual_canvas_region.w();
        virtual_canvas_h = (int)virtual_canvas_region.h();
        virtual_canvas_scale = Math.min(
            (float) logical_canvas_w / virtual_canvas_w,
            (float) logical_canvas_h / virtual_canvas_h
        );
        
        image = new java.awt.image.BufferedImage(
            virtual_canvas_w,
            virtual_canvas_h,
            java.awt.image.BufferedImage.TYPE_INT_RGB
        );
        image_buffer = ((java.awt.image.DataBufferInt)image.getRaster().getDataBuffer()).getData();
        
        audio_buffer = new byte[fps > 0 ? AUDIO_SAMPLE_RATE / fps : 1];
        audio = AudioSystem.getSourceDataLine(AUDIO_FORMAT);
        audio.open(AUDIO_FORMAT, audio_buffer.length * 8  );
        audio.start();
    
        // add listeners
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent we) {
                queue(new WindowEvent(WindowEvent.ON_CLOSE));
            }
        });
        window.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent we) {
                queue(new WindowEvent(WindowEvent.ON_FOCUS));
            }
        
            @Override
            public void windowLostFocus(java.awt.event.WindowEvent we) {
                queue(new WindowEvent(WindowEvent.ON_UNFOCUS));
            }
        });
    
        canvas.setFocusable(true);
        canvas.setFocusTraversalKeysEnabled(false);
    
        canvas.addKeyListener        (Input.INSTANCE);
        canvas.addMouseListener      (Input.INSTANCE);
        canvas.addMouseWheelListener (Input.INSTANCE);
        canvas.addMouseMotionListener(Input.INSTANCE);
        canvas.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent ce) {
                queue(new CanvasEvent(
                    ce.getComponent().getWidth(),
                    ce.getComponent().getHeight()
                ));
            }
        });
    
        window.setIgnoreRepaint(true);
        canvas.setIgnoreRepaint(true);
        window.setVisible(true);
    
        // reset timing and metrics
        nanos_per_frame = fps > 0 ? one_second / fps : 0;
        frame_timer = 0;
        frame_count = 0;
    
        avg_frame_nanos = 0;
        min_frame_nanos = Long.MAX_VALUE;
        max_frame_nanos = 0;
    
        avg_update_nanos = 0;
        min_update_nanos = Long.MAX_VALUE;
        max_update_nanos = 0;
    
        avg_image_nanos = 0;
        min_image_nanos = Long.MAX_VALUE;
        max_image_nanos = 0;
    
        one_second_timer = 0;
    
        fixed_frame_dt = (float)nanos_per_frame / one_second;
        fixed_frame_ms = (float)nanos_per_frame / one_millis;
    
        t0 = t1 = t2 = System.nanoTime();
    }
    
    public void onStep() throws Exception {
        t2 = System.nanoTime();
    
        // compute delta time in nanos
        long delta_nanos = t2 - t1; t1 = t2;
        frame_timer      += delta_nanos;
        one_second_timer += delta_nanos;
    
        // if one frame has passed
        if(frame_timer >= nanos_per_frame) {
            // compute delta time in seconds
            float  t = (float)  (t2 - t0) / one_second;
            float dt = (float)frame_timer / one_second;
        
            long a, b, c, d, e, f;
        
            // UPDATE
            a = System.nanoTime();
            update(t, dt, fixed_frame_dt);
            b = System.nanoTime();
        
            long update_nanos = b - a;
            avg_update_nanos += update_nanos;
            if(update_nanos < min_update_nanos)
                min_update_nanos = update_nanos;
            if(update_nanos > max_update_nanos)
                max_update_nanos = update_nanos;
        
            // RENDER IMAGE
            c = System.nanoTime();
            renderImage(t, dt, fixed_frame_dt);
            d = System.nanoTime();
        
            long image_nanos = d - c;
            avg_image_nanos += image_nanos;
            if(image_nanos < min_image_nanos)
                min_image_nanos = image_nanos;
            if(image_nanos > max_image_nanos)
                max_image_nanos = image_nanos;
        
            // RENDER AUDIO
            e = System.nanoTime();
            renderAudio(t, dt, fixed_frame_dt);
            f = System.nanoTime();
        
            long audio_nanos = f - e;
            avg_audio_nanos += audio_nanos;
            if(audio_nanos < min_audio_nanos)
                min_audio_nanos = audio_nanos;
            if(audio_nanos > max_audio_nanos)
                max_audio_nanos = audio_nanos;
        
            long frame_nanos = f - a;
            avg_frame_nanos += frame_nanos;
            if(frame_nanos < min_frame_nanos)
                min_frame_nanos = frame_nanos;
            if(frame_nanos > max_frame_nanos)
                max_frame_nanos = frame_nanos;
        
            frame_timer = 0;
            frame_count  ++;
        }
    
        // if one second has passed
        if(one_second_timer >= one_second) {
            // capture metrics
            avg_frame_hz = (int)frame_count;
        
            avg_frame_ms = (float)avg_frame_nanos / frame_count / one_millis;
            min_frame_ms = (float)min_frame_nanos / one_millis;
            max_frame_ms = (float)max_frame_nanos / one_millis;
        
            avg_update_ms = (float)avg_update_nanos / frame_count / one_millis;
            min_update_ms = (float)min_update_nanos / one_millis;
            max_update_ms = (float)max_update_nanos / one_millis;
        
            avg_image_ms = (float) avg_image_nanos / frame_count / one_millis;
            min_image_ms = (float) min_image_nanos / one_millis;
            max_image_ms = (float) max_image_nanos / one_millis;
        
            avg_audio_ms = (float) avg_audio_nanos / frame_count / one_millis;
            min_audio_ms = (float) min_audio_nanos / one_millis;
            max_audio_ms = (float) max_audio_nanos / one_millis;
        
            // if debug is enabled
            if(debug) {
                // print metrics
                Debug.info(new Object() { }, String.format(
                    "DEBUG: %1$d hz @ %2$3.2f of %3$3.2f ms [ %4$3.2f < %5$5.2f ] \\ UPDATE %6$3.2f ms [ %7$3.2f < %8$5.2f] \\ IMAGE: %9$3.2f ms [ %10$3.2f < %11$5.2f] \\ AUDIO: %12$3.2f ms [ %13$3.2f < %14$5.2f]",
                    avg_frame_hz,
                
                    avg_frame_ms,
                    fixed_frame_ms,
                    min_frame_ms,
                    max_frame_ms,
                
                    avg_update_ms,
                    min_update_ms,
                    max_update_ms,
                
                    avg_image_ms,
                    min_image_ms,
                    max_image_ms,
                
                    avg_audio_ms,
                    min_audio_ms,
                    max_audio_ms
                ));
    
                setDebugProperty(_DEBUG, String.format(
                    "debug %1$d hz @ %2$3.2f of %3$3.2f ms [ %4$3.2f < %5$5.2f ]",
                    avg_frame_hz,
                    avg_frame_ms,
                    fixed_frame_ms,
                    min_frame_ms,
                    max_frame_ms
                ));
                setDebugProperty(_DEBUG_UPDATE, String.format(
                    " debug-update       %1$3.2f ms [ %2$3.2f < %3$5.2f ]",
                    avg_update_ms,
                    min_update_ms,
                    max_update_ms
                ));
                setDebugProperty(_DEBUG_RENDER_IMAGE, String.format(
                    " debug-render-image %1$3.2f ms [ %2$3.2f < %3$5.2f ]",
                    avg_image_ms,
                    min_image_ms,
                    max_image_ms
                ));
                setDebugProperty(_DEBUG_RENDER_AUDIO, String.format(
                    " debug-render-audio %1$3.2f ms [ %2$3.2f < %3$5.2f ]",
                    avg_audio_ms,
                    min_audio_ms,
                    max_audio_ms
                ));
            }
        
            // reset metrics
            frame_count = 0;
        
            avg_frame_nanos = 0;
            min_frame_nanos = Long.MAX_VALUE;
            max_frame_nanos = 0;
        
            avg_update_nanos = 0;
            min_update_nanos = Long.MAX_VALUE;
            max_update_nanos = 0;
        
            avg_image_nanos = 0;
            min_image_nanos = Long.MAX_VALUE;
            max_image_nanos = 0;
        
            avg_audio_nanos = 0;
            min_audio_nanos = Long.MAX_VALUE;
            max_audio_nanos = 0;
        
            one_second_timer = 0;
        }
    
        // put thread to sleep when not doing work to save resources
        long sync = (nanos_per_frame - frame_timer) / one_millis;
        if(sync > 1)
            Thread.sleep(1);
    }
    
    public void onExit() {
        if(window != null)
            window.dispose();
        for(GIF g: gif)
            g.write();
        for(WAV w: wav)
            w.write();
    }
    
    protected final Updateable.UpdateContext
        update_context = new Updateable.UpdateContext();
    public void update(float t, float dt, float fixed_dt) {
        Input .poll();
        broker.poll();
    }
    
    protected java.awt.image.BufferStrategy
        b;
    protected final Renderable.ImageContext
        image_context = new Renderable.ImageContext();
    public void renderImage(float t, float dt, float fixed_dt) {
        image_context.t  = t ;
        image_context.dt = dt;
        image_context.fixed_dt = fixed_dt;
        
        image_context.x = 0;
        image_context.y = 0;
        image_context.w = virtual_canvas_w;
        image_context.h = virtual_canvas_h;
        
        image_context.image_buffer = image_buffer;
        
        if(scene != null)
            scene.onRenderImage(image_context);
        
        for(GIF g: gif)
            g.onRenderImage(image_context);
        
        if(b == null || b.contentsLost()) {
            canvas.createBufferStrategy(2);
            b = canvas.getBufferStrategy();
        }
    
        Graphics2D g = (Graphics2D)b.getDrawGraphics();
        Graphics2D h = (Graphics2D)g.create();
    
        h.setColor(window_background_color);
        h.fillRect(0, 0, logical_canvas_w, logical_canvas_h);
    
        h.translate(
            (int)(logical_canvas_w - virtual_canvas_w * virtual_canvas_scale) / 2,
            (int)(logical_canvas_h - virtual_canvas_h * virtual_canvas_scale) / 2
        );
        h.scale(
            virtual_canvas_scale,
            virtual_canvas_scale
        );
        h.clipRect(
            0, 0,
            virtual_canvas_w,
            virtual_canvas_h
        );
    
        h.setColor(canvas_background_color);
        h.fillRect(0, 0, virtual_canvas_w, virtual_canvas_h);
        
        h.drawImage(image, 0, 0, null);
    
        if(debug && dbg.size() > 0) {
            g.setFont(debug_font);
            java.awt.FontMetrics fm = g.getFontMetrics();
        
            int
                print_w = 0,
                print_h = 0;
            for(String property : dbg.values())
                if(property != null) {
                    print_w  = Math.max(print_w, fm.stringWidth(property));
                    print_h += fm.getHeight();
                }
        
            g.setColor(debug_background_color);
            g.fillRect(0, 0, print_w, print_h);
            g.setColor(debug_foreground_color);
        
            int i = 0;
            for(String property : dbg.values())
                if(property != null)
                    g.drawString(property, 0, fm.getLeading() + fm.getAscent() + fm.getHeight() * i ++);
        }
    
        h.dispose();
        g.dispose();
        b.show();
    }
    
    protected final Renderable.AudioContext
        audio_context = new Renderable.AudioContext();
    public void renderAudio(float t, float dt, float fixed_dt) {
        audio_context.t  = t ;
        audio_context.dt = dt;
        audio_context.fixed_dt = fixed_dt;
        
        audio_context.audio_sample_rate = AUDIO_SAMPLE_RATE;
        audio_context.audio_sample_size = AUDIO_SAMPLE_SIZE;
        audio_context.audio_channels = AUDIO_CHANNELS;
        
        audio_context.audio_buffer = audio_buffer;
        
        if(scene != null)
            scene.onRenderAudio(audio_context);
        
        for(WAV w: wav)
            w.onRenderAudio(audio_context);
        
        audio.write(audio_buffer, 0, audio_buffer.length);
    }
    
    public static Region2 getDeviceRegion(int i, boolean borderless) {
        GraphicsDevice        gd = Utility.getGraphicsDevice(i);
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle bounds = gc.getBounds();
        
        if(borderless)
            return new Region2(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height
            );
        else {
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            return new Region2(
                bounds.x + insets.left,
                bounds.y + insets.top,
                bounds.width  - insets.left - insets.right,
                bounds.height - insets.top  - insets.bottom
            );
        }
    }
    
    public static Bounds2 getDeviceBounds(int i, boolean borderless) {
        GraphicsDevice        gd = Utility.getGraphicsDevice(i);
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle bounds = gc.getBounds();
        
        if(borderless)
            return new Bounds2(
                bounds.x,
                bounds.y,
                bounds.x + bounds.width,
                bounds.y + bounds.height
            );
        else {
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            return new Bounds2(
                bounds.x + insets.left,
                bounds.y + insets.top,
                bounds.x + bounds.width  - insets.left - insets.right,
                bounds.y + bounds.height - insets.top  - insets.bottom
            );
        }
    }
    
    public void onKeyUp(int key) {
        if(scene != null) scene.onKeyUp(key);
    }
    
    public void onKeyDown(int key) {
        if(scene != null) scene.onKeyDown(key);
    }
    
    public void onMouseUp(int mouse) {
        if(scene != null) scene.onMouseUp(mouse);
    }
    
    public void onMouseDown(int mouse) {
        if(scene != null) scene.onMouseDown(mouse);
    }
    
    public void onMouseMoved(Vector2 mouse) {
        if(scene != null) scene.onMouseMoved(mouse);
    }
    
    public void onMouseWheel(float   wheel) {
        if(scene != null) scene.onMouseWheel(wheel);
    }
    
    public void onCanvasEvent(CanvasEvent event) {
        Region2
            logical_canvas_region = new Region2(
                logical_canvas_w = event.canvas_w,
                logical_canvas_h = event.canvas_h
            ),
            virtual_canvas_region = Layout.regionOf(canvas_layout, logical_canvas_region);
        
        int
            _virtual_canvas_w = (int)virtual_canvas_region.w(),
            _virtual_canvas_h = (int)virtual_canvas_region.h();
        if(
            virtual_canvas_w != _virtual_canvas_w ||
            virtual_canvas_h != _virtual_canvas_h
        ) {
            image = new java.awt.image.BufferedImage(
                _virtual_canvas_w,
                _virtual_canvas_h,
                java.awt.image.BufferedImage.TYPE_INT_RGB
            );
            image_buffer = ((java.awt.image.DataBufferInt)image.getRaster().getDataBuffer()).getData();
        }
    
        virtual_canvas_w = _virtual_canvas_w;
        virtual_canvas_h = _virtual_canvas_h;
        virtual_canvas_scale = Math.min(
            (float) logical_canvas_w / virtual_canvas_w,
            (float) logical_canvas_h / virtual_canvas_h
        );
    
        setDebugProperty(_DEBUG_CANVAS, String.format(
            " debug-canvas [%1$s, %2$s] (%3$s, %4$s) %5$3.2f%%",
            logical_canvas_w,
            logical_canvas_h,
            virtual_canvas_w,
            virtual_canvas_h,
            virtual_canvas_scale * 100
        ));
    
        if(scene != null)
            scene.onResize(new Vector2(
                virtual_canvas_w,
                virtual_canvas_h
            ));
    }
    
    public void onWindowEvent(WindowEvent event) {
        if(event.isClose())
            Engine.exit();
    }
    
    public void onSceneEvent(SceneEvent event) {
        if(scene != null)
            scene.onDetach();
        scene = event.scene;
        if(scene != null)
            scene.onAttach();
    }
    
    public static class CanvasEvent {
        public final int
            canvas_w,
            canvas_h;
        
        public CanvasEvent(
            int canvas_w,
            int canvas_h
        ) {
            this.canvas_w = canvas_w;
            this.canvas_h = canvas_h;
        }
    }
    
    public static class WindowEvent {
        public static final int
            ON_CLOSE   = 0x01,
            ON_FOCUS   = 0x02,
            ON_UNFOCUS = 0x03;
        
        public final int
            value;
        
        public WindowEvent(
            int value
        ) {
            this.value = value;
        }
        
        public boolean isClose() {
            return value == ON_CLOSE;
        }
    }
    
    public static class SceneEvent {
        public final Scene
            scene;
        
        public SceneEvent(
            Scene scene
        ) {
            this.scene = scene;
        }
    }
    
    public static final String
        DEBUG            = "debug",
        DEBUG_BACKGROUND = "debug_background",
        DEBUG_FONT_NAME  = "debug_font_name",
        DEBUG_FONT_SIZE  = "debug_font_size",
        DEBUG_FOREGROUND = "debug_foreground",
        FPS               = "fps",
        CANVAS_BACKGROUND = "canvas-background",
        CANVAS_LAYOUT     = "canvas-layout",
        WINDOW_BACKGROUND = "window-background",
        WINDOW_BORDER     = "window-border",
        WINDOW_DEVICE     = "window-device",
        WINDOW_LAYOUT     = "window-layout",
        WINDOW_STRING     = "window-string";
    
    public static final String
        _DEBUG = ".debug",
        _DEBUG_UPDATE       = ".debug-update",
        _DEBUG_RENDER_IMAGE = ".debug-render-image",
        _DEBUG_RENDER_AUDIO = ".debug-render-audio",
        _DEBUG_CANVAS       = ".debug-canvas";
    
    public static class PNG implements AutoCloseable {
        protected static final String
            FORMAT = "png/PNG_%1$06d.png";
        protected static int
            INDEX = 0;
        
        protected final Resource
            resource;
        
        public PNG() {
            this((Resource)null);
        }
    
        public PNG(Class<?> from, String path) {
            this(new Resource(from, path));
        }
    
        public PNG(String   from, String path) {
            this(new Resource(from, path));
        }
    
        public PNG(String   resource) {
            this(new Resource(resource));
        }
    
        public PNG(Resource resource) {
            if(resource != null)
                this.resource = resource;
            else {
                String path = String.format(FORMAT, INDEX);
                while(Resource.exists(path))
                    path = String.format(FORMAT, ++ INDEX);
                this.resource = new Resource(path);
            }
        }
    
        @Override
        public void close() throws Exception {
            try (OutputStream out = resource.newOutputStream()) {
                ImageIO.write(INSTANCE.image, "PNG", out);
            }
        }
        
        public void write() {
            try {
                close();
            } catch(Exception na) {
                // do nothing
            }
        }
    }
    
    public static class GIF implements AutoCloseable, Renderable.Image {
        protected static final String
            FORMAT = "gif/GIF_%1$03d.gif";
        protected static int
            INDEX = 0;
        
        protected final Resource
            resource;
        
        protected OutputStream
            gif_output;
        protected GifEncoder
            gif_encoder;
        protected ImageOptions
            gif_options;
        
        public GIF() {
            this((Resource)null);
        }
        
        public GIF(Class<?> from, String path) {
            this(new Resource(from, path));
        }
        
        public GIF(String   from, String path) {
            this(new Resource(from, path));
        }
    
        public GIF(String   resource) {
            this(new Resource(resource));
        }
        
        public GIF(Resource resource) {
            if(resource != null)
                this.resource = resource;
            else {
                String path = String.format(FORMAT, INDEX);
                while(Resource.exists(path))
                    path = String.format(FORMAT, ++ INDEX);
                this.resource = new Resource(path);
            }
            start();
        }
        
        protected void start() {
            INSTANCE.gif.add(this);
            try {
                gif_output  = resource.newOutputStream();
                gif_encoder = new GifEncoder(
                    gif_output,
                    INSTANCE.virtual_canvas_w,
                    INSTANCE.virtual_canvas_h,
                    0
                );
                gif_options = new ImageOptions();
            } catch(Exception na) {
                na.printStackTrace();
            }
        }
    
        @Override
        public void close() throws Exception {
            INSTANCE.gif.remove(this);
            
            gif_encoder.finishEncoding();
            gif_output.flush();
            gif_output.close();
        }
        
        public void write() {
            try {
                close();
            } catch(Exception na) {
                // do nothing
            }
        }
    
        @Override
        public void onRenderImage(ImageContext context) {
            try {
                gif_options.setDelay((int)(1000 * context.fixed_dt), TimeUnit.MILLISECONDS);
                gif_encoder.addImage(context.image_buffer, context.w, gif_options);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    public static class WAV implements AutoCloseable, Renderable.Audio {
        protected static final String
            FORMAT = "wav/WAV_%1$03d.gif";
        protected static int
            INDEX = 0;
    
        protected final Resource
            resource;
        
        protected OutputStream
            wav_output;
        protected ByteArrayOutputStream
            wav_buffer;
    
        public WAV() {
            this((Resource)null);
        }
    
        public WAV(Class<?> from, String path) {
            this(new Resource(from, path));
        }
    
        public WAV(String   from, String path) {
            this(new Resource(from, path));
        }
    
        public WAV(String   resource) {
            this(new Resource(resource));
        }
    
        public WAV(Resource resource) {
            if(resource != null)
                this.resource = resource;
            else {
                String path = String.format(FORMAT, INDEX);
                while(Resource.exists(path))
                    path = String.format(FORMAT, ++ INDEX);
                this.resource = new Resource(path);
            }
        }
        
        protected void start() {
            INSTANCE.wav.add(this);
    
            wav_output = resource.newOutputStream();
            wav_buffer = new ByteArrayOutputStream();
        }
    
        @Override
        public void close() throws Exception {
            INSTANCE.wav.remove(this);
    
            byte[] b = wav_buffer.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            AudioInputStream ais = new AudioInputStream(bais, Engine.AUDIO_FORMAT, b.length);
    
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wav_output);
            wav_buffer.close();
            wav_output.close();
        }
    
        public void write() {
            try {
                close();
            } catch(Exception na) {
                // do nothing
            }
        }
    
        @Override
        public void onRenderAudio(Renderable.AudioContext context) {
            try {
                wav_buffer.write(context.audio_buffer);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
