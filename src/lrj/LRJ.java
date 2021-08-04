package lrj;

import pel.Pel;
import pel.core.*;
import pel.math.Vector2;
import pel.math.Vector3;
import pel.old.Note;
import pel.old.Song;
import pel.util.Debug;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class LRJ {
    static Engine.GIF
        record_gif;

    public static void main(String[] args) {
        Debug.info(new Object() { }, Pel.VERSION);

        // configure engine for lowrezjam
        Engine.configure(
            Engine.DEBUG        , false             ,
            Engine.CANVAS_LAYOUT, "w0: 64u, h0: 64u",
            Engine.WINDOW_LAYOUT, "w0:640u, h0:480u",
            Engine.WINDOW_DEVICE, 1
        );

        Engine.attach(Input.KEY_EVENT, (event) -> {
            if(event.isDown() && event.isKey(VK_SPACE))
                if(record_gif != null) {
                    record_gif.write();
                    record_gif = null;
                } else
                    record_gif = Engine.recordGIF();
        });
        Engine.setCurrentScene(new Game());

        Engine.init();

    }
}
