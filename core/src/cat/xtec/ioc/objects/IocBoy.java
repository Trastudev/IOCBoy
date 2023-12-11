package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

// TODO PART 1 - Nou personatge principal
public class IocBoy extends Actor {

    // Distintes posicions de l'IOCBoy, recte, pujant i baixant
    public static final int IOCBOY_STRAIGHT = 0;
    public static final int IOCBOY_UP = 1;
    public static final int IOCBOY_DOWN = 2;

    // Paràmetres de l'IOCBoy
    private Vector2 position;
    private int width, height;
    private int direction;

    private float boyRunTime;

    private Rectangle collisionRect;

    private ScrollHandler scrollHandler;



    public IocBoy(ScrollHandler scrollHandler, float x, float y, int width, int height) {

        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        this.scrollHandler = scrollHandler;

        // Inicialitzem l'IOCBoy a l'estat normal
        direction = IOCBOY_STRAIGHT;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        // Per a la gestió de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);


    }


    // TODO PART 2 - El personatge principal pot disparar lightningballs
    // Disparem lightningballs
    public void shootLightningBalls(){
        LightningBall lightningBall = new LightningBall(position.x+getWidth(), position.y+getHeight()/2, 50, 20, Settings.BOY_VELOCITY *2);
        scrollHandler.newLightningBall(lightningBall);
    }

    public void act(float delta) {
        super.act(delta);

        // Movem l'IOCBoy depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case IOCBOY_UP:
                if (this.position.y - Settings.BOY_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.BOY_VELOCITY * delta;
                }
                break;
            case IOCBOY_DOWN:
                if (this.position.y + height + Settings.BOY_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.BOY_VELOCITY * delta;
                }
                break;
            case IOCBOY_STRAIGHT:
                break;
        }

        collisionRect.set(position.x, position.y, width, height);
        setBounds(position.x, position.y, width, height);

    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // Canviem la direcció de l'IOCBoy: Puja
    public void goUp() {
        direction = IOCBOY_UP;
    }

    // Canviem la direcció de l'IOCBoy: Baixa
    public void goDown() {
        direction = IOCBOY_DOWN;
    }

    // Posem l'IOCBoy' al seu estat original
    public void goStraight() {
        direction = IOCBOY_STRAIGHT;
    }

    public void reset() {

        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.BOY_STARTX;
        position.y = Settings.BOY_STARTY;
        direction = IOCBOY_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        boyRunTime += Gdx.graphics.getDeltaTime();
        batch.draw((TextureRegion) AssetManager.boyRunAnim.getKeyFrame(boyRunTime, true), position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
