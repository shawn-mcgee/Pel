package pel.old.scene;

public class Scene implements Updateable, Renderable.Image, Renderable.Audio {
    public void onAttach() { }
    public void onDetach() { }
    
    @Override
    public void onUpdate(UpdateContext context) { }
    @Override
    public void onRenderImage(Renderable.ImageContext context) { }
    @Override
    public void onRenderAudio(Renderable.AudioContext context) { }
}
