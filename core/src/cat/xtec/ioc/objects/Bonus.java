package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

// TODO PART 3 - Creem classe Bonus
public class Bonus extends Scrollable{

    private Rectangle collisionRect;

    Random r;

    private int model;

    private float littleCoinRunTime;

    private boolean bonusTaken;

    // TODO PART 3 - Constructor amb paràmetre "model" per distingir entre els dos models de bonus
    public Bonus(int model, float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        collisionRect = new Rectangle();
        this.model=model;
        r = new Random();
        setOrigin();
        littleCoinRunTime = 0;
        bonusTaken = false;

    }

    public void setOrigin() {
        this.setOrigin(width/2, height/2);
    }

    @Override
    public void act(float delta){
        super.act(delta);

        // Actualitzem rectangle de col·lisions
        collisionRect.set(position.x, position.y,width,height);
    }

    public void reset(){

        // Col·loquem el bonus a l'amplada de la pantalla més la seva amplada
        float newX = Settings.GAME_WIDTH+width;
        super.reset(newX);

        // Tornem a l'alçada i l'amplada d'origen
        setOrigin();

        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y = new Random().nextInt(Settings.GAME_HEIGHT - (int) height);
        collisionRect = new Rectangle();
        if (r.nextInt(100) < 90){
            model=1;
        } else {
            model=2;
        }
    }

    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        littleCoinRunTime+= Gdx.graphics.getDeltaTime();
        if (this.model == 1) {
            batch.draw((TextureRegion) AssetManager.littleCoinAnim.getKeyFrame(littleCoinRunTime, true), position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
        } else if (this.model ==2){
            batch.draw((TextureRegion) AssetManager.treasureAnim.getKeyFrame(littleCoinRunTime, true), position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
        }
        //drawCollisionRect(batch);
    }

    public boolean collides(IocBoy iocBoy){
        if (position.x <= iocBoy.getX() + iocBoy.getWidth()) {
            //Comprovem si han col·lisionat sempre i quan la fireball estigui a la mateixa alçada que l'IOCBoy'
            return (Intersector.overlaps(collisionRect, iocBoy.getCollisionRect()));
        }
        return false;
    }

    public int getModel(){return model;}

    public boolean isBonusTaken() {
        return bonusTaken;
    }

    public void setBonusTaken(boolean bonusTaken) {
        this.bonusTaken = bonusTaken;
    }


}
