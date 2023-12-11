package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import cat.xtec.ioc.helpers.AssetManager;

// TODO PART 2 - Nova classe lightningball
public class LightningBall extends Scrollable {

    private float lightningBallRunTime=0;
    private Rectangle collisionRect;

    private float velocity;

    public LightningBall(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, 0);
        collisionRect = new Rectangle();
        this.velocity=velocity;
        setOrigin();
    }

    public void setOrigin() {
        this.setOrigin(width/2 + 1, height/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        position.x = position.x + velocity * delta;
        collisionRect.set(position.x, position.y, width, height);
        lightningBallRunTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw((TextureRegion) AssetManager.lighningBallAnim.getKeyFrame(lightningBallRunTime), getX(), getY(), getWidth(), getHeight());
    }


    public Rectangle getCollisionRect() {
        return collisionRect;
    }



}
