package lrj.game.sprites;

import lrj.core.Sprite;

public class Sprites {
    public static final Sprite.Atlas
        LRJ_SHIP = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_ship.png", "lrj_ship", 16, 16),
        LRJ_HEALTH_ICON = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_health_icon.png", "lrj_health_icon", 8, 8),
        LRJ_PHASER_ICON = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_phaser_icon.png", "lrj_phaser_icon", 8, 8),
        LRJ_SENSOR_ICON = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_sensor_icon.png", "lrj_sensor_icon", 8, 8),
        LRJ_ROCK_16 = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_rock_16.png", "lrj_rock_16", 16, 16),
        LRJ_ROCK_08 = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_rock_08.png", "lrj_rock_08",  8,  8),
        LRJ_ROCK_04 = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_rock_04.png", "lrj_rock_04",  4,  4);
    public static final Sprite.Atlas
        LRJ_PLAY_BUTTON = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_play_button.png", "lrj_play_button", 40, 12),
        LRJ_QUIT_BUTTON = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_quit_button.png", "lrj_quit_button", 40, 12),
        LRJ_MENU_BUTTON = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_menu_button.png", "lrj_menu_button", 40, 12),
        LRJ_GAME_OVER   = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_game_over.png", "lrj_game_over", 32, 20),
        LRJ_NUMBER      = Sprite.Atlas.load("lrj.game.sprites.Sprites:lrj_number.png", "lrj_number", 8, 8);
}
