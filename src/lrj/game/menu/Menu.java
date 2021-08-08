package lrj.game.menu;

import lrj.core.*;
import lrj.math.Vector2;
import lrj.math.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;

import static lrj.game.Game.*;
import static lrj.game.Game.CANVAS_H;
import static lrj.game.sprites.Sprites.LRJ_PLAY_BUTTON;
import static lrj.game.sprites.Sprites.LRJ_QUIT_BUTTON;

public class Menu extends Scene {
    public static final int
        ON_HOVER = 1,
        ON_PRESS = 2;

    public static boolean isButtonHover(Sprite button) {
        Vector2 mouse = Input.getMouse();
        return
            mouse.x() >= button.x && mouse.x() <= button.x + button.w &&
            mouse.y() >= button.y && mouse.y() <= button.y + button.h;
    }

    public static boolean isButtonPress(Sprite button) {
        return isButtonHover(button) && Input.isMouseDown(1);
    }

}
