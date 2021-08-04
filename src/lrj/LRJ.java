package lrj;

import lrj.core.Engine;
import lrj.core.Input;
import lrj.game.Game;
import lrj.util.Debug;
import lrj.util.Version;

import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_SPACE;

public class LRJ {
    public static final Version
        VERSION = new Version("LRJ 2021", 8, 4, 1);

    static Engine.GIF
        record_gif;

    public static void main(String[] args) {
        Debug.info(new Object() { }, lrj.LRJ.VERSION);

        // configure engine for lowrezjam
        Engine.configure(
                Engine.DEBUG        , true              ,
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
            if(event.isDown() && event.isKey(VK_PERIOD))
                Engine.capturePNG();
        });
        Engine.setCurrentScene(new Game());

        Engine.init();
    }
}
