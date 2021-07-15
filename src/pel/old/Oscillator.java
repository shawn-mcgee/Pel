package pel.old;

import pel.old.scene.Renderable;

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
        float x = PI * frequency / context.sample_rate;
        for(int i = 0; i < context.buffer.length; i ++)
            context.buffer[i] += (byte)(amplitude * Math.sin((i + j) * x));//(byte)(Math.sin((i + j) * x) >= 0 ? amplitude : - amplitude);//
        j += context.buffer.length;
    }
}
