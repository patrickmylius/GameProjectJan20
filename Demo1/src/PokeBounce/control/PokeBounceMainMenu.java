package PokeBounce.control;

import PokeBounce.pokebounce.BasicGameApp;
import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PokeBounceMainMenu extends FXGLMenu {
    public PokeBounceMainMenu(MenuType type) {
        super(MenuType.MAIN_MENU);

        var button = new PokeBounceButton("Start New Game", () -> this.fireNewGame());
        button.setTranslateX(FXGL.getAppWidth() / 2 - 300 / 2);
        button.setTranslateY(FXGL.getAppHeight() / 4 - 40 / 2);

        var hsButton = new PokeBounceButton("View Highscores", () -> {
            try {
                showScores();
            } catch (FileNotFoundException e) {
                 e.printStackTrace();
            }
        });
        hsButton.setTranslateX(FXGL.getAppWidth() / 2 - 300 / 2);
        hsButton.setTranslateY(FXGL.getAppWidth() / 3 - 40 / 2);

        var exitGameButton = new PokeBounceButton("Exit Game", () -> this.fireExit()); // Make button show highScores
        exitGameButton.setTranslateX(FXGL.getAppWidth() / 2 - 300 / 2);
        exitGameButton.setTranslateY(FXGL.getAppHeight() / 1.5 - 40 / 2);

        getMenuContentRoot().getChildren().addAll(button, exitGameButton,hsButton);


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
        return new ImageView(FXGL.image("MainMenuGif.gif"));
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

    protected void showScores() throws FileNotFoundException {
        LinkedHashMap<String, Integer> reverseSorted = getHighScoreMap();

        int position = 1;

        VBox vBox = new VBox();
        vBox.setSpacing(5);

        for (Map.Entry<String, Integer> entry : reverseSorted.entrySet()) {
            BorderPane borderPane = new BorderPane();


            Label label1 = new Label("  " + position + ". " + entry.getKey());
            borderPane.setLeft(label1);

            Label label2 = new Label(entry.getValue() + "  ");
            borderPane.setRight(label2);

            position++;
            vBox.getChildren().addAll(borderPane);
            vBox.setStyle("-fx-text-fill: black;-fx-font-size: 20; -fx-font-style: italic; -fx-font-weight: bold; -fx-padding: 0 0 20 0; " +
                    "-fx-background-color: rgba(0,200,100, 0.25);");
                    // SETS TEXT SIZE; style and weight and BACKGROND
        }
        Stage stage = new Stage();
        stage.setTitle("PokeBounce - Highscores");
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(vBox);
        stage.getIcons().add(new Image("assets/textures/EvilPuff1.png"));
        stage.setScene(scene);
        stage.show();

    }
    public static LinkedHashMap<String, Integer> getHighScoreMap() throws FileNotFoundException {
        File file = new File("Demo1/src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); //LAPTOP
        //File file = new File("src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); // STATION

        ArrayList<String> players = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        Map<String, Integer> list = new HashMap<>();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            scanner.next();
            if (scanner.hasNext("Player")) {
                scanner.next();
                players.add(scanner.next().replace(":", ""));
                scanner.next();
                scanner.next();
                scanner.next();
                scanner.next();
                scores.add(scanner.nextInt());
            }
        }

        for (int i = 0; i < players.size(); i++) {
            list.put(players.get(i), scores.get(i));
        }

        LinkedHashMap<String, Integer> reverseSorted = new LinkedHashMap<>();
        list.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSorted.put(x.getKey(), x.getValue()));

        return reverseSorted;
    }
    private static class PokeBounceButton extends StackPane {
        public PokeBounceButton(String name, Runnable action) {
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



