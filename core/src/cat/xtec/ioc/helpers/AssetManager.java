package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet, sheetFireBall;

    // IOCBoy i fons de pantalla
    public static TextureRegion iocboy, iocboyDown, iocboyUp, background;

    public static TextureRegion[] boyRun;
    public static Animation boyRunAnim;

    // Fireball
    public static TextureRegion[] fireball;
    public static Animation fireballAnim;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    // Lightningball
    public static TextureRegion[] lightningBall;
    public static Animation lighningBallAnim;

    // Bonus moneda
    public static TextureRegion[] littleCoin;
    public static Animation littleCoinAnim;

    // Bonus tresor
    public static TextureRegion[] treasure;
    public static Animation treasureAnim;

    // Botons de pausa i disparar
    public static TextureRegion pauseButton;
    public static TextureRegion shootButton;

    /* TODO playerName - Afegir nova funcionalitat al joc: introduir player name per fer un petit leaderboard
    public static TextureRegion textFieldBackground;
    public static TextureRegion textFieldCursor;
    */

    // Sons
    public static Sound explosionSound, bonusSound;
    public static Music music, menuMusic;


    // Font
    public static BitmapFont font;



    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        sheet = new Texture(Gdx.files.internal("sheet.png"));
        sheetFireBall = new Texture(Gdx.files.internal("fireball-sheet.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Sprites de iocboy
        iocboy = new TextureRegion(sheet, 0, 0, 28, 50);
        iocboy.flip(false, true);

        iocboyUp = new TextureRegion(sheet, 0, 0, 28, 50);
        iocboyUp.flip(false, true);

        iocboyDown = new TextureRegion(sheet, 0, 0, 28, 50);
        iocboyDown.flip(false, true);

        // TODO PART 1 - Nous sprites amb animació
        //Creem els 9  estats de Run
        boyRun = new TextureRegion[8];

        //Animació Run
        for (int i = 0; i < boyRun.length; i++){
            boyRun[i] = new TextureRegion(sheet, i*28, 0, 28, 50);
            boyRun[i].flip(false,true);
        }

        //Creem l'animació
        boyRunAnim = new Animation(0.1f, boyRun);
        boyRunAnim.setPlayMode(Animation.PlayMode.LOOP);


        // Carreguem els 6 estats de la fireball
        fireball = new TextureRegion[5];
        for (int i = 0; i < fireball.length; i++) {

            fireball[i] = new TextureRegion(sheetFireBall, i * 250, 0, 250, 100);
          //  fireball[i].flip(false, true);

        }

        // Creem l'animació de la fireball i fem que s'executi contínuament
        fireballAnim = new Animation(0.05f, fireball);
        fireballAnim.setPlayMode(Animation.PlayMode.LOOP);


        // Carreguem els 6 estats de la lightningball
        lightningBall = new TextureRegion[5];
        for (int i = 0; i < lightningBall.length; i++) {

            lightningBall[i] = new TextureRegion(sheetFireBall, i * 250, 100, 250, 125);
            lightningBall[i].flip(true, false);

        }

        // Creem l'animació de la lightningball i fem que s'executi contínuament
        lighningBallAnim = new Animation(0.05f, lightningBall);
        lighningBallAnim.setPlayMode(Animation.PlayMode.LOOP);


        // Creem el coin
        littleCoin = new TextureRegion[7];
        for (int i = 0; i < littleCoin.length; i++) {
            littleCoin[i] = new TextureRegion(sheet, i * 26, 540, 26, 26);
            littleCoin[i].flip(false, true);
        }

        // Creem l'animació del coin
        littleCoinAnim = new Animation(0.1f, littleCoin);
        littleCoinAnim.setPlayMode(Animation.PlayMode.LOOP);

        // Creem el treasure
        treasure = new TextureRegion[7];
        for (int i = 0; i < treasure.length; i++) {
            treasure[i] = new TextureRegion(sheet, i * 35, 570, 35, 30);
            treasure[i].flip(false, true);
        }

        // Creem l'animació del treasure
        treasureAnim = new Animation(0.1f, treasure);
        treasureAnim.setPlayMode(Animation.PlayMode.LOOP);

        // Creem els 16 estats de l'explosió
        explosion = new TextureRegion[13];

        // Carreguem els 16 estats de l'explosió
        for (int i = 0; i < explosion.length; i++) {
            explosion[i] = new TextureRegion(sheet, i * 96, 440, 96, 96);
              //  explosion[index-1].flip(false, true);
        }

        // Finalment creem l'animació
        explosionAnim = new Animation(0.04f, explosion);

        // Creem el botó de Pausa
        pauseButton = new TextureRegion(sheet,0,634,80,80);

        // Creem el botó de disparar
        shootButton = new TextureRegion(sheet,5,730,110,110);

        // Fons de pantalla
        background = new TextureRegion(sheet, 0, 50, 495, 390);
        background.flip(false, true);

        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        // Bonus sound
        bonusSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));

        // Música del menú inicial
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/cruising-down-8bit-lane-159615.mp3"));
        music.setVolume(0.5f);
        music.setLooping(true);

        // Música del joc
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/your-game-comedy-173310.mp3"));
        menuMusic.setVolume(0.5f);
        menuMusic.setLooping(true);


        // TODO PART 1  Nova font
        /******************************* Text *************************************/
        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/iocboy.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);





        /* TODO playerName Afegir nova funcionalitat al joc: introduir player name per fer un petit leaderboard
        skin = new Skin();
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;
        skin.add("default", textFieldStyle);
        */
    }



    public static void dispose() {

        // Alliberem els recursos gràfics i de audio
        sheet.dispose();
        bonusSound.dispose();
        explosionSound.dispose();
        music.dispose();


    }
}
