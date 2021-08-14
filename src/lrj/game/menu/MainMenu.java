package lrj.game.menu;

import lrj.core.*;
import lrj.game.Game;
import lrj.math.Vector2;
import lrj.math.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static lrj.game.Game.*;
import static lrj.game.menu.Menu.*;
import static lrj.game.sprites.Sprites.*;

public class MainMenu extends Scene {
    protected Sprite
        logo_sprite,
        play_button,
        quit_button;
    protected float
        warp_parallax;

    public MainMenu() {
        logo_sprite = new Sprite(LRJ_LOGO);
        play_button = new Sprite(LRJ_PLAY_BUTTON);
        quit_button = new Sprite(LRJ_QUIT_BUTTON);

        play_button.x = (CANVAS_W - play_button.w) / 2;
        quit_button.x = (CANVAS_W - quit_button.w) / 2;

        play_button.y = (CANVAS_H / 2);
        quit_button.y = play_button.y + play_button.h + 2;

        parallax_layer0_image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);
        parallax_layer1_image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);
        parallax_layer2_image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);

        parallax_layer0_image_buffer = ((DataBufferInt) parallax_layer0_image.getRaster().getDataBuffer()).getData();
        parallax_layer1_image_buffer = ((DataBufferInt) parallax_layer1_image.getRaster().getDataBuffer()).getData();
        parallax_layer2_image_buffer = ((DataBufferInt) parallax_layer2_image.getRaster().getDataBuffer()).getData();

        parallax_layer0_stars = new Vector3.Mutable[CANVAS_W];
        parallax_layer1_stars = new Vector3.Mutable[CANVAS_W];
        parallax_layer2_stars = new Vector3.Mutable[CANVAS_W];

        Random random = new Random();
        for (int i = 0; i < CANVAS_W; i++) {
            parallax_layer0_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * random.nextFloat() - 1f), random.nextFloat() + 1f);
            parallax_layer1_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * random.nextFloat() - 1f), random.nextFloat() + 2f);
            parallax_layer2_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * random.nextFloat() - 1f), random.nextFloat() + 3f);
        }
    }

    @Override
    public void onRenderImage(ImageContext context) {
        Arrays.fill(context.image_buffer, 0x080808);

        Vector2 mouse = Input.getMouse();
        warp_parallax = (MAX_WARP_PARALLAX - MIN_WARP_PARALLAX) * (1f - mouse.y() / CANVAS_H) + MIN_WARP_PARALLAX;

        renderParallaxLayers(context);

        play_button.frame = 0;
        if (isButtonHover(play_button)) play_button.frame = ON_HOVER;
        if (isButtonPress(play_button)) play_button.frame = ON_PRESS;

        quit_button.frame = 0;
        if (isButtonHover(quit_button)) quit_button.frame = ON_HOVER;
        if (isButtonPress(quit_button)) quit_button.frame = ON_PRESS;

        logo_sprite.onRenderImage(context);
        play_button.onRenderImage(context);
        quit_button.onRenderImage(context);
    }

    @Override
    public void onKeyDown(int key) {
        if(key == VK_ESCAPE)
            onQuit();
    }

    @Override
    public void onMouseUp(int mouse) {
        if (mouse == 1) {
            if (isButtonHover(play_button)) onPlay();
            if (isButtonHover(quit_button)) onQuit();
        }
    }

    public void onPlay() {
        Engine.setCurrentScene(new Game());
    }

    public void onQuit() {
        Engine.exit();
    }

    protected BufferedImage
        parallax_layer0_image,
        parallax_layer1_image,
        parallax_layer2_image;
    protected int[]
        parallax_layer0_image_buffer,
        parallax_layer1_image_buffer,
        parallax_layer2_image_buffer;
    protected Vector3.Mutable[]
        parallax_layer0_stars,
        parallax_layer1_stars,
        parallax_layer2_stars;
    protected final java.awt.Color
        parallax_layer0_color = new Color(0x333333),
        parallax_layer1_color = new Color(0x666666),
        parallax_layer2_color = new Color(0x888888);
    protected final Vector3
        parallax_layer0_fade = new Vector3(24, 24, 24),
        parallax_layer1_fade = new Vector3(24, 24, 24),
        parallax_layer2_fade = new Vector3(24, 24, 24);

    public void renderParallaxLayers(ImageContext context) {
        renderParallaxLayer(
            context,
            parallax_layer0_image,
            parallax_layer0_image_buffer,
            parallax_layer0_stars,
            parallax_layer0_color,
            parallax_layer0_fade,
            warp_parallax
        );
        renderParallaxLayer(
            context,
            parallax_layer1_image,
            parallax_layer1_image_buffer,
            parallax_layer1_stars,
            parallax_layer1_color,
            parallax_layer1_fade,
            warp_parallax
        );
        renderParallaxLayer(
            context,
            parallax_layer2_image,
            parallax_layer2_image_buffer,
            parallax_layer2_stars,
            parallax_layer2_color,
            parallax_layer2_fade,
            warp_parallax
        );
    }

    public void renderParallaxLayer(
        ImageContext context,
        BufferedImage image,
        int[] image_buffer,
        Vector3.Mutable[] stars,
        Color color,
        Vector3 fade,
        float parallax
    ) {
        // fade
        for (int i = 0; i < image_buffer.length; i++) {
            int rgb = image_buffer[i];
            rgb = Image.r(rgb, Math.max(0, Image.r(rgb) - (int) fade.x()));
            rgb = Image.g(rgb, Math.max(0, Image.g(rgb) - (int) fade.y()));
            rgb = Image.b(rgb, Math.max(0, Image.b(rgb) - (int) fade.z()));
            image_buffer[i] = rgb;
        }

        // draw
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        for (int i = 0; i < stars.length; i++) {
            g.drawLine(
                (int) stars[i].x(),
                (int) stars[i].y(),
                (int) stars[i].x(),
                (int) stars[i].y(stars[i].y() + stars[i].z() * parallax * context.fixed_dt).y()
            );
            if (stars[i].y() > CANVAS_H)
                stars[i].y(stars[i].y() % CANVAS_H - CANVAS_H);
        }
        g.dispose();

        // mix
        for (int i = 0; i < image_buffer.length; i++)
            if ((image_buffer[i] & 0xffffff) != 0)
                context.image_buffer[i] = image_buffer[i];
    }
}
