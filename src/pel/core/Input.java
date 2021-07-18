package pel.core;

import pel.math.Vector2;
import pel.util.Event;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

public final class Input implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
    public static Class<KeyEvent>
        KEY_EVENT = KeyEvent.class;
    public static Class<MouseEvent>
        MOUSE_EVENT = MouseEvent.class;
    public static Class<MouseMovedEvent>
        MOUSE_MOVED_EVENT = MouseMovedEvent.class;
    public static Class<MouseWheelEvent>
        MOUSE_WHEEL_EVENT = MouseWheelEvent.class;
    
    protected static final Input
        INSTANCE = new Input();
    
    protected final HashMap<Integer, Boolean>
        k0 = new HashMap<>(),
        k1 = new HashMap<>(),
        m0 = new HashMap<>(),
        m1 = new HashMap<>();
    protected final Vector2.Mutable
        mouse = new Vector2.Mutable();
    protected float
        wheel;
    
    protected final Event.Broker
        broker;
    protected final Event.Handle
        handle;
    
    
    private Input() {
        broker = new Event.Broker();
        handle = new Event.Handle();
        broker.onAttach(handle);
        
        handle.onAttach(KEY_EVENT        , this::onKeyEvent       );
        handle.onAttach(MOUSE_EVENT      , this::onMouseEvent     );
        handle.onAttach(MOUSE_MOVED_EVENT, this::onMouseMovedEvent);
        handle.onAttach(MOUSE_WHEEL_EVENT, this::onMouseWheelEvent);
    }
    
    public static boolean isKeyUp(int key) {
        Boolean b;
        return (b = INSTANCE.k1.get(key)) == null || !b;
    }
    
    public static boolean isKeyDown(int key) {
        Boolean b;
        return (b = INSTANCE.k1.get(key)) != null &&  b;
    }
    
    public static boolean isMouseUp(int mouse) {
        Boolean b;
        return (b = INSTANCE.m1.get(mouse)) == null || !b;
    }
    
    public static boolean isMouseDown(int mouse) {
        Boolean b;
        return (b = INSTANCE.m1.get(mouse)) != null &&  b;
    }
    
    public static Vector2 getMouse() {
        return new Vector2(INSTANCE.mouse);
    }
    
    public static float   getWheel() {
        return INSTANCE.wheel;
    }
    
    public static boolean isWheelUp() {
        return getWheel() < 0;
    }
    
    public static boolean isWheelDown() {
        return getWheel() > 0;
    }
    
    protected static void poll() {
        INSTANCE.broker.poll();
    }
    
    public void onKeyEvent(KeyEvent event) {
        if(event.isDown()) {
            k1.put(event.key, true );
            Engine.INSTANCE.onKeyDown(event.key);
        } else {
            k1.put(event.key, false);
            Engine.INSTANCE.onKeyUp  (event.key);
        }
        Engine.flush(event);
    }
    
    public void onMouseEvent(MouseEvent event) {
        if(event.isDown()) {
            m1.put(event.mouse, true );
            Engine.INSTANCE.onMouseDown(event.mouse);
        } else {
            m1.put(event.mouse, false);
            Engine.INSTANCE.onMouseUp  (event.mouse);
        }
        Engine.flush(event);
    }
    
    public void onMouseMovedEvent(MouseMovedEvent event) {
        mouse.set(event.value);
        Engine.INSTANCE.onMouseMoved(event.value);
        Engine.flush(event);
    }
    
    public void onMouseWheelEvent(MouseWheelEvent event) {
        wheel = event.value;
        Engine.INSTANCE.onMouseWheel(event.value);
        Engine.flush(event);
    }
    
    @Override
    public void keyTyped(java.awt.event.KeyEvent ke) {
    
    }
    
    @Override
    public void keyPressed(java.awt.event.KeyEvent ke) {
        Boolean b;
        if((b = INSTANCE.k0.get(ke.getKeyCode())) == null || !b) {
            k0.put(ke.getKeyCode(), true );
            broker.queue(new KeyEvent(ke.getKeyCode(), true ));
        }
    }
    
    @Override
    public void keyReleased(java.awt.event.KeyEvent ke) {
        Boolean b;
        if((b = INSTANCE.k0.get(ke.getKeyCode())) != null &&  b) {
            k0.put(ke.getKeyCode(), false);
            broker.queue(new KeyEvent(ke.getKeyCode(), false));
        }
    }
    
    @Override
    public void mouseClicked(java.awt.event.MouseEvent me) {
    
    }
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent me) {
        Boolean b;
        if((b = INSTANCE.k0.get(me.getButton())) == null || !b) {
            k0.put(me.getButton(), true );
            broker.queue(new MouseEvent(me.getButton(), true ));
        }
    }
    
    @Override
    public void mouseReleased(java.awt.event.MouseEvent me) {
        Boolean b;
        if((b = INSTANCE.k0.get(me.getButton())) != null &&  b) {
            k0.put(me.getButton(), false);
            broker.queue(new MouseEvent(me.getButton(), false));
        }
    }
    
    @Override
    public void mouseEntered(java.awt.event.MouseEvent me) {
    
    }
    
    @Override
    public void mouseExited(java.awt.event.MouseEvent me) {
    
    }
    
    @Override
    public void mouseDragged(java.awt.event.MouseEvent me) {
        broker.queue(new MouseMovedEvent(Engine.logicalToVirtual(
            me.getX(),
            me.getY()
        )));
    }
    
    @Override
    public void mouseMoved(java.awt.event.MouseEvent me) {
        broker.queue(new MouseMovedEvent(Engine.logicalToVirtual(
            me.getX(),
            me.getY()
        )));
    }
    
    @Override
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent mwe) {
        broker.queue(new MouseWheelEvent(mwe.getWheelRotation()));
    }
    
    public static class KeyEvent {
        public final int
            key;
        public final boolean
            value;
        
        public KeyEvent(int key, boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean isKey(int id) {
            return this.key == id;
        }
        
        public boolean isDown() {
            return  value;
        }
        
        public boolean isUp() {
            return !value;
        }
    }
    
    public static class MouseEvent {
        public final int
            mouse;
        public final boolean
            value;
        
        public MouseEvent(int mouse, boolean value) {
            this.mouse = mouse;
            this.value = value;
        }
        
        public boolean isMouse(int id) {
            return this.mouse == id;
        }
        
        public boolean isDown() {
            return  value;
        }
        
        public boolean isUp() {
            return !value;
        }
    }
    
    public static class MouseMovedEvent {
        public final Vector2
            value;
        
        public MouseMovedEvent(Vector2 value) {
            this.value = value;
        }
    }
    
    public static class MouseWheelEvent {
        public final float
            value;
        
        public MouseWheelEvent(float value) {
            this.value = value;
        }
        
        public boolean isDown() {
            return value > 0f;
        }
        
        public boolean isUp() {
            return value < 0f;
        }
    }
}
