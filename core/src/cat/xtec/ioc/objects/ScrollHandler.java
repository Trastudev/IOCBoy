package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;


public class ScrollHandler extends Group {


    Settings settings = new Settings();
    // Fons de pantalla
    Background bg, bg_back;

    private float width, height, y;

    // Asteroides
    int numFireballs;
    private ArrayList<Fireball> fireballs;

    private ArrayList<LightningBall> lightningBalls;

    int numBonus = 2;

    private ArrayList<Bonus> bonuses;

    private float timeAsteroid;
    private float timeBonus;

    private Bonus bonus;


    // Objecte Random
    Random r;


    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        // Comencem amb 3 asteroids
        numFireballs = 3;

        // Creem l'ArrayList
        fireballs = new ArrayList<Fireball>();
        // TODO PART 2 - Inicialitzem lightningballs
        lightningBalls = new ArrayList<LightningBall>();
        // TODO PART 3 - Incialitzem bonuses
        bonuses = new ArrayList<Bonus>();

        timeBonus = 0;

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_FIREBALL, Settings.MAX_FIREBALL);

        // Afegim el primer Asteroid a l'Array i al grup
        Fireball fireball = new Fireball(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize * Settings.FIREBALL_WIDTH, newSize * Settings.FIREBALL_HEIGHT, Settings.FIREBALL_SPEED);
        fireballs.add(fireball);
        addActor(fireball);

        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numFireballs; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_FIREBALL, Settings.MAX_FIREBALL);
            // Afegim l'fireball.
            fireball = new Fireball(fireballs.get(fireballs.size() - 1).getTailX() + Settings.FIREBALL_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize),newSize * Settings.FIREBALL_WIDTH, newSize * Settings.FIREBALL_HEIGHT, Settings.FIREBALL_SPEED);
            // Afegim l'asteroide a l'ArrayList
            fireballs.add(fireball);
            // Afegim l'asteroide al grup d'actors
            addActor(fireball);
        }

        newFireball();
        newBonus();

    }

    // TODO PART 2 - Creem noves lightningballs
    public void newLightningBall(LightningBall lightningBall) {
        if (lightningBalls.size() < 3) {
            lightningBalls.add(lightningBall);
            addActor(lightningBall);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }


        for (int i = 0; i < fireballs.size(); i++) {
            Fireball fireball = fireballs.get(i);
            if (fireball.isLeftOfScreen()) {
                fireball.reset(Settings.GAME_WIDTH + Settings.FIREBALL_GAP);
            }
        }

        for (int i = 0; i < bonuses.size(); i++) {
            bonus = bonuses.get(i);
            if (bonus.isLeftOfScreen()) {
                bonus.reset();
            }
        }

        if (timeBonus > 1f && bonuses.size() < numBonus) {
            newBonus();
            timeBonus = 0;
        } else {
            timeBonus +=delta;
        }

        // TODO PART 2 - Si un lightningball surt de la pantalla l'eliminem
        for (Iterator<LightningBall> iterator = lightningBalls.iterator(); iterator.hasNext();) {
            LightningBall lightningBall = iterator.next();
            if (lightningBall.getX() > settings.GAME_WIDTH-(lightningBall.getWidth()/2)) {
                lightningBall.remove();
                iterator.remove();
            }
        }

        // TODO PART 2 – Control de col·lisions entre lightningballs i fireballs
        ArrayList<LightningBall> lightningBallsToRemove = new ArrayList<LightningBall>();

        for(int i = lightningBalls.size()-1; i >= 0; i--){
            LightningBall lightningBall = lightningBalls.get(i);
            ArrayList<Fireball> fireballsToRemove = new ArrayList<Fireball>();
            for (int j = fireballs.size()-1; j >= 0; j--){
                Fireball fireball = fireballs.get(j);
                if(fireball.collides(lightningBall)){
                    fireball.explodes();
                    AssetManager.explosionSound.play();
                    fireballsToRemove.add(fireball);
                    lightningBallsToRemove.add(lightningBall);
                }
            }
            // Eliminar fireballs afectats
            for (Fireball fireballsToRemove1 : fireballsToRemove) {
                fireballs.remove(fireballsToRemove1);
                newFireball();
            }
        }

        // TODO PART 2 - Eliminar lightningballs afectats
        for (LightningBall lightningBallToRemove : lightningBallsToRemove) {
            lightningBallToRemove.remove();
            lightningBalls.remove(lightningBallToRemove);
        }
    }

    // TODO PART 2 – Creem nou Fireball després d'haver sigut destruït per una lightningball
    public void newFireball(){
        float newSize = Methods.randomFloat(Settings.MIN_FIREBALL, Settings.MAX_FIREBALL);

        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = Settings.FIREBALL_WIDTH * newSize;
        height = Settings.FIREBALL_HEIGHT * newSize;
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);
        // Afegim el fireball.
        Fireball fireball = new Fireball(Settings.GAME_WIDTH + Settings.FIREBALL_GAP, y, width, height, Settings.FIREBALL_SPEED);
        // Afegim l'asteroide a l'ArrayList
        fireballs.add(fireball);
        // Afegim l'asteroide al grup d'actors
        addActor(fireball);
    }


    // TODO PART 3 - Creem nou model de bonus de manera aleatòria amb probabilitat: 90% coin, 10% treasure
    public void newBonus(){
        int model;
        // Afegim el bonus a l'Array i al grup

        if (r.nextInt(100)<90){
            model=1;
            bonus = new Bonus(model,Settings.GAME_WIDTH + Settings.FIREBALL_GAP, new Random().nextInt(Settings.GAME_HEIGHT - (int) height), 17, 17, Settings.COIN_SPEED);
        } else {
            model=2;
            bonus = new Bonus(model,Settings.GAME_WIDTH + Settings.FIREBALL_GAP, new Random().nextInt(Settings.GAME_HEIGHT - (int) height), 17, 17, Settings.TREAUSURE_SPEED);
        }
        bonuses.add(bonus);
        addActor(bonus);
    }

    public boolean collides(IocBoy nau) {

        // Comprovem les col·lisions entre cada fireball i l'IOCBoy
        for (Fireball fireball : fireballs) {
            if (fireball.collides(nau)) {
                return true;
            }
        }
        return false;
    }

    // TODO PART 3 - Retornem l'objecte bonus impactat
    public Bonus getBonus(IocBoy iocBoy){
        for (Bonus bonus : bonuses){
            if (bonus.collides(iocBoy)){
                return bonus;
            }
        }
        return null;
    }

    // TODO PART 3 - Eliminem objecte bonus fent l'animació d'escalat
    public void bonusRemove(Bonus bonus){
        timeBonus += Gdx.graphics.getDeltaTime();
            bonus.addAction(Actions.sequence(
                    Actions.scaleTo(3f, 3f, 0.1f),
                    Actions.scaleTo(1.0f,1.0f,0.05f),
                    Actions.fadeOut(0.02f),
                    Actions.run(() -> {
                        bonuses.remove(bonus);
                        bonus.remove();
                    })
            ));
    }

    // TODO PART 3 - Retornem si hi ha col·lisió o no
    public boolean bonusCollides(IocBoy nau){
        // Comprovem les col·lisions entre cada bonus i la nau
        for (Bonus bonus : bonuses) {
            if (bonus.collides(nau)) {
                return true;
            }
        }
        return false;
    }


    public void reset() {

        // TODO Exercici 2 - Eliminem els lightningBalls que quedin
        for (LightningBall lightningBall : lightningBalls){
            lightningBall.remove();
        }
        lightningBalls.clear();

        // Posem el primer asteroid fora de la pantalla per la dreta
        fireballs.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < fireballs.size(); i++) {
            fireballs.get(i).reset(fireballs.get(i - 1).getTailX() + Settings.FIREBALL_GAP);
        }

        // TODO PART 3 - Reiniciem els bonus
        bonuses.get(0).reset(Settings.GAME_WIDTH);
        for (int i = 1; i < bonuses.size(); i++) {
            bonuses.get(i).reset(bonuses.get(i - 1).getTailX() + Settings.FIREBALL_GAP);
        }


    }

    public ArrayList<Fireball> getFireballs() {
        return fireballs;
    }
}