package cat.xtec.ioc.utils;

public class Settings {

    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = 240;
    public static final int GAME_HEIGHT = 135;

    // Propietats de la nau
    public static final float BOY_VELOCITY = 75;
    public static final int BOY_WIDTH = 15;
    public static final int BOY_HEIGHT = 30;
    public static final float BOY_STARTX = 20;
    public static final float BOY_STARTY = GAME_HEIGHT/2 - BOY_HEIGHT /2;

    public static final int BUTTON_WIDTH = 25;
    public static final int BUTTON_HEIGHT = 25;

    // Rang de valors per canviar la mida de l'asteroide.
    public static final float MAX_FIREBALL = 1f;
    public static final float MIN_FIREBALL = 0.75f;

    // Configuració Scrollable
    public static final int FIREBALL_SPEED = -100;
    public static final int FIREBALL_GAP = 75;
    public static final int FIREBALL_WIDTH = 50;
    public static final int FIREBALL_HEIGHT = 21;
    public static final int BG_SPEED = -100;

    public static final int SCORE_COIN = 10;
    public static final int SCORE_TREASURE = 100;

    public static final int COIN_SPEED = -75;

    public static final int TREAUSURE_SPEED = -125;



}
