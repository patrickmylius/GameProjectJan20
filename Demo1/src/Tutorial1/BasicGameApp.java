package Tutorial1;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Map;


//TODO - Background image
//TODO - Spawn new Evil puff Entry sound ("NewBallEntry.wav")
//TODO - Spawn more COIN and change point system, by amount of COIN picked up
//TODO - Implement Speed boost
//TODO - Implement Evil Puff Killer buff
//TODO - Implement GAME OVER if player collides with Evil Puff

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
     * Creates object "player"
     */
    private Entity player;
    private boolean leftWallTouched;
    private boolean rightWallTouched;
    private boolean topWallTouched;
    private boolean bottomWallTouched;
    private Entity evilPuff;
    private Entity leftWall;
    private Entity rightWall;

    /**
     * creates circle, with the radius of 20 in Black
     */
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BasicGameFactory());

        evilPuff = FXGL.getGameWorld().spawn("EvilPuff", 100, 500);
        //evilPuff.se

        /** Create new Entity (Player) */
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                .viewWithBBox("PokePlayerUnit1.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();

        /** Create new Entity (Coin) */
        FXGL.entityBuilder()
                .type(EntityType.COIN)
                .at(350, 200)
                .viewWithBBox(new Circle(15, Color.GOLD))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        /** Create new Entity (Evil puff)
         Entity enemy = FXGL.entityBuilder()
         .type(EntityType.ENEMY)
         .at(0,0)
         .viewWithBBox("EvilPuff.png")
         .with(new CollidableComponent(true))
         .buildAndAttach(); */

        //TODO - Make Evil puff spawn every 30 seconds and move random by bouncing walls...


        /** adds RIGHTWALL as entity*/
        leftWall = FXGL.entityBuilder()
                .type(EntityType.LEFTWALL)
                .at(-10, 0)
                .bbox(new HitBox(BoundingShape.box(10, 600)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();

        /** adds RIGHTWALL as entity*/
        rightWall = FXGL.entityBuilder()
                .type(EntityType.RIGHTWALL)
                .at(610, 0)
                .bbox(new HitBox(BoundingShape.box(-10, 600)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();

        /** adds TOPWALL as entity */
        FXGL.entityBuilder()
                .type(EntityType.TOPWALL)
                .at(0, -10)
                .bbox(new HitBox(BoundingShape.box(600, 10)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();
        /** Implements BOTTOMWALL as entity */
        FXGL.entityBuilder()
                .type(EntityType.BOTTOMWALL)
                .at(0, 610)
                .bbox(new HitBox(BoundingShape.box(600, -10)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();


    }

    /**public Entity newEnemy(double x, double y) {
     Entity enemy = FXGL.entityBuilder()
     .type(EntityType.ENEMY)
     .at(0,0)
     .viewWithBBox("EvilPuff.png")
     .with(new CollidableComponent(true))
     .buildAndAttach();

     if (mode == GameMode.SP || mode == GameMode.MP_HOST) {
     PhysicsComponent enemyPhysics = new PhysicsComponent();
     enemyPhysics.setBodyType(BodyType.DYNAMIC);

     FixtureDef def = new FixtureDef().density(0.3f).restitution(1.0f);

     enemyPhysics.setFixtureDef(def);
     enemyPhysics.setOnPhysicsInitialized(() -> enemyPhysics.setLinearVelocity(5 * 60, -5 * 60));

     enemy.addComponent(enemyPhysics);
     enemy.addComponent(new CollidableComponent(true));
     enemy.addControl(new EnemyControl());


     }
     }*/

    /**
     * Creating the unitCollision handler, runs if player and coin collides
     */
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                coin.removeFromWorld();
            }

        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity evilPuff) {
                player.removeFromWorld();
            }

        });

        /** Adds unitCollision to left wall and player unit*/
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.LEFTWALL) {
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
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.RIGHTWALL) {
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
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.TOPWALL) {
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
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOTTOMWALL) {
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

    @Override
    protected void onPreInit() {
        FXGL.loopBGM("Pokemon Red, Yellow, Blue Battle Music- Trainer.mp3");
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (rightWallTouched) //If player unit collides with right wall,"Move Right" function stops until false.
                    return;

                player.translateX(5); //Move right, 5 pixels
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (leftWallTouched) //If player unit collides with left wall,"Move Left" function stops until false.
                    return;

                player.translateX(-5); //move left 5 pixels
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (topWallTouched) //If player unit collides with top wall,"Move Up" function stops until false.
                    return;

                player.translateY(-5); //move 5 pixels up
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (bottomWallTouched) //If player unit collides with bottom wall,"Move Down" function stops until false.
                    return;

                player.translateY(5); //move 5 pixels down
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.S);
    }

    @Override
    protected void onUpdate(double trf) {
        Point2D velocity = evilPuff.getObject("velocity");
        evilPuff.translate(velocity);

        if (evilPuff.getX() == leftWall.getRightX()
                && evilPuff.getY() < leftWall.getBottomY()
                && evilPuff.getBottomY() > leftWall.getY()) {
            evilPuff.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (evilPuff.getRightX() == rightWall.getX()
                && evilPuff.getY() < rightWall.getBottomY()
                && evilPuff.getBottomY() > rightWall.getY()) {
            evilPuff.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (evilPuff.getY() <= 0) {
            evilPuff.setY(0);
            evilPuff.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

        if (evilPuff.getBottomY() >= 600) {
            evilPuff.setY(600 - 55);
            evilPuff.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));

        }



    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(300); // x = 50
        textPixels.setTranslateY(10); // y = 100

        FXGL.getGameScene().addUINode(textPixels); // adds initUi to scene
        textPixels.textProperty().bind(FXGL.getGameState().intProperty("pixelsMoved").asString()); // pixels is moved variable added to Scene as textfield
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}