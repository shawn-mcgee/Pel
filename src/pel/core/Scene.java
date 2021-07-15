package pel.core;

import pel.math.Vector2;

public class Scene implements Updateable, Renderable.Image, Renderable.Audio {
    public void onAttach() { }
    public void onDetach() { }
    public void onResize(Vector2 canvas) { }
    
    @Override
    public void onUpdate(UpdateContext context) { }
    @Override
    public void onRenderImage(ImageContext context) { }
    @Override
    public void onRenderAudio(AudioContext context) { }
}
