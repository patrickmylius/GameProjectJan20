package PokeBounce.control;
import PokeBounce.pokebounce.BasicGameApp;
import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PokeBounceGameMenu extends FXGLMenu {
    public PokeBounceGameMenu(MenuType type) {
        super(MenuType.GAME_MENU);

        var button = new GameMenuButton("Continue Game", () -> this.fireResume());
        button.setTranslateX(FXGL.getAppWidth() / 2 - 300 / 2);
        button.setTranslateY(FXGL.getAppHeight() / 4 - 40 / 2);

        var rsButton = new GameMenuButton("Restart Game", () -> this.fireNewGame());
        rsButton.setTranslateX(FXGL.getAppWidth() / 2 - 300 / 2);
        rsButton.setTranslateY(FXGL.getAppWidth() / 1.75 - 40 / 2);

        var mmButton = new GameMenuButton("Main Menu", () ->
        { FXGL.getDisplay().showConfirmationBox("Are you sure?", yes -> {
            if (yes) {
                FXGL.getGameController().gotoMainMenu();
                FXGL.getAudioPlayer().stopMusic(BasicGameApp.music);
            }
        });

        }); // Make button show highScores

        mmButton.setTranslateX(FXGL.getAppWidth() / 2 - 300 / 2);
        mmButton.setTranslateY(FXGL.getAppHeight() / 1.5 - 40 / 2);

        getMenuContentRoot().getChildren().addAll(button, rsButton, mmButton);


    }

    @Override
    protected Button createActionButton(String name, Runnable action) {
        return new Button(name);
    }

    @Override
    protected Button createActionButton(StringBinding name, Runnable action) {
        return new Button();
    }

    @Override
    protected Node createBackground(double width, double height) {
        return new ImageView(FXGL.image("GameMenuEvilPuff3.png"));
    }

    @Override
    protected Node createTitleView(String title) {
        return new Text(title);
    }

    @Override
    protected Node createVersionView(String version) {
        return new Text(version);
    }

    @Override
    protected Node createProfileView(String profileName) {
        return new Text(profileName);
    }
    private static class GameMenuButton extends StackPane {
        public GameMenuButton(String name, Runnable action) {

            var bg = new Rectangle(300,40, Color.GRAY);
            bg.setStroke(Color.BLACK);

            var text = FXGL.getUIFactory().newText(name, Color.WHITESMOKE, 22);

            bg.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITESMOKE)
            );

            setOnMouseClicked(e -> action.run());

            getChildren().addAll(bg, text);

            var exitBg = new Rectangle(300, 40, Color.GRAY);
            exitBg.setStroke(Color.BLACK);

            var exitText = FXGL.getUIFactory().newText(name, Color.WHITESMOKE, 22);

            exitBg.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );

            exitText.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITESMOKE)
            );

            var hsBg = new Rectangle(300, 40, Color.GRAY);
            hsBg.setStroke(Color.BLACK);

            var hsText = FXGL.getUIFactory().newText(name, Color.WHITESMOKE, 22);

            hsBg.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );

            hsText.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITESMOKE)
            );


        }

    }
}
