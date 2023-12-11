package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Bonus;
import cat.xtec.ioc.objects.Fireball;
import cat.xtec.ioc.objects.IocBoy;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.utils.Settings;



public class GameScreen implements Screen {

    // Els estats del joc

    public enum GameState {

        READY, RUNNING, GAMEOVER, PAUSE

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;

    private Fireball fireball;
    private IocBoy iocBoy;
    private int score;
    private int topScore;
    private ScrollHandler scrollHandler;

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;
    private float boyRunTime = 0;

    // Botons acció pause i shoot
    private Image pauseButton;
    private Image shootButton;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;
    private GlyphLayout pausedLayout;
    private GlyphLayout scoreLayout;
    private GlyphLayout levelMessage;

    private Preferences preferences;



    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        // Creem la nau i la resta d'objectes
        scrollHandler = new ScrollHandler();
        iocBoy = new IocBoy(scrollHandler, Settings.BOY_STARTX, Settings.BOY_STARTY, Settings.BOY_WIDTH, Settings.BOY_HEIGHT);


        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(iocBoy);
        // Donem nom a l'Actor
        iocBoy.setName("iocBoy");

        // TODO PART 4 - Afegim botó de Pausa
        pauseButton = new Image(AssetManager.pauseButton);
        pauseButton.setName("pauseButton");
        pauseButton.setBounds(Settings.GAME_WIDTH - 40, 10, 20,20);
        stage.addActor(pauseButton);

        // Afegim el botó de disparar
        shootButton = new Image(AssetManager.shootButton);
        shootButton.setName("shootButton");
        shootButton.setBounds(Settings.GAME_WIDTH -40,Settings.GAME_HEIGHT-40,20,20);
        stage.addActor(shootButton);


        // Iniciem els GlyphLayout
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Estas preparat?");
        // TODO PART 4 - Creem el GlyphLayout de pausa
        pausedLayout = new GlyphLayout();
        pausedLayout.setText(AssetManager.font, "Pausat");
        scoreLayout = new GlyphLayout();
        scoreLayout.setText(AssetManager.font,"Punts: "+score);
        levelMessage = new GlyphLayout();

        // TODO PART 5 - Donem valor a la variable topScore amb les dades persistents de "TopScore"
        preferences = Gdx.app.getPreferences("TopScore");
        topScore = preferences.getInteger("TopScore");

        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        //Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem l'IOCBoy
        shapeRenderer.rect(iocBoy.getX(), iocBoy.getY(), iocBoy.getWidth(), iocBoy.getHeight());


        // Recollim tots els Asteroid
        ArrayList<Fireball> fireballs = scrollHandler.getFireballs();
        Fireball fireball1;

        for (int i = 0; i < fireballs.size(); i++) {

            fireball1 = fireballs.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.rect(fireball1.getX(), fireball1.getY(), fireball1.getWidth(), fireball1.getHeight());
        }
        shapeRenderer.end();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Dibuixem tots els actors de l'stage
        stage.draw();

        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {

            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                updateReady();
                break;
            // TODO PART 4 - En cas de currentState PAUSE cridem a pause();
            case PAUSE:
                pause();
                break;
        }

        // Per depuracions
        //drawElements();

    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        batch.end();
        score=0;

    }

    private void updateRunning(float delta) {
        stage.act(delta);

        // Col·lisió amb l'IOCBoy' i la fireball
        if (scrollHandler.collides(iocBoy)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("iocBoy").remove();
            textLayout.setText(AssetManager.font, "Game Over :'(");
            currentState = GameState.GAMEOVER;
        }

        // TODO PART 3 - Controlem si hi ha col·lisió amb el bonus i depèn del model afegim uns punts o altres. Apliquem efecte de so.
        Bonus bonus = scrollHandler.getBonus(iocBoy);
        if (scrollHandler.bonusCollides(iocBoy) && !bonus.isBonusTaken()){
            AssetManager.bonusSound.play();
            if (bonus != null){
                if (bonus.getModel() == 1){
                    score += Settings.SCORE_COIN;
                } else if (bonus.getModel() == 2){
                    score += Settings.SCORE_TREASURE;
                }
                bonus.setBonusTaken(true);
                scrollHandler.bonusRemove(bonus);
            }
        }

        pauseButton.setVisible(true);
        shootButton.setVisible(true);

        // TODO PART 3 - Actualització de l'score
        // Actualitzem l'score i el dibuixem a la cantonada superior esquerra
        scoreLayout.setText(AssetManager.font,"Punts: "+score);
        batch.begin();
        AssetManager.font.draw(batch, scoreLayout, 10, 10);
        batch.end();
    }

    private void updateGameOver(float delta) {
        stage.act(delta);

        // TODO PART 5 - Missatge personalitzat quan hem perdut el joc depenent de la puntuació
        // Missatge personalitzat puntuació del joc
        if (score < 100) {
            levelMessage.setText(AssetManager.font,"Has de practicar mes!");
        } else if (score <= 150) {
            levelMessage.setText(AssetManager.font, "Es pot millorar!");
        } else {
            levelMessage.setText(AssetManager.font, "Estas fet un crack!");
        }

        // TODO PART 5 - Si la puntuació de la partida és més gran al "TopScore" la reemplacem i la fem persistent
        if (score > topScore) {
            preferences.putInteger("TopScore", score);
            preferences.flush();
        }

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        AssetManager.font.draw(batch, scoreLayout,(Settings.GAME_WIDTH - scoreLayout.width)/2, (Settings.GAME_HEIGHT + textLayout.height+scoreLayout.height)/2);
        AssetManager.font.draw(batch, levelMessage,(Settings.GAME_WIDTH - levelMessage.width)/2,(Settings.GAME_HEIGHT+scoreLayout.height+textLayout.height)/2+levelMessage.height+5);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw((TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (iocBoy.getX() + iocBoy.getWidth() / 2) - 32, iocBoy.getY() + iocBoy.getHeight() / 2 - 32, 64, 64);
        batch.end();

        pauseButton.setVisible(false);
        shootButton.setVisible(false);

        explosionTime += delta;
    }

    public void reset() {

        // Posem el text d'inici
        textLayout.setText(AssetManager.font, "Are you\nready?");

        // Cridem als restart dels elements.
        iocBoy.reset();
        scrollHandler.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim l'IOCBoy a l'stage
        stage.addActor(iocBoy);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

    }


    @Override
    public void resize(int width, int height) {

    }

    // TODO PART 4 - Funcionalitats de pause: invisibilitzar botons de shoot i pause, baixar volum música i mostrar GlyphLayout Pausa
    @Override
    public void pause() {
        AssetManager.music.setVolume(0.1f);
        pauseButton.setVisible(false);
        shootButton.setVisible(false);
        batch.begin();
        AssetManager.font.draw(batch, pausedLayout, (Settings.GAME_WIDTH / 2) - pausedLayout.width/2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        batch.end();
        if (getCurrentState() == GameState.RUNNING){
            setCurrentState(GameState.PAUSE);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    public IocBoy getSpacecraft() {
        return iocBoy;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }


}
