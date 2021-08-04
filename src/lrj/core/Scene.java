package lrj.core;

import lrj.math.Vector2;

public class Scene implements Updateable, Renderable.Image, Renderable.Audio {
    public void onAttach() { }
    public void onDetach() { }
    public void onResize(Vector2 canvas) { }
    
    public void onKeyUp(int key) { }
    public void onKeyDown(int key) { }
    public void onMouseUp(int mouse) { }
    public void onMouseDown(int mouse) { }
    public void onMouseMoved(Vector2 mouse) { }
    public void onMouseWheel(float   wheel) { }
    
    @Override
    public void onUpdate(UpdateContext context) { }
    @Override
    public void onRenderImage(ImageContext context) { }
    @Override
    public void onRenderAudio(AudioContext context) { }
}
