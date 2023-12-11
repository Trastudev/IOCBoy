package cat.xtec.ioc.helpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import cat.xtec.ioc.objects.LightningBall;
import cat.xtec.ioc.objects.IocBoy;
import cat.xtec.ioc.screens.GameScreen;

public class InputHandler implements InputProcessor {

    // Enter per a la gestió del moviment d'arrastrar
    int previousY = 0;

    // Objectes necessaris
    private IocBoy iocBoy;
    private GameScreen screen;
    private Vector2 stageCoord;
    private ArrayList<LightningBall> lightningBalls = new ArrayList<LightningBall>();
    private Stage stage;


    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        iocBoy = screen.getSpacecraft();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {
            case READY:
                // Si fem clic comencem el joc
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                break;
            case RUNNING:
                previousY = screenY;
                stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                if (actorHit != null) {
                    // TODO PART 4 - Si l'estat és RUNNING i fem clic al botó de PAUSE passem l'estat a PAUSE
                    if (actorHit.getName().equals("pauseButton")){
                        screen.setCurrentState(GameScreen.GameState.PAUSE);
                    // TODO PART 2 - Si l'estat és RUNNING i fem clic al botó de disparar disparem una lightningball
                    } else if (actorHit.getName().equals("shootButton")){
                        iocBoy.shootLightningBalls();
                    }
                }
                break;
            // Si l'estat és GameOver tornem a iniciar el joc
            case GAMEOVER:
                screen.reset();
                break;
            // TODO PART 4 - Si l'estat es PAUSE i fem clic tornem a l'estat RUNNING
            case PAUSE:
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                AssetManager.music.setVolume(0.5f);
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // Quan deixem anar el dit acabem un moviment
        // i posem la nau en l'estat normal
        iocBoy.goStraight();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        // Posem un umbral per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousY - screenY) > 2)

            // Si la Y és major que la que tenim
            // guardada és que va cap avall
            if (previousY < screenY) {
                iocBoy.goDown();
            } else {
                // En cas contrari cap a dalt
                iocBoy.goUp();
            }
        // Guardem la posició de la Y
        previousY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
