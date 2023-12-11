package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class Fireball extends Scrollable {

    private Rectangle collisionRect;

    private float fireballRunTime;

    Random r;

    int assetFireball;
    private float explodedTime = 0;
    private boolean exploded = false;

    public Fireball(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        // Creem el cercle
        collisionRect = new Rectangle();

        /* Accions */
        r = new Random();
        assetFireball = r.nextInt(5);

        setOrigin();

        // Rotacio
        //RotateByAction rotateAction = new RotateByAction();
        //rotateAction.setAmount(-90f);
        //rotateAction.setDuration(0.2f);

        // Accio de repetició
        //RepeatAction repeat = new RepeatAction();
        //repeat.setAction(rotateAction);
        //repeat.setCount(RepeatAction.FOREVER);

        // Equivalent:
        // this.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.rotateBy(-90f, 0.2f)));

        //this.addAction(repeat);

        fireballRunTime = 0;

    }

    public void setOrigin() {
        this.setOrigin(width/2, height/2);
    }

    public void explodes(){
        collisionRect = null;
        explodedTime = Gdx.graphics.getDeltaTime();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (explodedTime==0) {
            collisionRect.set(position.x, position.y, width, height);
        }
    }

    public void reset(float newX) {
        super.reset(newX);
        // Obtenim un número al·leatori entre MIN i MAX
        float newSize = Methods.randomFloat(Settings.MIN_FIREBALL, Settings.MAX_FIREBALL);

        float originalWidth = 60f;
        float originalHeight = 25f;

        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = originalWidth * newSize;
        height = originalHeight * newSize;

        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        assetFireball = r.nextInt(5);
        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        fireballRunTime += Gdx.graphics.getDeltaTime();
        if (explodedTime > 0 && !exploded){
            if (AssetManager.explosionAnim.isAnimationFinished(explodedTime)){
                exploded=true;
                this.remove();
            }
            explodedTime+=Gdx.graphics.getDeltaTime();
            batch.draw((TextureRegion)AssetManager.explosionAnim.getKeyFrame(explodedTime),position.x, position.y-(position.y/2), this.getOriginX(), this.getOriginY(), 64, 64, this.getScaleX(), this.getScaleY(), this.getRotation());
        } else {
            batch.draw((TextureRegion) AssetManager.fireballAnim.getKeyFrame(fireballRunTime, true), position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
        }
    }

    // Retorna true si hi ha col·lisió
    public boolean collides(IocBoy nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            //Comprovem si han col·lisionat sempre i quan l'asteroid estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionRect, nau.getCollisionRect()));
        }
        return false;
    }

    public boolean collides(LightningBall lightningBall) {

        if (position.x <= lightningBall.getX() + lightningBall.getWidth()) {
            // Comprovem si han col·lisionat sempre i quan l'asteroid estigui a la mateixa alçada que el laser
            return (Intersector.overlaps(collisionRect, lightningBall.getCollisionRect()));
        }
        return false;
    }




}
