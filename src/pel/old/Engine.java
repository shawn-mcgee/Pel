package pel.old;

import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;
import pel.old.scene.Renderable;
import pel.old.scene.Scene;
import pel.old.scene.Updateable;
import pel.util.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

// This class is a monolith and it does most of the heavy lifting

public class Engine implements Runnable {
    // STATIC VARIABLES
    // singleton
    protected static final Engine
        ENGINE = new Engine();
    // configuration
    public static Boolean
        CONFIGURE_DEBUG;
    public static Integer
        CONFIGURE_FRAME_HZ,
        CONFIGURE_WINDOW_W,
        CONFIGURE_WINDOW_H,
        CONFIGURE_CANVAS_W,
        CONFIGURE_CANVAS_H,
        CONFIGURE_AUDIO_SAMPLE_RATE,
        CONFIGURE_AUDIO_SAMPLE_SIZE,
        CONFIGURE_AUDIO_CHANNELS   ;
    
    // INSTANCE VARIABLES
    
    // configuration
    protected boolean
        debug = false;
    protected int
        frame_hz = 60,
        window_w = 640,
        window_h = 480,
        logical_canvas_w,
        logical_canvas_h,
        virtual_canvas_w = 64,
        virtual_canvas_h = 64,
        audio_sample_rate = 44100,
        audio_sample_size = 8,
        audio_channels    = 1,
        audio_frame_bytes = 1;
    protected float
        virtual_canvas_scale = 1f;
    protected AudioFormat
        audio_format;
    
    // event handling
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
    
    // buffer components
    protected BufferedImage
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
    
    // capture
    protected boolean
        record_gif,
        record_wav;
    protected OutputStream
        gif_output_stream,
        wav_output_stream;
    protected ByteArrayOutputStream
        wav_buffer_stream;
    protected GifEncoder
        gif_encoder;
    protected ImageOptions
        gif_options;
    
    protected Engine() {
        // initialize the event handler
        broker = new Event.Broker();
        handle = new Event.Handle();
        broker.onAttach(handle);
        
        // listen for window/canvas/scene events
        handle.onAttach(WindowEvent.class, this::onWindowEvent);
        handle.onAttach(CanvasEvent.class, this::onCanvasEvent);
        handle.onAttach(SceneEvent.class , this::onSceneEvent);
        
        // listen for recording events
        handle.onAttach(StartGIF.class, this::onStartGIF);
        handle.onAttach(WriteGIF.class, this::onWriteGIF);
        handle.onAttach(StartWAV.class, this::onStartWAV);
        handle.onAttach(WriteWAV.class, this::onWriteWAV);
        
        // listen for capture events
        handle.onAttach(WriteImageBufferToPNG.class, this::onWriteImageBufferToPNG);
        handle.onAttach(WriteAudioBufferToWAV.class, this::onWriteAudioBufferToWAV);
    }
    
    // init singleton thread
    public static synchronized void init() {
        if(!ENGINE.running) {
            ENGINE.thread = new Thread(ENGINE);
            ENGINE.running = true;
            ENGINE.thread.start();
        }
    }
    
    // kill singleton thread
    public static synchronized void exit() {
        if( ENGINE.running)
            ENGINE.running = false;
    }
    
    // schedule scene to be changed next frame
    public static void setScene(Scene scene) {
        queue(new SceneEvent(scene));
    }
    
    // attach an event listener to the global event system
    public static <T> void attach(Class<T> type, Event.Listener<T> listener) {
        ENGINE.handle.attach(type, listener);
    }
    
    // detach an event listener from the global event system
    public static <T> void detach(Class<T> type, Event.Listener<T> listener) {
        ENGINE.handle.detach(type, listener);
    }
    
    // attach an event broker to the global event system
    public static void attach(Event.Broker broker) {
        ENGINE.broker.attach(broker);
    }
    
    // detach an event broker from the global event system
    public static void detach(Event.Broker broker) {
        ENGINE.broker.detach(broker);
    }
    
    // attach an event handle to the global event system
    public static void attach(Event.Handle handle) {
        ENGINE.broker.attach(handle);
    }
    
    // detach an event handle from the global event system
    public static void detach(Event.Handle handle) {
        ENGINE.broker.detach(handle);
    }
    
    // flush an event in the global event system
    public static <T> void flush(T event) {
        ENGINE.broker.flush(event);
    }
    
    // queue an event in the global event system
    public static <T> void queue(T event) {
        ENGINE.broker.queue(event);
    }
    
    // begin recording a gif
    public static void startGIF(Resource resource) {
        queue(new StartGIF(resource));
    }
    
    public static void startGIF(String   resource) {
        startGIF(new Resource(resource));
    }
    
    public static void startGIF() {
        startGIF((Resource)null);
    }
    
    // begin recording a wav
    public static void startWAV(Resource resource) {
        queue(new StartWAV(resource));
    }
    
    public static void startWAV(String   resource) {
        startWAV(new Resource(resource));
    }
    
    public static void startWAV() {
        startWAV((Resource)null);
    }
    
    // finish gif recording and write
    public static void writeGIF() {
        queue(new WriteGIF());
    }
    // finish wav recording and write
    public static void writeWAV() {
        queue(new WriteWAV());
    }
    
    // capture image frame as png
    public static void writeImageBufferToPNG(Resource resource) {
        queue(new WriteImageBufferToPNG(resource));
    }
    
    public static void writeImageBufferToPNG(String   resource) {
        writeImageBufferToPNG(new Resource(resource));
    }
    
    public static void writeImageBufferToPNG() {
        writeImageBufferToPNG((Resource)null);
    }
    
    // capture audio frame as wav
    public static void writeAudioBufferToWAV(Resource resource) {
        queue(new WriteAudioBufferToWAV(resource));
    }
    
    public static void writeAudioBufferToWAV(String   resource) {
        writeAudioBufferToWAV(new Resource(resource));
    }
    
    public static void writeAudioBufferToWAV() {
        writeAudioBufferToWAV((Resource)null);
    }
    
    @Override
    public void run() {
        try {
            onInit();
            while(running)
                onStep();
        } catch(Exception fatal) {
            Debug.warn(new Object() { }, "A fatal exception has occurred");
            fatal.printStackTrace();
        } finally {
            onExit();
        }
    }
    
    // called when thread starts
    public void onInit() throws Exception {
        // apply configuration
        debug = Engine.interpret(CONFIGURE_DEBUG, debug);
        frame_hz = Engine.interpret(CONFIGURE_FRAME_HZ, frame_hz);
        window_w = Engine.interpret(CONFIGURE_WINDOW_W, window_w);
        window_h = Engine.interpret(CONFIGURE_WINDOW_H, window_h);
        virtual_canvas_w = Engine.interpret(CONFIGURE_CANVAS_W, virtual_canvas_w);
        virtual_canvas_h = Engine.interpret(CONFIGURE_CANVAS_H, virtual_canvas_h);
    
        // ignore audio configuration for now
        if(CONFIGURE_AUDIO_SAMPLE_RATE != null)
            Debug.warn(new Object() { }, "CONFIGURE_AUDIO_SAMPLE_RATE is not supported yet \\ DEFAULT: 44100HZ 8BIT MONO");
        if(CONFIGURE_AUDIO_SAMPLE_SIZE != null)
            Debug.warn(new Object() { }, "CONFIGURE_AUDIO_SAMPLE_SIZE is not supported yet \\ DEFAULT: 44100HZ 8BIT MONO");
        if(CONFIGURE_AUDIO_CHANNELS != null)
            Debug.warn(new Object() { }, "CONFIGURE_AUDIO_CHANNELS is not supported yet \\ DEFAULT: 44100HZ 8BIT MONO");
//        audio_sample_rate = Engine.interpret(CONFIGURE_AUDIO_SAMPLE_RATE, audio_sample_rate);
//        audio_sample_bits = Engine.interpret(CONFIGURE_AUDIO_SAMPLE_BITS, audio_sample_bits);
//        audio_channels    = Engine.interpret(CONFIGURE_AUDIO_CHANNELS, audio_channels);
        audio_frame_bytes = audio_sample_size * audio_channels / 8;
        
        audio_format = new AudioFormat(
            audio_sample_rate,
            audio_sample_size,
            audio_channels,
            true,
            true
        );
        
        // create awt components
        window = new java.awt.Frame();
        canvas = new java.awt.Canvas();
        window.add(canvas);
        
        // create buffer components
        image = new BufferedImage(
            virtual_canvas_w,
            virtual_canvas_h,
            BufferedImage.TYPE_INT_RGB
        );
        image_buffer = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        
        int n = audio_sample_rate * audio_sample_size * audio_channels / frame_hz / 8;
        audio_buffer = new byte[n];
        
        audio = AudioSystem.getSourceDataLine(audio_format);
        audio.open(audio_format, n * 16);
        audio.start();
        
        // forward awt events to global event system
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent we) {
                queue(WindowEvent.ON_CLOSE);
            }
        });
        canvas.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent ce) {
                queue(new CanvasEvent(
                    ce.getComponent().getWidth() ,
                    ce.getComponent().getHeight()
                ));
            }
        });
        
        // finalize awt components
        window.setSize(
            window_w,
            window_h
        );
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.revalidate();
    
        // reset timing and metrics
        nanos_per_frame = frame_hz > 0 ? one_second / frame_hz : 0;
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
    
        gif_options = new ImageOptions();
        gif_options.setDelay(
            (int)fixed_frame_ms,
            TimeUnit.MILLISECONDS
        );
    
        t0 = t1 = t2 = System.nanoTime();
    }
    
    // called each thread loop
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
    
    // called when thread stops
    public void onExit() {
        if(window != null)
            window.dispose();
    }
    
    // scene context variables
    protected final Updateable.UpdateContext
        update_context = new Updateable.UpdateContext();
    protected final Renderable.ImageContext
        image_context = new Renderable.ImageContext();
    protected final Renderable.AudioContext
        audio_context = new Renderable.AudioContext();
    
    public void update(float t, float dt, float fixed_dt) {
        broker.poll();
        
        update_context.t  = t ;
        update_context.dt = dt;
        update_context.fixed_dt = fixed_dt;
        
        if(scene != null)
            scene.onUpdate(update_context);
    }
    
    protected BufferStrategy
        b;
    public void renderImage(float t, float dt, float fixed_dt) throws Exception {
        image_context.t  = t ;
        image_context.dt = dt;
        image_context.fixed_dt = fixed_dt;
        
        image_context.buffer = image_buffer;
        image_context.w = virtual_canvas_w;
        image_context.h = virtual_canvas_h;
        
        if(scene != null)
            scene.onRenderImage(image_context);
        
        if(record_gif)
            gif_encoder.addImage(image_buffer, 64, gif_options);
    
        if(b == null || b.contentsLost()) {
            canvas.createBufferStrategy(2);
            b = canvas.getBufferStrategy();
        }
    
        java.awt.Graphics2D g = (java.awt.Graphics2D)b.getDrawGraphics();
    
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(
            0, 0,
            logical_canvas_w,
            logical_canvas_h
        );
        g.translate(
            (int)(logical_canvas_w - virtual_canvas_w * virtual_canvas_scale) / 2,
            (int)(logical_canvas_h - virtual_canvas_h * virtual_canvas_scale) / 2
        );
        g.scale(
            virtual_canvas_scale,
            virtual_canvas_scale
        );
    
        g.drawImage(image, 0, 0, null);
        g.dispose();
        b.show();
    }
    
    public void renderAudio(float t, float dt, float fixed_dt) throws Exception {
        audio_context.t  = t ;
        audio_context.dt = dt;
        audio_context.fixed_dt = fixed_dt;
        
        //Arrays.fill(audio_buffer, (byte)0);
        
        audio_context.buffer = audio_buffer;
        audio_context.sample_rate = audio_sample_rate;
        audio_context.sample_size = audio_sample_size;
        audio_context.channels    = audio_channels;
        
        if(scene != null)
            scene.onRenderAudio(audio_context);
        
        if(record_wav)
            wav_buffer_stream.write(audio_buffer);
        
        audio.write(audio_buffer, 0, audio_buffer.length);
    }
    
    public void onWindowEvent(WindowEvent event) {
        if (event == WindowEvent.ON_CLOSE)
            Engine.exit();
    }
    
    public void onCanvasEvent(CanvasEvent event) {
        logical_canvas_w = event.logical_canvas_w;
        logical_canvas_h = event.logical_canvas_h;
        virtual_canvas_scale = Math.min(
            (float) logical_canvas_w / virtual_canvas_w,
            (float) logical_canvas_h / virtual_canvas_h
        );
    }
    
    public void onSceneEvent(SceneEvent event) {
        if(scene != null)
            scene.onDetach();
        scene = event.scene;
        if(scene != null)
            scene.onAttach();
    }
    
    protected static final String
        png_format = "png/PNG_%1$06d.png",
        gif_format = "gif/GIF_%1$06d.gif",
        wav_format = "wav/WAV_%1$06d.wav";
    
    protected int
        png_index = 0,
        gif_index = 0,
        wav_index = 0;
    
    protected Resource PNG() {
        String path = String.format(png_format, png_index);
        while(Resource.exists(path))
            path = String.format(png_format, ++ png_index);
        return new Resource(path);
    }
    
    protected Resource GIF() {
        String path = String.format(gif_format, gif_index);
        while(Resource.exists(path))
            path = String.format(gif_format, ++ gif_index);
        return new Resource(path);
    }
    
    protected Resource WAV() {
        String path = String.format(wav_format, wav_index);
        while(Resource.exists(path))
            path = String.format(wav_format, ++ wav_index);
        return new Resource(path);
    }
    
    public void onStartGIF(StartGIF event) {
        if(!record_gif) {
            try {
                gif_output_stream = Resource.newOutputStream(event.resource != null ? event.resource : GIF());
                gif_encoder = new GifEncoder(
                    gif_output_stream,
                    virtual_canvas_w,
                    virtual_canvas_h,
                    0
                );
                record_gif = true;
            } catch(Exception na) {
                na.printStackTrace();
            }
        }
    }
    
    public void onStartWAV(StartWAV event) {
        if(!record_wav) {
            wav_output_stream = Resource.newOutputStream(event.resource != null ? event.resource : WAV());
            wav_buffer_stream = new ByteArrayOutputStream();
            record_wav = true;
        }
    }
    
    public void onWriteGIF(WriteGIF event) {
        if( record_gif)
            try {
                record_gif = false;
                gif_encoder.finishEncoding();
                gif_output_stream.close();
            } catch(Exception na) {
                na.printStackTrace();
            }
    }
    
    public void onWriteWAV(WriteWAV event) {
        if( record_wav)
            try {
                record_wav = false;
                byte[] b = wav_buffer_stream.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                AudioInputStream ais = new AudioInputStream(bais, audio_format, b.length);
    
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wav_output_stream);
                wav_output_stream.close();
            } catch(Exception na) {
                na.printStackTrace();
            }
    }
    
    public void onWriteImageBufferToPNG(WriteImageBufferToPNG event) {
        try (OutputStream out = Resource.newOutputStream(event.resource != null ? event.resource : PNG())) {
            ImageIO.write(image, "PNG", out);
        } catch(Exception na) {
            Debug.warn(new Object() { }, "");
        }
    }
    
    public void onWriteAudioBufferToWAV(WriteAudioBufferToWAV event) {
        try (OutputStream out = Resource.newOutputStream(event.resource != null ? event.resource : WAV())){
            ByteArrayInputStream bais = new ByteArrayInputStream(audio_buffer);
            AudioInputStream ais = new AudioInputStream(bais, audio_format, audio_buffer.length);
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, out);
        } catch(Exception na) {
            Debug.warn(new Object() { }, "");
        }
    }
    
    protected static boolean interpret(Boolean property, boolean alt) {
        boolean value;
        if(property != null)
            value = property;
        else
            value = alt;
        return value;
    }
    
    protected static int interpret(Integer property, int alt) {
        return interpret(property, alt, 0, Integer.MAX_VALUE);
    }
    
    protected static int interpret(Integer property, int alt, int min, int max) {
        int value;
        if(property != null) {
            value = property;
            if(value > max)
                value = max;
            if(value < min)
                value = min;
        } else
            value = alt;
        
        return value;
    }
    
    protected static float interpret(Float property, float alt) {
        return interpret(property, alt, 0, Float.MAX_VALUE);
    }
    
    protected static float interpret(Float property, float alt, float min, float max) {
        float value;
        if(property != null) {
            value = property;
            if(value > max)
                value = max;
            if(value < min)
                value = min;
        } else
            value = alt;
        
        return value;
    }
    
    public static enum WindowEvent {
        ON_CLOSE;
    }
    
    public static class CanvasEvent {
        public final int
            logical_canvas_w,
            logical_canvas_h;
        
        public CanvasEvent(
            int logical_canvas_w,
            int logical_canvas_h
        ) {
            this.logical_canvas_w = logical_canvas_w;
            this.logical_canvas_h = logical_canvas_h;
        }
    }
    
    public static class SceneEvent {
        public final Scene
            scene;
        
        public SceneEvent(Scene scene) {
            this.scene = scene;
        }
    }
    
    public static class WriteImageBufferToPNG {
        public final Resource
            resource;
        
        public WriteImageBufferToPNG(Resource resource) {
            this.resource = resource;
        }
    }
    
    public static class WriteAudioBufferToWAV {
        public final Resource
            resource;
    
        public WriteAudioBufferToWAV(Resource resource) {
            this.resource = resource;
        }
    }
    
    public static class StartGIF {
        public final Resource
            resource;
    
        public StartGIF(Resource resource) {
            this.resource = resource;
        }
    }
    
    public static class StartWAV {
        public final Resource
            resource;
    
        public StartWAV(Resource resource) {
            this.resource = resource;
        }
    }
    
    public static class WriteGIF {
    
    }
    
    public static class WriteWAV {
    
    }
}
