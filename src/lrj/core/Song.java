package lrj.core;

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
        float q = PI * frequency / context.audio_sample_rate;
        for(int i = 0; i < context.audio_buffer.length; i ++) {
            if(duration <= 0) {
                note = notes[z = (z + 1) % notes.length];
                frequency = note.frequency;
                amplitude = note.amplitude;
                duration = (int)(note.duration * context.audio_sample_rate);
    
                q = PI * frequency / context.audio_sample_rate;
            }
            context.audio_buffer[i] += (byte)(amplitude * Math.sin((i + j) * q));
            duration --;
        }
        j += context.audio_buffer.length;
    }
}
