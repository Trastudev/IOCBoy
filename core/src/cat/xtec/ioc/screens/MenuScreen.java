package cat.xtec.ioc.screens;

import static cat.xtec.ioc.helpers.AssetManager.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import cat.xtec.ioc.IocBoyGame;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

// TODO PART 5 - Creem pantalla d'inici mostrant la puntuació més alta
public class MenuScreen implements Screen {

    private Stage stage;
    private Batch batch;
    TextButton startButton;
    // TODO PART 5 - Declarem el GlyphLayout de topScore per mostrar per pantalla i la variable topScore per guardar el valor TopScore
    GlyphLayout topScoreLayout;
    private int topScore;
    private float boyRunTime;
    private int boyPosition;
    // TODO PART 5 - Declarem les Preferences
    private Preferences preferences;
    private IocBoyGame game;

    /* TODO playerName - Afegir nova funcionalitat al joc: introduir player name per fer un petit leaderboard
    private Skin skin;
    private TextField playerNameInput;
    GlyphLayout playerNameLayout;
     */



    public MenuScreen(Viewport prevViewport, IocBoyGame game) {

        this.game=game;
        topScore = 0;
        stage = new Stage(prevViewport);
        batch = stage.getBatch();
        boyRunTime=0;
        boyPosition=0-Settings.BOY_WIDTH;

        //Iniciem la música
        AssetManager.menuMusic.play();

        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));

        // TODO PART 5 - Inicialitzem les Preferences i guardem el valor de "TopScore" en una variable per mostrarla en un GlyphLayout
        // Preferències i Label amb el Top Score
        preferences = Gdx.app.getPreferences("TopScore");
        topScore = preferences.getInteger("TopScore",0);
        topScoreLayout = new GlyphLayout();
        topScoreLayout.setText(font, "Top Score: "+topScore);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

        // Creem el menú: botó Start
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        startButton = new TextButton("Start Game", textButtonStyle);
        startButton.setPosition((Settings.GAME_WIDTH / 2) - startButton.getWidth() / 2,
                (Settings.GAME_HEIGHT / 2) - startButton.getHeight() / 2 - 10);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AssetManager.menuMusic.stop();
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
                dispose();
            }
        });

        // Creem el contenidor necessari per aplicar-li les accions
        Container container = new Container(startButton);
        container.setTransform(true);
        container.center();
        container.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 3);
        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        stage.addActor(container);

        stage.addActor(container);

        /* TODO playerName - Afegir nova funcionalitat al joc: introduir player name per fer un petit leaderboard
        playerNameLayout = new GlyphLayout();
        playerNameLayout.setText(AssetManager.font, "Player name: ");

        playerNameInput = new TextField("", AssetManager.skin);
        playerNameInput.setMessageText("PlayerName");
        playerNameInput.setAlignment(Align.center);
        playerNameInput.setWidth(200f);
        playerNameInput.setCursorPosition(Cursor.TEXT_CURSOR);
        playerNameInput.setSize(200f, playerNameInput.getStyle().font.getLineHeight() + 10f);

        // Creem el contenidor necessari per aplicar-li les accions
        Container container = new Container(playerNameInput);
        container.setTransform(true);
        container.center();
        container.setPosition(playerNameLayout.width+60, Settings.GAME_HEIGHT / 2-5);
        // Afegim les accions de escalar: primer es fa gran i després torna a l'estat original ininterrompudament
        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(0.8f, 0.8f, 0.8f), Actions.scaleTo(1f, 1f, 0.8f))));
        stage.addActor(container);
        */
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);

        batch.begin();
        font.draw(batch, topScoreLayout, (Settings.GAME_WIDTH / 2) - topScoreLayout.width / 2, (((Settings.GAME_HEIGHT / 2) - topScoreLayout.height / 2)+ topScoreLayout.height));
        batch.draw((TextureRegion) AssetManager.boyRunAnim.getKeyFrame(boyRunTime, true), boyPosition,Settings.GAME_HEIGHT/2 + topScoreLayout.height +20,Settings.BOY_WIDTH, Settings.BOY_HEIGHT);

        /* TODO playerName - Afegir nova funcionalitat al joc: introduir player name per fer un petit leaderboard
        font.draw(batch, playerNameLayout, 10, Settings.GAME_HEIGHT/2-10);
        */
        batch.end();

        // TODO PART 1 - Movem personatge principal per l'eix de les X

        if (boyPosition>Settings.GAME_WIDTH){
            boyPosition = 0 - Settings.BOY_WIDTH;
        } else {
            boyPosition++;
        }
        boyRunTime+=delta;

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

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

}
