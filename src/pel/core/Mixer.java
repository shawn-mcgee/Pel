package pel.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class Mixer implements Renderable.Audio {
    protected final LinkedHashSet<Audio>
        audio = new LinkedHashSet<Audio>();
    public float
        level = 1f;
    
    public void attach(Audio a) {
        audio.add   (a);
    }
    
    public void detach(Audio a) {
        audio.remove(a);
    }
    
    @Override
    public void onRenderAudio(AudioContext context) {
        byte[]
            b0 =          context.audio_buffer        ,
            b1 = new byte[context.audio_buffer.length];
        context.audio_buffer = b1;
        
        for(Renderable.Audio a : audio) {
            Arrays.fill(b1, (byte)0);
            a.onRenderAudio(context);
            
            for(int i = 0; i < b0.length; i ++)
                b0[i] += Audio.clip(level * b1[i]);
        }
        
        context.audio_buffer = b0;
    }
}
