package pel.old;

import pel.core.Renderable;

public class Oscillator implements Renderable.Audio {
    public static float
        PI = (float)Math.PI;
    public float
        frequency = 440f,
        amplitude =   2f;
    
    protected int
        j = 0;
    @Override
    public void onRenderAudio(Renderable.AudioContext context) {
        float x = PI * frequency / context.audio_sample_rate;
        for(int i = 0; i < context.audio_buffer.length; i ++)
            context.audio_buffer[i] += (byte)(amplitude * Math.sin((i + j) * x));//(byte)(Math.sin((i + j) * x) >= 0 ? amplitude : - amplitude);//
        j += context.audio_buffer.length;
    }
}
