package pel.old;

public class Note {
        public static final float
            C5       = 523.25f,
            C5_SHARP = 554.37f,
            D5       = 587.33f,
            E5_FLAT  = 622.25f,
            E5       = 659.25f,
            F5       = 698.46f,
            F5_SHARP = 739.99f,
            G5       = 783.99f,
            A5_FLAT  = 830.61f,
            A5       = 880.00f,
            B5_FLAT  = 932.33f,
            B5       = 987.77f;
        public float
            frequency,
            amplitude,
            duration;
        
        public Note(float frequency, float amplitude, float duration) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.duration  = duration;
        }
    }