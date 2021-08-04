package lrj.core;

public interface Updateable {
    public void onUpdate(UpdateContext context);
    
    public static class UpdateContext {
        public float
            t,
            dt,
            fixed_dt;
    }
}
