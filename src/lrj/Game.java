package lrj;

import pel.core.*;
import pel.math.Vector2;
import pel.math.Vector3;
import pel.util.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class Game extends Scene {
    protected static final Sprite.Atlas
        ship_atlas = Sprite.Atlas.load("lrj.LRJ:lrj_ship.png", "lrj_ship", 16, 16);
    protected static final float
        min_warp   =  8f,
        max_warp   = 64f,
        delta_warp = .85f,
        min_x =  8f,
        max_x = 56f,
        min_y = 10f,
        max_y = 54f,
        delta_strafe = 1f;

    protected float
        x, y,
        warp = (max_warp + min_warp) / 4f;

    protected Layer
        parallax_layer0,
        parallax_layer1,
        parallax_layer2,
        warp_trail_layer;
    protected Vector3.Mutable[]
        parallax_stars0,
        parallax_stars1,
        parallax_stars2;
    protected final java.awt.Color
        parallax_color0 = new Color(0x333333),
        parallax_color1 = new Color(0x666666),
        parallax_color2 = new Color(0x888888),
        warp_trail_color = new Color(0xAACCEE);
    protected final Vector3
        parallax_fade0 = new Vector3(24, 24, 24),
        parallax_fade1 = new Vector3(24, 24, 24),
        parallax_fade2 = new Vector3(24, 24, 24),
        warp_trail_fade = new Vector3(24, 24, 24);


    protected BufferedImage
        fg = new BufferedImage(
            64, 64, BufferedImage.TYPE_INT_RGB
        );
    protected int[]
        fg_buffer = ((DataBufferInt)fg.getRaster().getDataBuffer()).getData();

    protected int
        afterburner = 0xaaccee;

    protected Sprite
        ship_sprite;

    @Override
    public void onAttach() {
        Vector2 wh = Engine.getVirtualCanvasSize();

        parallax_layer0 = new Layer((int)wh.x(), (int)wh.y());
        parallax_layer1 = new Layer((int)wh.x(), (int)wh.y());
        parallax_layer2 = new Layer((int)wh.x(), (int)wh.y());
        warp_trail_layer = new Layer((int)wh.x(), (int)wh.y());

        parallax_stars0 = new Vector3.Mutable[(int)wh.x()];
        parallax_stars1 = new Vector3.Mutable[(int)wh.x()];
        parallax_stars2 = new Vector3.Mutable[(int)wh.x()];

        Random random = new Random();
        for(int i = 0; i < 64; i ++) {
            parallax_stars0[i] = new Vector3.Mutable(i, wh.y() * (random.nextFloat() * 2f - 1f), random.nextFloat() + 1f);
            parallax_stars1[i] = new Vector3.Mutable(i, wh.y() * (random.nextFloat() * 2f - 1f), random.nextFloat() + 2f);
            parallax_stars2[i] = new Vector3.Mutable(i, wh.y() * (random.nextFloat() * 2f - 1f), random.nextFloat() + 3f);
        }

        ship_sprite = new Sprite(ship_atlas);
        x = wh.x() / 2;
        y = wh.y()    ;
    }

    float frame_count = 0;

    @Override
    public void onRenderImage(ImageContext context) {
        Arrays.fill(context.image_buffer, 0);
        renderParallaxLayer(
            context,
            parallax_layer0,
            parallax_stars0,
            parallax_color0,
            parallax_fade0
        );
        renderParallaxLayer(
            context,
            parallax_layer1,
            parallax_stars1,
            parallax_color1,
            parallax_fade1
        );
        renderParallaxLayer(
            context,
            parallax_layer2,
            parallax_stars2,
            parallax_color2,
            parallax_fade2
        );
        renderWarpTrailLayer(
            context,
            warp_trail_layer,
            warp_trail_color,
            warp_trail_fade
        );

        y = ((warp - min_warp) / (max_warp - min_warp)) * (min_y - max_y) + max_y;

        ship_sprite.x = x - ship_sprite.w / 2;
        ship_sprite.y = y - ship_sprite.h / 2;
        ship_sprite.onRenderImage(context);
    }

    @Override
    public void onUpdate(UpdateContext context) {
        if(Input.isKeyDown(VK_UP))
            warp = Math.min(warp + delta_warp, max_warp);
        if(Input.isKeyDown(VK_DOWN))
            warp = Math.max(warp - delta_warp, min_warp);

        if(Input.isKeyDown(VK_LEFT) || Input.isKeyDown(VK_RIGHT)) {
            if (Input.isKeyDown(VK_LEFT)) {
                x = Math.max(x - delta_strafe, min_x);
                ship_sprite.frame = 1;
            }
            if (Input.isKeyDown(VK_RIGHT)) {
                x = Math.min(x + delta_strafe, max_x);
                ship_sprite.frame = 2;
            }
        } else
            ship_sprite.frame = 0;
    }

    public void renderParallaxLayer(Renderable.ImageContext context, Layer layer, Vector3.Mutable[] stars, Color color, Vector3 fade) {
        Graphics2D g = layer.image.createGraphics();
        g.setColor(color);
        for(int i = 0; i < stars.length; i ++) {
            g.drawLine(
                (int)stars[i].x(),
                (int)stars[i].y(),
                (int)stars[i].x(),
                (int)stars[i].y(stars[i].y() + stars[i].z() * warp * context.fixed_dt).y()
            );
            if(stars[i].y() > context.h)
                stars[i].y(stars[i].y() % context.h - context.h);
        }
        g.dispose();
        for(int i = 0; i < layer.image_buffer.length; i ++) {
            int rgb = layer.image_buffer[i];
            rgb = Image.r(rgb, Math.max(0, Image.r(rgb) - (int)fade.x()));
            rgb = Image.g(rgb, Math.max(0, Image.g(rgb) - (int)fade.y()));
            rgb = Image.b(rgb, Math.max(0, Image.b(rgb) - (int)fade.z()));
            layer.image_buffer[i] = rgb;
        }
        layer.onRenderImage(context);
    }

    public void renderWarpTrailLayer(Renderable.ImageContext context, Layer layer, Color color, Vector3 fade) {
        for(int i = layer.image_buffer.length - 1; i >= 0; i --) {
            if(i - context.w >= 0)
                layer.image_buffer[i] = layer.image_buffer[i - context.w];
            else
                layer.image_buffer[i] = 0;
        }
        for(int i = 0; i < layer.image_buffer.length; i ++) {
            int rgb = layer.image_buffer[i];
            rgb = Image.r(rgb, Math.max(0, Image.r(rgb) - (int)fade.x()));
            rgb = Image.g(rgb, Math.max(0, Image.g(rgb) - (int)fade.y()));
            rgb = Image.b(rgb, Math.max(0, Image.b(rgb) - (int)fade.z()));
            layer.image_buffer[i] = rgb;
        }
        layer.onRenderImage(context);
    }
}
