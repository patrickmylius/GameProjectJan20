package PokeBounce.pokebounce;

import PokeBounce.EntityType;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.gameplay.GameState;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.collision.Collision;
import com.almasb.fxgl.time.TimerAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


//TODO - Background image
//TODO - Implement Killer puff Powerup
//TODO - Implement GAME OVER + RESTART GAME if player collides with Evil Puff and has no lifes left
//TODO - IMPLEMENT GAME RESET METHOD
//TODO - IMPLEMENT GAMEOVER METHOD
//TODO - COIN SPAWN SOUND
//TODO - COIN DESIGN, implemented
//TODO - Player dead sound
//TODO - Player dead DESIGN

public class BasicGameApp extends GameApplication {

    /**
     * Creates window 600x600, setTitle to Basic Game App, Version 0.1
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Basic Game app");
        gameSettings.setVersion("0.1");
    }

    /**
     * Creates variables
     */
    private Entity player;
    private boolean leftWallTouched;
    private boolean rightWallTouched;
    private boolean topWallTouched;
    private boolean bottomWallTouched;
    private Entity evilPuff;
    private Entity leftWall;
    private Entity rightWall;
    private Entity coin;
    private int playerLives = 3;


    /**
     * initialize game method, spawns evilPuffs, launch spawn sound.
     */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BasicGameFactory());
        Sound evilPuffEntrySound = getAssetLoader().loadSound("NewEvilPuffEntry.wav");
        //Sound coinEntrySound = getAssetLoader().loadSound("")

        /** Spawns new EvilPuff every 20 seconds */
        evilPuff = getGameWorld().spawn("EvilPuff", getAppHeight() / (Math.random() * 50) + (1),
                getAppWidth() / (Math.random() * 50) + (1));
        TimerAction timerAction = getGameTimer().runAtInterval(() ->
        {
            evilPuff = getGameWorld().spawn("EvilPuff", getAppHeight() / (Math.random() * 50) + (1),
                    getAppWidth() / (Math.random() * 50) + (1));
            getAudioPlayer().playSound(evilPuffEntrySound);
        }, Duration.seconds(10));
        timerAction.resume();

        /** Spawns new coin every 15 second*/
        coin = getGameWorld().spawn("Coin", getAppHeight() / (Math.random() * 50) + (1),
                getAppWidth() / (Math.random() * 50) + (1));
        TimerAction timerAction1 = getGameTimer().runAtInterval(() -> {

            coin = getGameWorld().spawn("Coin", getAppHeight() / (Math.random() * 50 ) + (1),
                    getAppWidth() / (Math.random() * 50) + (1));
            //FXGL.getAudioPlayer().playSound();
        }, Duration.seconds(10));
        timerAction1.resume();



        /* EventBus bus = getEventBus();

         EventHandler<PickUpEvent> handler = event -> {
         if (player.isColliding(coin))
         };

         getEventBus().addEventHandler(PickUpEvent.ANY, handler); */
        /** Create new Entity (Player) */

        player = FXGL.spawn("Player", new Point2D(300, 300));


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
     * initializing physics, adding collisionHandler, Player, Coin, Enemy
     */
    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                onCoinPickup();
                coin.removeFromWorld();

            }

        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity playerEaten, Entity evilPuff) {
                player.removeFromWorld();
                onPlayerDeath();
                if (playerLives > 0) {
                    runOnce(() -> {
                        respawn();
                    }, Duration.seconds(2));

                }
                //getDisplay().showBox("Game over", getGameController();
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
        loopBGM("Pokemon Red, Yellow, Blue Battle Music- Trainer.mp3");
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (rightWallTouched) //If player unit collides with right wall,"Move Right" function stops until false.
                    return;

                player.translateX(3); //Move right, 5 pixels
                getGameState().increment("pixelsMoved", +3);
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (leftWallTouched) //If player unit collides with left wall,"Move Left" function stops until false.
                    return;

                player.translateX(-3); //move left 5 pixels
                getGameState().increment("pixelsMoved", +3);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (topWallTouched) //If player unit collides with top wall,"Move Up" function stops until false.
                    return;

                player.translateY(-3); //move 5 pixels up
                getGameState().increment("pixelsMoved", +3);
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (bottomWallTouched) //If player unit collides with bottom wall,"Move Down" function stops until false.
                    return;

                player.translateY(3); //move 5 pixels down
                getGameState().increment("pixelsMoved", +3);
            }
        }, KeyCode.S);
    }

    @Override
    protected void onUpdate(double trf) {
        if (playerLives == 0)
            getDisplay().showMessageBox("Game over");
     }


    @Override
    protected void initUI() {
        /** pixels moved positioning in UI*/
        Text textPixels = new Text();
        textPixels.setTranslateX(25); // x = 50
        textPixels.setTranslateY(25); // y = 100

        /** Tracks pixels moved */
        getGameScene().addUINode(textPixels); // adds initUi to scene sa
        textPixels.textProperty().bind(getGameState().intProperty("pixelsMoved").asString()); // pixels is moved variable added to Scene as textfield

        /** Score positioning in UI*/
        Text textScore = new Text();
        textScore.setTranslateX(550);
        textScore.setTranslateY(25);

        /** Tracks Score*/
        getGameScene().addUINode(textScore);
        textScore.textProperty().bind(getGameState().intProperty("score").asString());
        addText("Score: ", 475, 25 ).setFill(Color.GREEN);

        /** Player lives positioning in UI*/
        Text textGameTimer = new Text();
        textGameTimer.setTranslateX(300);
        textGameTimer.setTranslateY(25);

        getGameScene().addUINode(textGameTimer);
        textGameTimer.textProperty().bind(getGameState().intProperty("lives").asString());


    }

    /**
     * Coin point method, increases score by 250 points.
     */
    public void onCoinPickup() {

        if (player.isColliding(coin)) {

            getGameState().increment("score", +250);
        }

    }

    /**
     * Player death method, decreases lives by 1, if player collides with Evil Puff
     */
    public void onPlayerDeath() {
        if (player.isColliding(evilPuff)) {
            getGameState().increment("lives", -1);
        }
        if (player.isColliding(evilPuff)) {
            playerLives--;
        }
    }


    /****
     * Map Strings to Scene
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("score", 0);
        vars.put("lives", 3);

    }


    /*
     * private boolean requestNewGame = false;
     * <p>
     * private void gameOver() { if (playerLives == 0) {
     * getDisplay().showMessageBox("Demo Over, Press OK to exit", this::gameOver);
     * }
     * }
     */

  /*  private void reSpawn() {
        if (player.isColliding(evilPuff) && playerLives > 0) {

            player = entityBuilder()
                    .type(EntityType.PLAYER)
                    .at(300, 300)
                    .viewWithBBox("PokePlayerUnit1.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
        }
    }
*/

    private void respawn() {
        player = spawn("Player", 300, 300);
    }
    public static void main(String[] args) {
        launch(args);
    }

}