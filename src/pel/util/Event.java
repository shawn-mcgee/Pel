package pel.util;

import java.util.*;

public interface Event {
    
    public static interface Listener<T> {
        public void handle(T event);
        
        public static class Group<T> {
            protected final Set<Listener<T>>
                listeners = new LinkedHashSet<>();
            protected final List<Listener<T>>
                attach = new ArrayList<>(),
                detach = new ArrayList<>();
            
            public void attach(Listener<T> listener) {
                synchronized (attach) {
                    attach.add(listener);
                }
            }
            
            public void detach(Listener<T> listener) {
                synchronized (detach) {
                    detach.add(listener);
                }
            }
            
            public void onAttach(Listener<T> listener) {
                synchronized (listeners) {
                    listeners.add   (listener);
                }
            }
            
            public void onDetach(Listener<T> listener) {
                synchronized (listeners) {
                    listeners.remove(listener);
                }
            }
            
            public void attach() {
                synchronized (attach) {
                    for (Listener<T> listener : attach)
                        onAttach(listener);
                    attach.clear();
                }
            }
            
            public void detach() {
                synchronized (detach) {
                    for (Listener<T> listener : detach)
                        onDetach(listener);
                    detach.clear();
                }
            }
            
            public void flush(T event) {
                synchronized (listeners) {
                    for (Listener<T> listener : listeners)
                        listener.handle(event);
                }
            }
        }
    }
    
    public static class Handle {
        protected final Map<Class<?>, Listener.Group<?>>
            listeners = new LinkedHashMap<>();
        
        public Handle() {
            // do nothing
        }
        
        public <T> Listener.Group<T> request(Class<T> type) {
            synchronized (listeners) {
                Listener.Group<T> t = Unsafe.cast(listeners.get(type));
//                if (t == null)
//                    listeners.put(type, t = new Listener.Group<>());
                return t;
            }
        }
        
        public <T> Listener.Group<T> require(Class<T> type) {
            synchronized (listeners) {
                Listener.Group<T> t = Unsafe.cast(listeners.get(type));
                if (t == null)
                    listeners.put(type, t = new Listener.Group<>());
                return t;
            }
        }
        
        public <T> void attach(Class<T> type, Listener<T> listener) {
            require(type).attach(listener);
        }
        
        public <T> void detach(Class<T> type, Listener<T> listener) {
            Listener.Group<T> _listeners = request(type);
            if (_listeners != null)
                _listeners.detach(listener);
        }
        
        public <T> void onAttach(Class<T> type, Listener<T> listener) {
            require(type).onAttach(listener);
        }
        
        public <T> void onDetach(Class<T> type, Listener<T> listener) {
            Listener.Group<T> _listeners = request(type);
            if (_listeners != null)
                _listeners.onDetach(listener);
        }
        
        public void attach() {
            synchronized (listeners) {
                listeners.forEach((type, group) -> group.attach());
            }
        }
        
        public void detach() {
            synchronized (listeners) {
                listeners.forEach((type, group) -> group.detach());
            }
        }
        
        public <T> void flush(T event) {
            synchronized (listeners) {
                listeners.forEach((_type, _listeners) -> {
                    if (_type.isInstance(event))
                        _listeners.flush(
                            Unsafe.cast(event)
                        );
                });
            }
        }
        
        public static class Group {
            protected final Set<Handle>
                handles = new LinkedHashSet<>();
            protected final List<Handle>
                attach = new ArrayList<>(),
                detach = new ArrayList<>();
            
            public void attach(Handle handle) {
                synchronized (attach) {
                    attach.add(handle);
                }
            }
            
            public void detach(Handle handle) {
                synchronized (detach) {
                    detach.add(handle);
                }
            }
            
            public void onAttach(Handle handle) {
                synchronized (handles) {
                    handles.add   (handle);
                }
            }
            
            public void onDetach(Handle handle) {
                synchronized (handles) {
                    handles.remove(handle);
                }
            }
            
            public void attach() {
                synchronized (attach) {
                    for (Handle handle : attach)
                        onAttach(handle);
                    attach.clear();
                }
                synchronized (handles) {
                    for (Handle handle : handles)
                        handle.attach();
                }
            }
            
            public void detach() {
                synchronized (handles) {
                    for (Handle handle : handles)
                        handle.detach();
                }
                synchronized (detach) {
                    for (Handle handle : detach)
                        onDetach(handle);
                    detach.clear();
                }
            }
            
            public <T> void flush(T event) {
                synchronized (handles) {
                    for (Handle handle: handles)
                        handle.flush(event);
                }
            }
        }
    }
    
    public static class Broker {
        protected final Handle.Group
            handles = new Handle.Group();
        protected final Group
            brokers = new Group();
        protected List<Object>
            events0 = new ArrayList<>(),
            events1 = new ArrayList<>();
        protected final Object
            lock = new Object();
        
        public void attach(Handle handle) {
            handles.attach(handle);
        }
        
        public void detach(Handle handle) {
            handles.detach(handle);
        }
        
        public void onAttach(Handle handle) {
            handles.onAttach(handle);
        }
        
        public void onDetach(Handle handle) {
            handles.onDetach(handle);
        }
        
        public void attach(Broker broker) {
            brokers.attach(broker);
        }
        
        public void detach(Broker broker) {
            brokers.detach(broker);
        }
        
        public void onAttach(Broker broker) {
            brokers.onAttach(broker);
        }
        
        public void onDetach(Broker broker) {
            brokers.onDetach(broker);
        }
        
        public void attach() {
            handles.attach();
            brokers.attach();
        }
        
        public void detach() {
            handles.detach();
            brokers.detach();
        }
        
        public <T> void queue(T event) {
            synchronized (lock) {
                events0.add(event);
            }
        }
        
        public <T> void flush(T event) {
            handles.flush(event);
            brokers.flush(event);
        }
        
        public void flush() {
            synchronized (lock) {
                List<Object> events2 = events0;
                events0 = events1;
                events1 = events2;
            }
            
            if (events1.size() > 0) {
                for (Object event : events1)
                    flush(event);
                events1.clear();
            }
        }
        
        public void poll() {
            attach();
            detach();
            flush();
        }
        
        public boolean isPending() {
            synchronized (lock) {
                return events0.size() > 0;
            }
        }
        
        public static class Group {
            protected final Set<Broker>
                brokers = new LinkedHashSet<>();
            protected final List<Broker>
                attach = new ArrayList<>(),
                detach = new ArrayList<>();
            
            public void attach(Broker broker) {
                synchronized (attach) {
                    attach.add(broker);
                }
            }
            
            public void detach(Broker broker) {
                synchronized (detach) {
                    detach.add(broker);
                }
            }
            
            public void onAttach(Broker broker) {
                synchronized (brokers) {
                    brokers.add   (broker);
                }
            }
            
            public void onDetach(Broker broker) {
                synchronized (brokers) {
                    brokers.remove(broker);
                }
            }
            
            public void attach() {
                synchronized (attach) {
                    for (Broker broker : attach)
                        onAttach(broker);
                    attach.clear();
                }
                synchronized (brokers) {
                    for (Broker broker : brokers)
                        broker.attach();
                }
            }
            
            public void detach() {
                synchronized (brokers) {
                    for (Broker broker : brokers)
                        broker.detach();
                }
                synchronized (detach) {
                    for (Broker broker : detach)
                        onDetach(broker);
                    detach.clear();
                }
            }
            
            public <T> void flush(T event) {
                synchronized (brokers) {
                    for (Broker broker : brokers)
                        broker.flush(event);
                }
            }
        }
    }
}
