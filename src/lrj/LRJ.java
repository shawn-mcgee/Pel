package lrj;

import lrj.core.Engine;
import lrj.core.Input;
import lrj.game.menu.GameOverMenu;
import lrj.game.menu.MainMenu;
import lrj.util.Debug;
import lrj.util.Version;

import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_SPACE;

public class LRJ {
    public static final Version
        VERSION = new Version("Star Blep", 1, 0, 0);

    static Engine.GIF
        record_gif;

    public static void main(String[] args) {
        Debug.info(new Object() { }, lrj.LRJ.VERSION);

        // configure engine for lowrezjam
        Engine.configure(
                Engine.DEBUG        , false             ,
                Engine.CANVAS_LAYOUT, "w0: 64u, h0: 64u",
                Engine.WINDOW_LAYOUT, "x0:.5, y0: .5, x1:.5, y1:.5, w0:512u, h0:512u",
                Engine.WINDOW_DEVICE, 1
        );

//        Engine.attach(Input.KEY_EVENT, (event) -> {
//            if(event.isDown() && event.isKey(VK_SPACE))
//                if(record_gif != null) {
//                    record_gif.write();
//                    record_gif = null;
//                } else
//                    record_gif = Engine.recordGIF();
//            if(event.isDown() && event.isKey(VK_PERIOD))
//                Engine.capturePNG();
//        });
//        Engine.setCurrentScene(new MainMenu());

        Engine.init();
    }
}
