package lrj.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Objects;

public class Debug {
    public static Logger
        info = Logger.create(System.out),
        warn = Logger.create(System.err);
    
    private Debug() {
        // do nothing
    }
    
    public static String info(Object event) {
        if(info != null)
            return info.log("Info", event);
        else
            return null;
    }
    
    public static String info(Object trace, Object event) {
        if(info != null)
            return info.log(trace, event);
        else
            return null;
    }
    
    public static String warn(Object event) {
        if(warn != null)
            return warn.log("Warn", event);
        else
            return null;
    }
    
    public static String warn(Object trace, Object event) {
        if(warn != null)
            return warn.log(trace, event);
        else
            return null;
    }
    
    public static String trace(Object trace) {
        return trace(trace, "");
    }
    
    public static String trace(Object trace, Object event, Object... args) {
        String format;
        if (trace instanceof String) {
            format = (String)trace;
            if(!format.isBlank())
                format = String.format("[%1$s]", trace);
        } else {
            try {
                String
                    _class = trace.getClass().getEnclosingMethod().getDeclaringClass().getName(),
                    _trace = trace.getClass().getEnclosingMethod()                    .getName();
                format = String.format("[%1$s.%2$s]", _class, _trace);
            } catch (Exception na) {
                format = String.format("[%1$s]", LocalDateTime.now());
            }
        }
    
        StringBuilder sb = new StringBuilder();
        String
            s0 = Objects.toString(event),
            s1 = String.format(s0, args);
        format += " %s%n";
        for(String u: s1.strip().split("\\n"))
            sb.append(String.format(format, u));
        return sb.toString();
    }
    
    public interface Logger {
        public String log(Object trace, Object event, Object... args);
        
        public static String log(PrintStream out, Object trace, Object event, Object... args) {
            String string = Debug.trace(trace, event, args);
            out.print(string);
            return string;
        }
        
        public static String log(PrintWriter out, Object trace, Object event, Object... args) {
            String string = Debug.trace(trace, event, args);
            out.print(string);
            return string;
        }
        
        public static Logger create(PrintStream out) {
            return (trace, event, args) -> log(out, trace, event, args);
        }
        
        public static Logger create(PrintWriter out) {
            return (trace, event, args) -> log(out, trace, event, args);
        }
    }
}