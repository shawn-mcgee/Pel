package pel.old.scene;

import java.util.LinkedHashSet;

public class Mixer implements Renderable.Audio {
    protected final LinkedHashSet<Audio>
        audio = new LinkedHashSet<Audio>();
    
    protected float
        level;
    
    @Override
    public void onRenderAudio(AudioContext context) {
        byte[] b0 = context.buffer;
        for(Audio a: audio) {
            context.buffer = new byte[b0.length];
            a.onRenderAudio(context);
            
            for(int i = 0; i < b0.length; i ++)
                b0[i] += (byte)(context.buffer[i] * level);
        }
        context.buffer = b0;
    }
}
