package PokeBounce.pokebounce;

import PokeBounce.EntityType;
import PokeBounce.control.PokeBounceGameMenu;
import PokeBounce.control.PokeBounceMainMenu;
import com.almasb.fxgl.app.*;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
/***/
//TODO ::::: REMEMBER PIXEL MOVEMENT CHANGE, WHEN SWAPPING FROM STATION TO LAPTOP :::::

/**
 * MAJOR TODOS
 */


/**
 * MINOR TODOES - FOR THE TIME AFTER PROJECT MONTH.....
 */
//TODO - Design new game map
//TODO - REFRACTER TO NEW PROGRAM FILE, CREATE ACCEPTABLE STRUCTURE..

    //TODO - @@@@ALPHATEST@@@@@

    //TODO - Change game theme music, theme imageBackground, player design, enemy design.
    //TODO - EvilPuff slow buff, turn down ENEMY pixel movement to 50% for x amount of time
    //TODO - APPLICATION increase pixel size
    //TODO - Countdown shown, on buffs and respawn
    //TODO - Power buff, overwrites respawn
    //TODO - Game diffs, easy, normal, hard
    //TODO - Implement different difficuties, easy, normal, hard.





public class BasicGameApp extends GameApplication {

    /**
     * Creates window 600x600, setTitle to Basic Game App, Version 0.1
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("PokeBounce - Zealand");
        gameSettings.setAppIcon("EvilPuffIcon.png");
        gameSettings.setVersion("1.0");
        gameSettings.setMenuEnabled(true);


        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new PokeBounceMainMenu(MenuType.MAIN_MENU);
            }

            public FXGLMenu newGameMenu() {
                return new PokeBounceGameMenu(MenuType.GAME_MENU);
            }


        });
    }

    /**
     * Creates variables
     */
    private boolean leftWallTouched;
    private boolean rightWallTouched;
    private boolean topWallTouched;
    private boolean bottomWallTouched;
    private boolean hasPowerUp;
    private boolean safeRespawn;

    private Entity evilPuff;
    private Entity leftWall;
    private Entity rightWall;
    private Entity coin;
    private Entity powerUp;
    private Entity player;
    private Entity backGroundImg;

    private int playerLives;
    public int totalScore;
    private String playerName;
    public static Music music;
    public static Music menuMusic;

    private String log = "************************************\nPokeBounce High Scores \n************************************";


    /**
     * initialize game method, spawns evilPuffs, launch spawn sound.
     */
    @Override
    protected void initGame() {
        getAudioPlayer().stopMusic(menuMusic);
        music = getAssetLoader().loadMusic("Pokemon Red, Yellow, Blue Battle Music- Trainer.mp3");
        getAudioPlayer().playMusic(music);
        getGameWorld().addEntityFactory(new BasicGameFactory());
        Sound evilPuffEntrySound = getAssetLoader().loadSound("NewEvilPuffEntry.wav");
        Sound coinEntrySound = getAssetLoader().loadSound("NewCoinEntry.wav");
        Sound powerUpEntrySound = getAssetLoader().loadSound("PowerUpSpawn.wav");

        playerLives = 3;
        safeRespawn = false;
        hasPowerUp = false;
        leftWallTouched = false;
        rightWallTouched = false;
        topWallTouched = false;
        bottomWallTouched = false;


        /**FOR STATION 57600x1080 pixels*/
        /** Spawns new EvilPuff every 6 seconds */
        evilPuff = getGameWorld().spawn("EvilPuff", getAppHeight() / (Math.random() * 10) + (1),
                getAppWidth() / -(Math.random() * 200) + (1));
        TimerAction timerAction = getGameTimer().runAtInterval(() ->
        {
            evilPuff = getGameWorld().spawn("EvilPuff", getAppHeight() / (Math.random() * 10) + (1),
                    getAppWidth() / (Math.random() * 200) + (1));
            getAudioPlayer().playSound(evilPuffEntrySound);
        }, Duration.seconds(6));
        timerAction.resume();

        /**FOR laptop 1920x1080 pixels*/
        /**evilPuff = getGameWorld().spawn("EvilPuff", getAppHeight() / (Math.random() * 50) + (1),
         getAppWidth() / -(Math.random() * 200) + (0.75));
         TimerAction timerAction = getGameTimer().runAtInterval(() ->
         {
         evilPuff = getGameWorld().spawn("EvilPuff", getAppHeight() / (Math.random() * 50) + (1),
         getAppWidth() / (Math.random() * 200) + (0.75));
         getAudioPlayer().playSound(evilPuffEntrySound);
         }, Duration.seconds(6));
         timerAction.resume(); */


        /** Spawns new coin every 8 second*/
        TimerAction timerAction1 = getGameTimer().runAtInterval(() -> {

            coin = getGameWorld().spawn("Coin");
            FXGL.getAudioPlayer().playSound(coinEntrySound);
        }, Duration.seconds(8));
        timerAction1.resume();

        /** Spawns powerup every 35 second */
        TimerAction timerAction2 = getGameTimer().runAtInterval(() -> {

            powerUp = getGameWorld().spawn("PowerUp");
            FXGL.getAudioPlayer().playSound(powerUpEntrySound);
        }, Duration.seconds(32));
        timerAction2.resume();


        /** Create new Entity (Player) */

        player = FXGL.spawn("Player", new Point2D(300, 300));

        /** Creates frame repeating 1-3 on background and creates view*/
        getGameScene().setBackgroundRepeat("backGroundGif.gif");


        /** adds RIGHTWALL as entity*/
        leftWall = entityBuilder()
                .type(EntityType.LEFTWALL)
                .at(-10, 0)
                .bbox(new HitBox(BoundingShape.box(10, 600)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();

        /** adds RIGHTWALL as entity*/
        rightWall = entityBuilder()
                .type(EntityType.RIGHTWALL)
                .at(610, 0)
                .bbox(new HitBox(BoundingShape.box(-10, 600)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();

        /** adds TOPWALL as entity */
        entityBuilder()
                .type(EntityType.TOPWALL)
                .at(0, -10)
                .bbox(new HitBox(BoundingShape.box(600, 10)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();
        /** Implements BOTTOMWALL as entity */
        entityBuilder()
                .type(EntityType.BOTTOMWALL)
                .at(0, 610)
                .bbox(new HitBox(BoundingShape.box(600, -10)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();


    }


    /**
     * initializing physics, adding collisionHandler, Player, Coin, Enemy, PowerUp
     */
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new PowerUpHandler());
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                onCoinPickup();
                coin.removeFromWorld();

            }

        });
/** Handles Collisions with Player and Evil puff and if player is powered up, including saveRespawn*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity playerEaten, Entity evilPuff) {
                if (!hasPowerUp && !safeRespawn) {
                    Sound playerSwallowed = getAssetLoader().loadSound("playerEaten.wav");
                    getAudioPlayer().playSound(playerSwallowed);

                    player.removeFromWorld();
                    onPlayerDeath();

                    if (playerLives > 0) {
                        runOnce(() -> {
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        }, Duration.seconds(2));

                    }
                }
                if (hasPowerUp) {
                    Sound evilPuffEaten = getAssetLoader().loadSound("evilPuffEaten.wav");
                    getAudioPlayer().playSound(evilPuffEaten);

                    evilPuff.removeFromWorld();
                    getGameState().increment("score", +500);
                    /** adds 500 points to totalScore for the log, every time player eats EvilPuff */
                    totalScore = totalScore + 500;
                }
            }


        });

        /** Adds unitCollision to left wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.LEFTWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                leftWallTouched = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                leftWallTouched = false;
            }
        });

        /** Adds unitCollision to right wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.RIGHTWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                rightWallTouched = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                rightWallTouched = false;
            }
        });

        /** Adds unitCollision to top wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.TOPWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                topWallTouched = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                topWallTouched = false;
            }
        });

        /** Adds unitCollision to bottom wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOTTOMWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                bottomWallTouched = true;
            }

            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                bottomWallTouched = false;
            }
        });
    }

    /**
     * Runs theme song, loops until game is closed
     */
    @Override
    protected void onPreInit() {
        BasicGameApp.menuMusic = getAssetLoader().loadMusic("MainMenuMusic.mp3");
        getAudioPlayer().playMusic(BasicGameApp.menuMusic);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (rightWallTouched) //If player unit collides with right wall,"Move Right" function stops until false.
                    return;

                //player.translateX(3); //Move right, 3 pixels, for 5760x1080
                player.translateX(5); //Move right 5 pixels for 1920x1080
                //getGameState().increment("pixelsMoved", +3); Tracks pixels moved right
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (leftWallTouched) //If player unit collides with left wall,"Move Left" function stops until false.
                    return;

                //player.translateX(-3); //move left -3 pixels. for 5760x1080
                player.translateX(-5); //move left -5 pixels, for 1920x1080
                //getGameState().increment("pixelsMoved", +3); Tracks pixels moved left
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (topWallTouched) //If player unit collides with top wall,"Move Up" function stops until false.
                    return;

                //player.translateY(-3); //move -3 pixels up, for 5760x1080
                player.translateY(-5); //move up -5 pixels, for 1920x1080
                //getGameState().increment("pixelsMoved", +3); tracks pixels moved up
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (bottomWallTouched) //If player unit collides with bottom wall,"Move Down" function stops until false.
                    return;

                //player.translateY(3); //move 3 pixels down, for 5760x1080
                player.translateY(5); //move down 5 pixels, for 1920x1080
                //getGameState().increment("pixelsMoved", +3); Tracks pixels moved down
            }
        }, KeyCode.S);
    }

    @Override
    protected void onUpdate(double trf) {
        if (playerLives == 0) {

            Sound gameOver = getAssetLoader().loadSound("GameOver.wav");
            getAudioPlayer().playSound(gameOver);
            getAudioPlayer().stopMusic(music);

            getDisplay().showInputBoxWithCancel("Write the name to be added to the highscore.", p -> !p.isEmpty(), (String) -> {
                setPlayerName(String);
                if (String.isEmpty()) {
                    System.out.println("Score not saved.");
                    totalScore = 0;

                } else {
                    String playerLog = "\nPlayer " + playerName + ": your score ended as: ";
                    System.out.println(log + totalScore);
                    log = log + playerLog + totalScore + "\n************************************";
                    saveToFile(totalScore);
                }
                getGameController().gotoMainMenu();
            });


/** When player is game over, log saves score to "totalScore.txt", first logging method used */
            //String logmessage = "\nPlayer unknown: your score ended as: ";
            //System.out.println(logmessage + totalScore);
            //log = log + logmessage + totalScore + "\n************************************";
            //saveToFile(totalScore);



        /** if player is Game Over, resets playerLives, and takes him to main menu, first GameOver Method */
        //if (playerLives == 0) {
        //  getGameController().gotoMainMenu();
        // playerLives++;
        // playerLives++;
        //playerLives++;

        //
    }
        if(hasPowerUp) {
        player.getViewComponent().clearChildren();
        player.getViewComponent().addChild(FXGL.texture("playerBuffed.png"));
    }
        if(safeRespawn &&!hasPowerUp) {
        player.getViewComponent().clearChildren();
        player.getViewComponent().addChild(FXGL.texture("respawnPlayer.gif"));
    }
        if(!safeRespawn &&!hasPowerUp) {
        player.getViewComponent().clearChildren();
        player.getViewComponent().addChild(FXGL.texture("PokePlayerUnit1.png"));

    }


}


    @Override
    protected void initUI() {
        /** pixels moved positioning in UI*/
        //Text textPixels = new Text();
        //textPixels.setTranslateX(25); // x = 50
        //textPixels.setTranslateY(25); // y = 100

        /** Tracks pixels moved */
        //getGameScene().addUINode(textPixels); // adds initUi to scene sa
        //textPixels.textProperty().bind(getGameState().intProperty("pixelsMoved").asString()); // pixels is moved variable added to Scene as textfield

        /** Score positioning in UI*/
        Text textScore = new Text();
        textScore.setTranslateX(75);
        textScore.setTranslateY(25);
        textScore.setFill(Color.BLACK);
        Font font = new Font(18);
        textScore.setFont(font);

        /** Tracks Score*/
        getGameScene().addUINode(textScore);
        textScore.textProperty().bind(getGameState().intProperty("score").asString());
        addText("Score:", 10, 25).setFill(Color.GOLD);


        /** Player lives positioning in UI*/
        Text textGameTimer = new Text();
        textGameTimer.setTranslateX(550);
        textGameTimer.setTranslateY(25);
        textGameTimer.setFill(Color.BLACK);
        Font f = new Font(18);
        textGameTimer.setFont(f);

        /** Tracks amount of player lives and */
        getGameScene().addUINode(textGameTimer);
        textGameTimer.textProperty().bind(getGameState().intProperty("lives").asString());
        addText("Lives:", 490, 25).setFill(Color.BLUE);


    }

    /**
     * Coin point method, increases score by 250 points.
     */
    public void onCoinPickup() {
        Sound coinPickedUp = getAssetLoader().loadSound("CoinPickedUp.wav");
        getAudioPlayer().playSound(coinPickedUp);
        getGameState().increment("score", +250);
        /** adds 250 points to totalScore for the log, every time player picks up coin */
        totalScore = totalScore + 250;

    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Player death method, decreases lives by 1, if player collides with Evil Puff
     */
    public void onPlayerDeath() {

        getGameState().increment("lives", -1);
        playerLives--;

    }

    public void playerPowerUp() {

        hasPowerUp = true;

        Sound poweredUp = getAssetLoader().loadSound("PoweredUp.wav");
        getAudioPlayer().playSound(poweredUp);

        FXGL.set("poweredUp", true);


    }

    public void playerPowerOff() {

        hasPowerUp = false;

        Sound poweredUp = getAssetLoader().loadSound("PoweredUp.wav");
        getAudioPlayer().stopSound(poweredUp);

        FXGL.set("poweredUp", false);


    }


    /****
     * Map Strings to Scene
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("lives", 3);

        vars.put("poweredUp", false);
        vars.put("safeRespawn", false);


    }


    private void respawn() {
        safeRespawn = true;

        player = spawn("Player", 300, 300);


        //player.getViewComponent().clearChildren();
        //player.getViewComponent().addChild(FXGL.texture("respawnPlayer.gif"));

        Sound respawn = getAssetLoader().loadSound("Respawn.wav");
        getAudioPlayer().playSound(respawn);

        FXGL.runOnce(() -> {
            safeRespawn = false;
            /** Sets respawn timer, where player is not able to collide with evilPuffs for the duration*/
        }, Duration.seconds(4.5));


    }

    /**
     * saves players score to file method
     */
    public void saveToFile(int totalScore) {

        try {
            File file = new File("Demo1/src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); //LAPTOP.
            //File file = new File("src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); //STATION.
            if (file.exists()) {
                if (PokeBounceMainMenu.getHighScoreMap().containsKey(playerName)) {
                    if (PokeBounceMainMenu.getHighScoreMap().get(playerName) > totalScore) {
                        System.out.println("Old high score not beaten, score not saved");
                    }
                    else {
                        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                        String text = "\nPlayer " + playerName + ": your score ended as: " + totalScore + "\n************************************";
                        output.append(text);
                        output.close();
                    }
                }
                else {
                    BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                    String text = "\nPlayer " + playerName + ": your score ended as: " + totalScore + "\n************************************";
                    output.append(text);
                    output.close();
                }
            }
            else {
                PrintWriter output = new PrintWriter(file);
                output.print(log);
                output.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, Highscore save was failed, try again! ");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** First saveToFileMethod*/

        //try {
        // File file = new File("src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); /** FOR STATION */
        // File file = new File("Demo1/src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); /** FOR LAPTOP */
        //PrintWriter output = new PrintWriter(file);
        //output.print(log);
        //output.close();
        //File file = new File("src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt");
        // File file = new File("Demo1/src/PokeBounce/pokebounce/HighScoreLog/TotalScore.txt"); /** FOR LAPTOP */
        //System.out.println("Your score: " + totalScore);

        ////  if (file.exists() == false) {
               // System.out.println("Sorry, we had to make a new logFile.");
              //  file.createNewFile();
               // PrintWriter out = new PrintWriter(new FileWriter(file, true));
               // out.append(logHeader);
              //  out.close();
          //  }
          //  PrintWriter out = new PrintWriter(new FileWriter(file, true));
         //   out.append("\n" + "\n******* " + "Player Unknown " + "score: " + totalScore + " ******** " + "\n");
          //  out.close();
      //  } catch (IOException e) {
       ////  }


        //catch (FileNotFoundException e) {
        //System.out.println("Sorry, Highscore save was failed, try again! ");
        // e.printStackTrace();
        //}
   ////
        }


    public static void main(String[] args) {
        launch(args);
    }

}