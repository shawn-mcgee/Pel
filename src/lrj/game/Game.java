package lrj.game;

import lrj.core.*;
import lrj.game.menu.MainMenu;
import lrj.math.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static lrj.game.menu.Menu.*;
import static lrj.game.sprites.Sprites.*;
import static java.awt.event.KeyEvent.*;
import static lrj.math.Vector.*;

public class Game extends Scene {
    public static final int
        CANVAS_W = 64,
        CANVAS_H = 64,
        HUD_H    = 10,
        MIN_X =            1,
        MAX_X = CANVAS_W - 1,
        MIN_Y =            1,
        MAX_Y = CANVAS_H - 1 - HUD_H;

    public static final float
        MIN_WARP_PARALLAX =  8f,
        MAX_WARP_PARALLAX = 64f,
        DELTA_WARP        = .75f,
        MIN_WARP_STRAFE   = 31f,
        MAX_WARP_STRAFE   = 62f,
        MIN_WARP_SCORE    =  1f,
        MAX_WARP_SCORE    = 10f;

    public static final int
        MAX_HEALTH = 3,
        MAX_PHASER = 2,
        MAX_SENSOR = 2;

    public static final Vector3
        MIN_WARP_TRAIL = new Vector3(96, 96, 96),
        MAX_WARP_TRAIL = new Vector3(16,  8,  8);

    protected Sprite
        ship_sprite,
        health_icon_sprite,
        phaser_icon_sprite,
        sensor_icon_sprite,
        rock_16_sprite,
        rock_08_sprite,
        rock_04_sprite,
        number,
        play_button,
        menu_button;

    protected float
        warp = 0f;
    protected float
        warp_parallax = computeWarpParallax(warp),
        delta_strafe  = computeDeltaStrafe(warp),
        delta_score   = computeDeltaScore(warp);

    protected float
        player_x,
        player_y,
        player_w,
        player_h,
        player_score;
    protected int
        player_health,
        player_phaser,
        player_sensor;

    protected Sprite[]
        sprites;

    protected boolean
        pause;



    public Game() {
        parallax_layer0_image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);
        parallax_layer1_image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);
        parallax_layer2_image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);
        warp_trail_image      = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_RGB);

        parallax_layer0_image_buffer = ((DataBufferInt)parallax_layer0_image.getRaster().getDataBuffer()).getData();
        parallax_layer1_image_buffer = ((DataBufferInt)parallax_layer1_image.getRaster().getDataBuffer()).getData();
        parallax_layer2_image_buffer = ((DataBufferInt)parallax_layer2_image.getRaster().getDataBuffer()).getData();
        warp_trail_image_buffer      = ((DataBufferInt)warp_trail_image.getRaster().getDataBuffer()).getData();

        parallax_layer0_stars = new Vector3.Mutable[CANVAS_W];
        parallax_layer1_stars = new Vector3.Mutable[CANVAS_W];
        parallax_layer2_stars = new Vector3.Mutable[CANVAS_W];

        Random random = new Random();
        for(int i = 0; i < CANVAS_W; i ++) {
            parallax_layer0_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * random.nextFloat() - 1f), random.nextFloat() + 1f);
            parallax_layer1_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * random.nextFloat() - 1f), random.nextFloat() + 2f);
            parallax_layer2_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * random.nextFloat() - 1f), random.nextFloat() + 3f);
        }

        ship_sprite = new Sprite(LRJ_SHIP);
        health_icon_sprite = new Sprite(LRJ_HEALTH_ICON);
        phaser_icon_sprite = new Sprite(LRJ_PHASER_ICON);
        sensor_icon_sprite = new Sprite(LRJ_SENSOR_ICON);
        rock_16_sprite = new Sprite(LRJ_ROCK_16);
        rock_08_sprite = new Sprite(LRJ_ROCK_08);
        rock_04_sprite = new Sprite(LRJ_ROCK_04);
        number = new Sprite(LRJ_NUMBER);
        play_button = new Sprite(LRJ_PLAY_BUTTON);
        menu_button = new Sprite(LRJ_MENU_BUTTON);
    }

    @Override
    public void onAttach() {
        onReset();
    }

    public void onReset() {
        player_health = 1;
        player_phaser = 1;
        player_sensor = 1;
        player_w = 10;
        player_h = 16;
        player_x = CANVAS_W / 2f;
        player_y = computePlayerPosition(warp);
        sprites = new Sprite[36];

        play_button.x = (CANVAS_W - play_button.w) / 2;
        menu_button.x = (CANVAS_W - play_button.w) / 2;
        play_button.y = CANVAS_H / 2 - play_button.h - 2;
        menu_button.y = CANVAS_H / 2;
    }

    public float computeWarpParallax(float warp) {
        return warp * (MAX_WARP_PARALLAX - MIN_WARP_PARALLAX) + MIN_WARP_PARALLAX;
    }

    public float computeDeltaStrafe(float warp) {
        return warp * (MAX_WARP_STRAFE - MIN_WARP_STRAFE) + MIN_WARP_STRAFE;
    }

    public float computeDeltaScore(float warp) {
        return warp * (MAX_WARP_SCORE - MIN_WARP_SCORE) + MIN_WARP_SCORE;
    }

    public Vector3 computeWarpTrailFade(float warp) {
        float _warp = 1f - warp;
        return add(mul(sub(MAX_WARP_TRAIL, MIN_WARP_TRAIL), 1f - _warp * _warp * _warp), MIN_WARP_TRAIL);
    }

    public float computePlayerPosition(float warp) {
        return warp * (MIN_Y - MAX_Y + player_h) + MAX_Y - (player_h / 2);
    }

    @Override
    public void onUpdate(UpdateContext context) {
        if(!pause) {
            if (Input.isKeyDown(VK_UP) || Input.isKeyDown(VK_W))
                warp = Math.min(warp + DELTA_WARP * context.fixed_dt, 1f);
            if (Input.isKeyDown(VK_DOWN) || Input.isKeyDown(VK_S))
                warp = Math.max(warp - DELTA_WARP * context.fixed_dt, 0f);

            warp_parallax = computeWarpParallax(warp);
            delta_strafe = computeDeltaStrafe(warp);
            delta_score = computeDeltaScore(warp);
            warp_trail_fade = computeWarpTrailFade(warp);

            boolean
                l = Input.isKeyDown(VK_LEFT) || Input.isKeyDown(VK_A),
                r = Input.isKeyDown(VK_RIGHT) || Input.isKeyDown(VK_D);

            if (l ^ r) {
                if (l) {
                    player_x = Math.max(player_x - delta_strafe * context.fixed_dt, MIN_X + player_w / 2);
                    ship_sprite.frame = 1;
                }
                if (r) {
                    player_x = Math.min(player_x + delta_strafe * context.fixed_dt, MAX_X - player_w / 2);
                    ship_sprite.frame = 2;
                }
            } else
                ship_sprite.frame = 0;

            player_y = computePlayerPosition(warp);
            ship_sprite.x = player_x - ship_sprite.w / 2;
            ship_sprite.y = player_y - ship_sprite.h / 2;

            player_score += delta_score * context.fixed_dt;
        }
    }

    ArrayList<Sprite>
        _sprites = spawnSprites(16);

    @Override
    public void onRenderImage(ImageContext context) {
        Arrays.fill(context.image_buffer, 0x080808);
        renderParallaxLayers(context);
        renderWarpTrail(context);

//        for(Sprite sprite: _sprites)
//            sprite.onRenderImage(context);

        ship_sprite.onRenderImage(context);

        renderHUD(context);

        if(pause) {
            for(int i = 0; i < CANVAS_W * HUD_H; i ++)
                context.image_buffer[i] = 0x080808;

            for(int i = 0; i < 8; i ++) {
                number.frame = (int)(player_score / Math.pow(10, i)) % 10;
                number.x = context.w - number.w - (i * number.w);
                number.y = 1;

                number.onRenderImage(context);
            }

            play_button.frame = 0;
            if (isButtonHover(play_button)) play_button.frame = ON_HOVER;
            if (isButtonPress(play_button)) play_button.frame = ON_PRESS;

            menu_button.frame = 0;
            if (isButtonHover(menu_button)) menu_button.frame = ON_HOVER;
            if (isButtonPress(menu_button)) menu_button.frame = ON_PRESS;

            play_button.onRenderImage(context);
            menu_button.onRenderImage(context);
        }
    }

    @Override
    public void onKeyDown(int key) {
        if(key == VK_ESCAPE)
            pause = !pause;
    }

    @Override
    public void onMouseUp(int mouse) {
        if (mouse == 1) {
            if (isButtonHover(play_button)) onPlay();
            if (isButtonHover(menu_button)) onMenu();
        }
    }

    public void onPlay() {
        pause = false;
    }

    public void onMenu() {
        Engine.setCurrentScene(new MainMenu());
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

    public void renderParallaxLayers(Renderable.ImageContext context) {
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
        Renderable.ImageContext context,
        BufferedImage     image,
        int[]             image_buffer,
        Vector3.Mutable[] stars,
        Color             color,
        Vector3           fade,
        float             parallax
    ) {
        if(!pause) {
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
        }

        // mix
        for(int i = 0; i < image_buffer.length; i ++)
            if((image_buffer[i] & 0xffffff) != 0)
                context.image_buffer[i] = image_buffer[i];
    }

    protected BufferedImage
        warp_trail_image;
    protected int[]
        warp_trail_image_buffer;
    protected final java.awt.Color
        warp_trail_color = new Color(0xAACCEE);
    protected Vector3
        warp_trail_fade = computeWarpTrailFade(warp);

    public void renderWarpTrail(Renderable.ImageContext context) {
        renderWarpTrail(
            context,
            warp_trail_image,
            warp_trail_image_buffer,
            warp_trail_color,
            warp_trail_fade
        );
    }

    public void renderWarpTrail(
        Renderable.ImageContext context,
        BufferedImage image,
        int[] image_buffer,
        Color color,
        Vector3 fade
    ) {
        if(!pause) {
            // translate
            for (int i = image_buffer.length - 1; i >= 0; i--) {
                if (i - CANVAS_W >= 0)
                    image_buffer[i] = image_buffer[i - CANVAS_W];
                else
                    image_buffer[i] = 0;
            }

            // draw
            boolean
                l = Input.isKeyDown(VK_LEFT) || Input.isKeyDown(VK_A),
                r = Input.isKeyDown(VK_RIGHT) || Input.isKeyDown(VK_D);

            switch ((int) ship_sprite.frame) {
                case 0:
                    image_buffer[(int) (player_y + 7) * CANVAS_W + (int) (player_x - 5)] = color.getRGB();
                    image_buffer[(int) (player_y + 7) * CANVAS_H + (int) (player_x + 4)] = color.getRGB();
                    break;
                case 1:
                    image_buffer[(int) (player_y + 7) * CANVAS_W + (int) (player_x - 5)] = color.getRGB();
                    image_buffer[(int) (player_y + 7) * CANVAS_H + (int) (player_x + 2)] = color.getRGB();
                    break;
                case 2:
                    image_buffer[(int) (player_y + 7) * CANVAS_W + (int) (player_x - 3)] = color.getRGB();
                    image_buffer[(int) (player_y + 7) * CANVAS_H + (int) (player_x + 4)] = color.getRGB();
                    break;
            }

            // fade
            for (int i = 0; i < image_buffer.length; i++) {
                int rgb = image_buffer[i];
                rgb = Image.r(rgb, Math.max(0, Image.r(rgb) - (int) fade.x()));
                rgb = Image.g(rgb, Math.max(0, Image.g(rgb) - (int) fade.y()));
                rgb = Image.b(rgb, Math.max(0, Image.b(rgb) - (int) fade.z()));
                image_buffer[i] = rgb;
            }
        }

        // mix
        for(int i = 0; i < image_buffer.length; i ++)
            if((image_buffer[i] & 0xffffff) != 0)
                context.image_buffer[i] = image_buffer[i];
    }

    public void renderHUD(Renderable.ImageContext context) {
        for(int i = 0; i < context.w * 10; i ++)
            context.image_buffer[context.image_buffer.length - i - 1] = 0x080808;

        for(int i = 0; i < player_health; i ++) {
            health_icon_sprite.x = (CANVAS_W - (health_icon_sprite.w) * player_health) / 2 + i * (health_icon_sprite.w);
            health_icon_sprite.y = CANVAS_H - health_icon_sprite.h - 1;
            health_icon_sprite.onRenderImage(context);
        }

        for(int i = 0; i < player_phaser; i ++) {
            phaser_icon_sprite.x = 1  +  i * (phaser_icon_sprite.w + 1);
            phaser_icon_sprite.y = CANVAS_H - phaser_icon_sprite.h - 1;
            phaser_icon_sprite.onRenderImage(context);
        }

        for(int i = 0; i < player_sensor; i ++) {
            sensor_icon_sprite.x = CANVAS_W - sensor_icon_sprite.w - 1 - i * (sensor_icon_sprite.w + 1);
            sensor_icon_sprite.y = CANVAS_H - health_icon_sprite.h - 1;
            sensor_icon_sprite.onRenderImage(context);
        }
    }


    protected float
        sprite_spawn_chance = 1f,
        rock_spawn_chance,
        power_spawn_chance;

    protected int
        health_spawn_weight,
        phaser_spawn_weight,
        sensor_spawn_weight;

    protected static final Random
        random = new Random();
    public ArrayList<Sprite> spawnSprites(int y) {
        ArrayList<Sprite> sprites = new ArrayList<Sprite>();
        int
            i = 0,
            _x = 0,
            _y = y,
            _h = 0;
        while(_x < CANVAS_W) {

            int w = 0;
            if(CANVAS_W - _x < 16) w = 1;
            if(CANVAS_W - _x <  8) w = 2;

            Sprite sprite = null;
            switch(random.nextInt(3 - w) + w) {
                case 0: sprite = new Sprite(LRJ_ROCK_16); break;
                case 1: sprite = new Sprite(LRJ_ROCK_08); break;
                case 2: sprite = new Sprite(LRJ_ROCK_04); break;
            }

            if(sprite == null)
                return sprites;

            sprite.x = _x;
            if(random.nextBoolean())
                sprite.y = (_y -= sprite.h) + sprite.h / 2;
            else
                sprite.y = (_y += sprite.h) - sprite.h / 2;
            _x += sprite.w;

            sprites.add(sprite);
        }

        return sprites;

//        boolean
//                spawn_sprite = random.nextFloat() < sprite_spawn_chance,
//                spawn_rock   = random.nextFloat() < rock_spawn_chance,
//                spawn_power  = random.nextFloat() < power_spawn_chance;
//
//
//
//        boolean
//                can_spawn_phaser = player_phaser < MAX_PHASER,
//                can_spawn_sensor = player_sensor < MAX_SENSOR,
//                can_spawn_health = player_health < MAX_HEALTH,
//                can_spawn_power  = can_spawn_health || can_spawn_phaser || can_spawn_sensor;
//
//
//
//
//        int
//                power_spawn_weight =
//                (can_spawn_health ? health_spawn_weight : 0) +
//                        (can_spawn_phaser ? phaser_spawn_weight : 0) +
//                        (can_spawn_sensor ? sensor_spawn_weight : 0);
//
//        int[] power_spawn_table_range;
//        String[] power_spawn_table_id;


        //smallest possible y gap that is still possible
        // 0 with phaser
        // without phaser?
        // ship_h? ship_h + 1? ship_h + 2?
        // maximum gap possible 2 * ship_h

        //spawning power-ups?
        // inline, yes easiest
        // directly in front of rocks? yes hardest to execute, but no tradeoff
        // directly behind rocks? yes  hardest? (requires shooting a rock... but once that happens should be easy to acquire) easy but requires tradeoff




        // even rows from the left
        // odd rows from the right


        // up to one of each powerup per row?
        // no, up to 2 power-ups per row


        // maximum sprites size
        // up to 2 rows at once

        // 16+2 * 2 = 36
    }

    public static void populateSpawnTable(int[] range, String[] id, Object... args) {

    }
}
