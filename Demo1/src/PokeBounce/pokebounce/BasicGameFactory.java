package PokeBounce.pokebounce;

import PokeBounce.EntityType;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BasicGameFactory implements EntityFactory {

    @Spawns("Player")
    public Entity Player(SpawnData data) {

        return entityBuilder()
                .type(EntityType.PLAYER)
                .from(data)
                .viewWithBBox("PokePlayerUnit1.png")
                .with(new CollidableComponent(true))
                .build();
    }

    /**
     * Spawns and create, evilPuff
     */
    @Spawns("EvilPuff")
    public Entity EvilPuff(SpawnData data) {

        return entityBuilder()

                .type(EntityType.ENEMY)
                .from(data)
                .viewWithBBox("EvilPuff1.png")
                .with(new CollidableComponent(true))
                /**Sets speed on ENEMY For Laptop 1920x1080*/
                .with("velocity", new Point2D((Math.random() * 1) + 3, (Math.random() * 1) + 3))
                /**Sets speed on ENEMY For Station 5760x1080*/
                //.with("velocity", new Point2D((Math.random() * 1) + 1.50, (Math.random() * 1) + 1.50))
                .with(new EvilPuffComponent())
                .build();

    }

    @Spawns("Coin")
    public Entity newCoin(SpawnData data) {

        return entityBuilder()

                .from(data)
                .type(EntityType.COIN)
                .at((Math.random() * 600) + (1),
                        (Math.random() * 600) + (1))
                .viewWithBBox("Coin.png")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }

    //Building power up
   @Spawns("PowerUp")
    public Entity newPowerUp(SpawnData data) {

        return entityBuilder()

                .from(data)
                .type(EntityType.POWERUP)
                .at( (Math.random() * 600) + (1),
                        (Math.random() * 600) + (1))
                .viewWithBBox("PowerUp.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();

    }
}
