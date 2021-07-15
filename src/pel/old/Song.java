package pel.old;

import pel.old.scene.Renderable;

public class Song implements Renderable.Audio {
    public static float
        PI = (float)Math.PI;
    protected float
        frequency,
        amplitude;
    protected int
        duration;
    protected final Note[]
        notes;
    protected Note
        note;
    protected int
        j, z = -1;
    
    public Song(Note... notes) {
        this.notes = notes;
    }
    
    @Override
    public void onRenderAudio(Renderable.AudioContext context) {
        float x = PI * frequency / context.sample_rate;
        for(int i = 0; i < context.buffer.length; i ++) {
            if(duration <= 0) {
                note = notes[z = (z + 1) % notes.length];
                frequency = note.frequency;
                amplitude = note.amplitude;
                duration = (int)(note.duration * context.sample_rate);
    
                x = PI * frequency / context.sample_rate;
            }
            context.buffer[i] += (byte)(amplitude * Math.sin((i + j) * x));
            duration --;
        }
        j += context.buffer.length;
    }
}
