package lrj.game;

import lrj.core.*;
import lrj.game.menu.GameOverMenu;
import lrj.game.menu.MainMenu;
import lrj.math.Box;
import lrj.math.Vector;
import lrj.math.Vector3;
import lrj.math.Vector4;
import lrj.util.Debug;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.*;
import static lrj.game.menu.Menu.*;
import static lrj.game.sprites.Sprites.*;
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
    public static final int
        ROCK_04_ID = 1,
        ROCK_08_ID = 2,
        ROCK_16_ID = 3,
        HEALTH_PICKUP_ID = 4,
        PHASER_PICKUP_ID = 5,
        SENSOR_PICKUP_ID = 6;
    public static final float
        MIN_WARP_PARALLAX =  8f,
        MAX_WARP_PARALLAX = 64f,
        DELTA_WARP        = .75f,
        MIN_SPRITE_SPEED = 8f,
        MAX_SPRITE_SPEED = 64f,
        MIN_DELTA_STRAFE = 31f,
        MAX_DELTA_STRAFE = 62f,
        MIN_DELTA_SCORE  =  1f,
        MAX_DELTA_SCORE  = 10f;
    public static final int
        MAX_HEALTH = 2,
        MAX_PHASER_POWER = 2,
        MAX_SENSOR_POWER = 2;
    public static final float
        INVINCIBILITY_TIMER = 3f,
        PHASER_TIMER = .15f,
        SENSOR_TIMER =  10f;

    public static final Vector3
        MIN_WARP_TRAIL = new Vector3(96, 96, 96),
        MAX_WARP_TRAIL = new Vector3(16,  8,  8);

    protected static final Random
        RANDOM = new Random();
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
        sprite_speed  = computeSpriteSpeed(warp),
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
        player_phaser_power,
        player_sensor_power;

    protected Vector4.Mutable[]
        sprites;
    protected float
        invincibility_timer,
        phaser_timer,
        sensor_timer;
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

        sprites = new Vector4.Mutable[18];

        for(int i = 0; i < CANVAS_W; i ++) {
            parallax_layer0_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * RANDOM.nextFloat() - 1f), RANDOM.nextFloat() + 1f);
            parallax_layer1_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * RANDOM.nextFloat() - 1f), RANDOM.nextFloat() + 2f);
            parallax_layer2_stars[i] = new Vector3.Mutable(i, CANVAS_H * (2f * RANDOM.nextFloat() - 1f), RANDOM.nextFloat() + 3f);
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
        player_score = 0f;
        player_health = 1;
        player_phaser_power = 1;
        player_sensor_power = 1;
        player_w = 10;
        player_h = 16;
        player_x = CANVAS_W / 2f;
        player_y = computePlayerPosition(warp);

        play_button.x = (CANVAS_W - play_button.w) / 2;
        menu_button.x = (CANVAS_W - play_button.w) / 2;
        play_button.y = CANVAS_H / 2 - play_button.h - 2;
        menu_button.y = CANVAS_H / 2;
    }

    public float computeWarpParallax(float warp) {
        return warp * (MAX_WARP_PARALLAX - MIN_WARP_PARALLAX) + MIN_WARP_PARALLAX;
    }

    public float computeSpriteSpeed(float warp) {
        return warp * (MAX_SPRITE_SPEED - MIN_SPRITE_SPEED) + MIN_SPRITE_SPEED;
    }

    public float computeDeltaStrafe(float warp) {
        return warp * (MAX_DELTA_STRAFE - MIN_DELTA_STRAFE) + MIN_DELTA_STRAFE;
    }

    public float computeDeltaScore(float warp) {
        return warp * (MAX_DELTA_SCORE - MIN_DELTA_SCORE) + MIN_DELTA_SCORE;
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
            sprite_speed = computeSpriteSpeed(warp);
            delta_strafe = computeDeltaStrafe(warp);
            delta_score  = computeDeltaScore(warp);
            warp_trail_fade = computeWarpTrailFade(warp);

            boolean
                l = Input.isKeyDown(VK_LEFT ) || Input.isKeyDown(VK_A),
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

            if(invincibility_timer > 0)
                invincibility_timer -= context.fixed_dt;
            if(phaser_timer > 0) {
                for(int i = 0; i < sprites.length; i ++)
                    if(sprites[i] != null) {
                        int size = 0;
                        switch((int)sprites[i].w()) {
                            case ROCK_04_ID: size =  4; break;
                            case ROCK_08_ID: size =  8; break;
                            case ROCK_16_ID: size = 16; break;
                        }

                        if(
                            sprites[i].x() <= player_x && sprites[i].x() + size >= player_x &&
                            sprites[i].y() <= player_y
                        )
                            sprites[i] = null;
                    }
                phaser_timer -= context.fixed_dt;
            }
            if(sensor_timer > 0)
                sensor_timer -= context.fixed_dt;




            updateSprites(context);
        }
    }

    public void updateSprites(UpdateContext context) {
        int null_count = 0;
        for(int i = 0; i < sprites.length; i ++) {
            if(sprites[i] != null) {
                sprites[i].y(sprites[i].y() + sprites[i].z() * sprite_speed * context.fixed_dt);
                if (sprites[i].y() > CANVAS_H) {
                    sprites[i] = null;
                    null_count ++;
                } else
                    switch((int)sprites[i].w()) {
                        case ROCK_04_ID: if(isPlayerTouchingRock04(sprites[i])) onPlayerDamage(); break;
                        case ROCK_08_ID: if(isPlayerTouchingRock08(sprites[i])) onPlayerDamage(); break;
                        case ROCK_16_ID: if(isPlayerTouchingRock16(sprites[i])) onPlayerDamage(); break;
                        case HEALTH_PICKUP_ID: if(isPlayerTouchingHealthPickup(sprites[i]) && onPlayerHealthPickup()) {
                            sprites[i] = null;
                            null_count ++;
                        } break;
                        case PHASER_PICKUP_ID: if(isPlayerTouchingPhaserPickup(sprites[i]) && onPlayerPhaserPickup()) {
                            sprites[i] = null;
                            null_count ++;
                        } break;
                        case SENSOR_PICKUP_ID: if(isPlayerTouchingSensorPickup(sprites[i]) && onPlayerSensorPickup()) {
                            sprites[i] = null;
                            null_count ++;
                        } break;
                    }
            } else
                null_count ++;
        }
        if(null_count >= sprites.length)
            spawnSprites();//spawn wave
    }

    public boolean isPlayerTouchingRock04(Vector p) {
        float
            dx = player_x -     p.x() - 2,
            dy = player_y - 3 - p.y() - 2;
        return dx * dx + dy * dy < 36; // (4.5 + 1.5) ^ 2
    }

    public boolean isPlayerTouchingRock08(Vector p) {
        float
            dx = player_x -     p.x() - 4,
            dy = player_y - 3 - p.y() - 4;
        return dx * dx + dy * dy < 64; // (4.5 + 3.5) ^ 2
    }

    public boolean isPlayerTouchingRock16(Vector p) {
        float
            dx = player_x -     p.x() - 8,
            dy = player_y - 3 - p.y() - 8;
        return dx * dx + dy * dy < 144; // (4.5 + 7.5) ^ 2
    }

    public boolean isPlayerTouchingHealthPickup(Vector p) {
        return
            player_x - player_w / 2 < p.x() + 8 && player_x + player_w / 2 > p.x() &&
            player_y - player_h / 2 < p.y() + 8 && player_y + player_h / 2 > p.y();
    }

    public boolean isPlayerTouchingPhaserPickup(Vector p) {
        return
            player_x - player_w / 2 < p.x() + 8 && player_x + player_w / 2 > p.x() &&
            player_y - player_h / 2 < p.y() + 8 && player_y + player_h / 2 > p.y();
    }

    public boolean isPlayerTouchingSensorPickup(Vector p) {
        return
            player_x - player_w / 2 < p.x() + 8 && player_x + player_w / 2 > p.x() &&
            player_y - player_h / 2 < p.y() + 8 && player_y + player_h / 2 > p.y();
    }

    public void onPlayerDamage() {
        if(invincibility_timer <= 0) {
            player_health --;
            if(player_health < 0)
                Engine.setCurrentScene(new GameOverMenu((int)player_score));
            else
                invincibility_timer = INVINCIBILITY_TIMER;
        }
    }

    public boolean onPlayerHealthPickup() {
        if(player_health < MAX_HEALTH) {
            player_health ++;
            return true;
        } else
            return false;
    }

    public boolean onPlayerPhaserPickup() {
        if(player_phaser_power < MAX_PHASER_POWER) {
            player_phaser_power ++;
            return true;
        } else
            return false;
    }

    public boolean onPlayerSensorPickup() {
        if(
            (sensor_timer >  0 && player_sensor_power < MAX_SENSOR_POWER - 1) ||
            (sensor_timer <= 0 && player_sensor_power < MAX_SENSOR_POWER    )
        ) {
            player_sensor_power ++;
            return true;
        } else
            return false;
    }

    public boolean onPlayerPhaserPower() {
        if(player_phaser_power > 0 && phaser_timer <= 0) {
            phaser_timer = PHASER_TIMER;
            player_phaser_power --;
            return true;
        } else
            return false;
    }

    public boolean onPlayerSensorPower() {
        if(player_sensor_power > 0 && sensor_timer <= 0) {
            sensor_timer = SENSOR_TIMER;
            player_sensor_power --;
            return true;
        } else
            return false;
    }

    @Override
    public void onRenderImage(ImageContext context) {
        Arrays.fill(context.image_buffer, 0x080808);
        renderParallaxLayers(context);
        renderSprites(context);

        if(phaser_timer > 0) {
            int phaser_color = (int)(255 * phaser_timer / PHASER_TIMER);
            for(int i = 0; i < player_y; i ++)
                context.r((int) player_x    , i, phaser_color);
        }

        if(sensor_timer > 0) {
            Graphics2D g = context.image.createGraphics();
            int color = ((int)(context.t * 6) & 1) == 1 ? (int)(247 * sensor_timer / SENSOR_TIMER) + 8 : 255;
            Color
                danger_color = new Color(color,     0,     0),
                pickup_color = new Color(    0, color, color);
            for(int i = 0; i < sprites.length; i ++)
                if(sprites[i] != null && sprites[i].y() < 0)
                    switch((int)sprites[i].w()) {
                        case ROCK_04_ID: g.setColor(danger_color); g.drawLine((int)sprites[i].x(), 0, (int)sprites[i].x() +  4, 0); break;
                        case ROCK_08_ID: g.setColor(danger_color); g.drawLine((int)sprites[i].x(), 0, (int)sprites[i].x() +  8, 0); break;
                        case ROCK_16_ID: g.setColor(danger_color); g.drawLine((int)sprites[i].x(), 0, (int)sprites[i].x() + 16, 0); break;
                        case HEALTH_PICKUP_ID:
                        case PHASER_PICKUP_ID:
                        case SENSOR_PICKUP_ID: g.setColor(pickup_color); g.drawLine((int)sprites[i].x(), 0, (int)sprites[i].x() + 8, 0); break;
                    }
            g.dispose();
        }

        // blink ship
        if(!pause && invincibility_timer > 0) {
             if(((int)(context.t * 18) & 1) == 0)
                 ship_sprite.onRenderImage(context);
        } else
            ship_sprite.onRenderImage(context);


        renderWarpTrail(context);



        renderHUD(context);

        if(pause)
            renderPauseMenu(context);
    }

    @Override
    public void onKeyDown(int key) {
        switch(key) {
            case VK_ESCAPE:
                pause = !pause; break;
            case VK_Q:
            case VK_Z:
                onPlayerPhaserPower(); break;
            case VK_E:
            case VK_X:
                onPlayerSensorPower(); break;
        }
    }

    @Override
    public void onMouseUp(int mouse) {
        if (pause && mouse == 1) {
            if (isButtonHover(play_button)) onPlayButton();
            if (isButtonHover(menu_button)) onMenuButton();
        }
    }

    public void onPlayButton() {
        pause = false;
    }

    public void onMenuButton() {
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
            parallax_layer0_fade
        );
        renderParallaxLayer(
            context,
            parallax_layer1_image,
            parallax_layer1_image_buffer,
            parallax_layer1_stars,
            parallax_layer1_color,
            parallax_layer1_fade
        );
        renderParallaxLayer(
            context,
            parallax_layer2_image,
            parallax_layer2_image_buffer,
            parallax_layer2_stars,
            parallax_layer2_color,
            parallax_layer2_fade
        );
    }

    public void renderParallaxLayer(
        Renderable.ImageContext context,
        BufferedImage     image,
        int[]             image_buffer,
        Vector3.Mutable[] stars,
        Color             color,
        Vector3           fade
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
                    (int) stars[i].y(stars[i].y() + stars[i].z() * warp_parallax * context.fixed_dt).y()
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
        if(!pause) {
            // translate
            for (int i = warp_trail_image_buffer.length - 1; i >= 0; i--) {
                if (i - CANVAS_W >= 0)
                    warp_trail_image_buffer[i] = warp_trail_image_buffer[i - CANVAS_W];
                else
                    warp_trail_image_buffer[i] = 0;
            }

            // draw
            boolean
                l = Input.isKeyDown(VK_LEFT) || Input.isKeyDown(VK_A),
                r = Input.isKeyDown(VK_RIGHT) || Input.isKeyDown(VK_D);

            switch ((int) ship_sprite.frame) {
                case 0:
                    warp_trail_image_buffer[(int) (player_y + 7) * CANVAS_W + (int) (player_x - 5)] = warp_trail_color.getRGB();
                    warp_trail_image_buffer[(int) (player_y + 7) * CANVAS_H + (int) (player_x + 4)] = warp_trail_color.getRGB();
                    break;
                case 1:
                    warp_trail_image_buffer[(int) (player_y + 7) * CANVAS_W + (int) (player_x - 5)] = warp_trail_color.getRGB();
                    warp_trail_image_buffer[(int) (player_y + 7) * CANVAS_H + (int) (player_x + 2)] = warp_trail_color.getRGB();
                    break;
                case 2:
                    warp_trail_image_buffer[(int) (player_y + 7) * CANVAS_W + (int) (player_x - 3)] = warp_trail_color.getRGB();
                    warp_trail_image_buffer[(int) (player_y + 7) * CANVAS_H + (int) (player_x + 4)] = warp_trail_color.getRGB();
                    break;
            }

            // fade
            for (int i = 0; i < warp_trail_image_buffer.length; i++) {
                int rgb = warp_trail_image_buffer[i];
                rgb = Image.r(rgb, Math.max(0, Image.r(rgb) - (int) warp_trail_fade.x()));
                rgb = Image.g(rgb, Math.max(0, Image.g(rgb) - (int) warp_trail_fade.y()));
                rgb = Image.b(rgb, Math.max(0, Image.b(rgb) - (int) warp_trail_fade.z()));
                warp_trail_image_buffer[i] = rgb;
            }
        }

        // mix
        for(int i = 0; i < warp_trail_image_buffer.length; i ++)
            if((warp_trail_image_buffer[i] & 0xffffff) != 0)
                context.image_buffer[i] = warp_trail_image_buffer[i];
    }

    public void renderSprites(Renderable.ImageContext context) {
        for(int i = 0; i < sprites.length; i ++)
            renderSprite(context, sprites[i]);
    }

    public void renderSprite(Renderable.ImageContext context, Vector4 t) {
        if(t != null) {
            Sprite sprite = null;
            switch ((int) t.w()) {
                case ROCK_04_ID: sprite = rock_04_sprite; break;
                case ROCK_08_ID: sprite = rock_08_sprite; break;
                case ROCK_16_ID: sprite = rock_16_sprite; break;
                case HEALTH_PICKUP_ID: sprite = health_icon_sprite; break;
                case PHASER_PICKUP_ID: sprite = phaser_icon_sprite; break;
                case SENSOR_PICKUP_ID: sprite = sensor_icon_sprite; break;
            }
            if(sprite != null) {
                sprite.x = t.x();
                sprite.y = t.y();
                sprite.onRenderImage(context);
            }
        }
    }

    public void renderHUD(Renderable.ImageContext context) {
        for(int i = 0; i < context.w * 10; i ++)
            context.image_buffer[context.image_buffer.length - i - 1] = 0x080808;

        for(int i = 0; i < player_health; i ++) {
            health_icon_sprite.x = (CANVAS_W - (health_icon_sprite.w) * player_health) / 2 + i * (health_icon_sprite.w);
            health_icon_sprite.y = CANVAS_H - health_icon_sprite.h - 1;
            health_icon_sprite.onRenderImage(context);
        }

        for(int i = 0; i < player_phaser_power; i ++) {
            phaser_icon_sprite.x = 1  +  i * (phaser_icon_sprite.w + 1);
            phaser_icon_sprite.y = CANVAS_H - phaser_icon_sprite.h - 1;
            phaser_icon_sprite.onRenderImage(context);
        }

        for(int i = 0; i < player_sensor_power + (sensor_timer > 0 ? 1 : 0); i ++) {
            sensor_icon_sprite.x = CANVAS_W - sensor_icon_sprite.w - 1 - i * (sensor_icon_sprite.w + 1);
            sensor_icon_sprite.y = CANVAS_H - health_icon_sprite.h - 1;

            if(!pause && i == player_sensor_power) {
                if (((int)(context.t * 6) & 1) == 0)
                    sensor_icon_sprite.onRenderImage(context);
            } else
                sensor_icon_sprite.onRenderImage(context);
        }
    }

    public void renderPauseMenu(Renderable.ImageContext context) {
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

    public static final float
        DIFFICULTY_CEILING = 1000f,
        EASY_ROCK_SPAWN_CHANCE   = .5f,
        HARD_ROCK_SPAWN_CHANCE   =  1f,
        EASY_PICKUP_SPAWN_CHANCE =  1f,
        HARD_PICKUP_SPAWN_CHANCE = .2f;

    public void spawnSprites() {
        float
            difficulty = Math.min(player_score / DIFFICULTY_CEILING, 1f),
            rock_spawn_chance   = difficulty * (HARD_ROCK_SPAWN_CHANCE   - EASY_ROCK_SPAWN_CHANCE  ) + EASY_ROCK_SPAWN_CHANCE  ,
            pickup_spawn_chance = difficulty * (HARD_PICKUP_SPAWN_CHANCE - EASY_PICKUP_SPAWN_CHANCE) + EASY_PICKUP_SPAWN_CHANCE;

        // spawn rocks
        boolean
            h_gap_enabled = false,
            v_gap_enabled = false;

        switch(useSpawnTable(
            RANDOM.nextFloat(),
            0 /* no gap */, player_phaser_power > 0 ? 1 : 0,
            1 /* h  gap */, 3,
            2 /* v  gap */, 2
        )) {
            case 0: break; // do nothing
            case 1: h_gap_enabled = true; break;
            case 2: v_gap_enabled = true; break;
        }

        int
            i = 0,
            x = 0,
            y = - CANVAS_H - CANVAS_H,
            _h = 0;

        int
            maximum_h_gap = 0,
            current_h_gap = 0;

        while(x < CANVAS_W) {
            switch(useSpawnTable(
                RANDOM.nextFloat(),
                4 , 1,
                8 , CANVAS_W - x >=  8 ? 4 : 0,
                16, CANVAS_W - x >= 16 ? 2 : 0
            )) {
                case  4:
                    if(RANDOM.nextFloat() < rock_spawn_chance) {
                        sprites[i ++] = new Vector4.Mutable(x, RANDOM.nextBoolean() ? (y -=  4) : (y += _h), 1f, ROCK_04_ID);
                        current_h_gap =  0;
                        _h = 4;
                    } else
                        current_h_gap += 4;
                    x +=  4;
                    break;
                case  8:
                    if(RANDOM.nextFloat() < rock_spawn_chance) {
                        sprites[i ++] = new Vector4.Mutable(x, RANDOM.nextBoolean() ? (y -=  8) : (y += _h), 1f, ROCK_08_ID);
                        current_h_gap =  0;
                        _h = 8;
                    } else
                        current_h_gap += 8;
                    x +=  8;
                    break;
                case 16:
                    if(RANDOM.nextFloat() < rock_spawn_chance) {
                        sprites[i ++] = new Vector4.Mutable(x, RANDOM.nextBoolean() ? (y -= 16) : (y += _h), 1f, ROCK_16_ID);
                        current_h_gap =   0;
                        _h = 16;
                    } else
                        current_h_gap =  16;
                    x += 16;
                    break;
            }
            if(current_h_gap > maximum_h_gap)
                maximum_h_gap = current_h_gap;
        }

        if(h_gap_enabled && maximum_h_gap < 12) {
            int h_gap = 0;
            if(RANDOM.nextBoolean()) {
                // remove sprites from the left until gap is created
                for(int j = 0; j < i && h_gap < 12; j ++)
                    if(sprites[j] != null) {
                        h_gap = (int)sprites[j].x();
                        if(h_gap < 12) {
                            switch ((int) sprites[j].w()) {
                                case ROCK_04_ID: h_gap +=  4; break;
                                case ROCK_08_ID: h_gap +=  8; break;
                                case ROCK_16_ID: h_gap += 16; break;
                            }
                            sprites[j] = null;
                        }
                    }
            } else {
                // remove sprites from the right until gap is created
                for(int j = i - 1; j >= 0 && h_gap < 12; j --)
                    if(sprites[j] != null) {
                        h_gap = CANVAS_W - (int)sprites[j].x();
                        sprites[j] = null;
                    }
            }
        }

        if(v_gap_enabled) {
            int k = 0, dy = RANDOM.nextBoolean() ? 32 : -32;
            if(RANDOM.nextBoolean()) {
                for(int j = 0; j < i && k < 3; j ++)
                    if(sprites[j] != null) {
                        sprites[j].y(sprites[j].y() + dy);
                        k ++;
                    }
            } else {
                for(int j = i - 1; j >= 0 && k < 3; j --)
                    if(sprites[j] != null) {
                        sprites[j].y(sprites[j].y() + dy);
                        k ++;
                    }
            }
        }

        // spawn pickups
        int a = -1, b = -1;
        for(int j = 1; j < i - 1; j ++)
            if(sprites[j] != null)
                a = j;
        for(int j = i - 2; j >= 1; j --)
            if(sprites[j] != null)
                b = j;
        int
            PICKUP_ID_0 = useSpawnTable(
                RANDOM.nextFloat(),
                HEALTH_PICKUP_ID, player_health < MAX_HEALTH ? player_health == 0 ? 12 : 1 : 0,
                PHASER_PICKUP_ID, 4,
                SENSOR_PICKUP_ID, 4
            ),
            PICKUP_ID_1 = useSpawnTable(
                RANDOM.nextFloat(),
                HEALTH_PICKUP_ID, player_health < MAX_HEALTH ? player_health == 0 ? 12 : 1 : 0,
                PHASER_PICKUP_ID, 4,
                SENSOR_PICKUP_ID, 4
            );

        int h2 = 0;
        if(a != b) {
            if(RANDOM.nextFloat() < pickup_spawn_chance) {
                switch ((int) sprites[a].w()) {
                    case ROCK_04_ID: h2 = 2; break;
                    case ROCK_08_ID: h2 = 4; break;
                    case ROCK_16_ID: h2 = 8; break;
                }
                sprites[i++] = new Vector4.Mutable(sprites[a].x() + h2 - 4, sprites[a].y() + h2 - 4 + (RANDOM.nextBoolean() ? 16 : -16), 1f, PICKUP_ID_0);
            }
            if(RANDOM.nextFloat() < pickup_spawn_chance) {
                switch ((int) sprites[b].w()) {
                    case ROCK_04_ID: h2 = 2; break;
                    case ROCK_08_ID: h2 = 4; break;
                    case ROCK_16_ID: h2 = 8; break;
                }
                sprites[i] = new Vector4.Mutable(sprites[b].x() + h2 - 4, sprites[b].y() + h2 - 4 + (RANDOM.nextBoolean() ? 16 : -16), 1f, PICKUP_ID_1);
            }
        } else if(a != -1) {
            if(RANDOM.nextFloat() < pickup_spawn_chance || RANDOM.nextFloat() < pickup_spawn_chance) {
                switch ((int) sprites[a].w()) {
                    case ROCK_04_ID: h2 = 2; break;
                    case ROCK_08_ID: h2 = 4; break;
                    case ROCK_16_ID: h2 = 8; break;
                }
                sprites[i] = new Vector4.Mutable(sprites[a].x() + h2 - 4, sprites[a].y() + h2 - 4 + (RANDOM.nextBoolean() ? 16 : -16), 1f, PICKUP_ID_0);
            }
        }
    }

    public int useSpawnTable(float random, Object... args) {
        // populate spawn table
        int
            n = args.length & 1,
            m = args.length - n;
        int l = 0;
        for(int i = 1; i < m; i += 2)
            if((int)args[i] > 0)
                l ++;
        int[]
            t0 = new int[l],
            t1 = new int[l];
        int i = 0, t = 0;
        for(int j = 0; j < m && i < l; j += 2) {
            int
                a = (j + 0),
                b = (j + 1);
            if((int)args[b] > 0) {
                t0[i] = (     (int)args[a]);
                t1[i] = (t += (int)args[b]);
                i ++;
            }
        }

        // use spawn table
        int u = (int)(random * t);
        for(int j = 0; j < l; j ++)
            if(u <= t1[j])
                return t0[j];
        return -1;
    }
}
