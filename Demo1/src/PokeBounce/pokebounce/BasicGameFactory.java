package PokeBounce.pokebounce;

import PokeBounce.EntityType;
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
                .with("velocity", new Point2D((Math.random() * 1) + 0.75, (Math.random() * 1) + 0.75))
                .with(new EvilPuffComponent())
                .build();

    }

    @Spawns("Coin")
    public Entity newCoin(SpawnData data) {

        return entityBuilder()

                .from(data)
                .type(EntityType.COIN)
                .at(getAppHeight() / (Math.random() * 10) + (1),
                        getAppWidth() / (Math.random() * 10) + (1))
                .viewWithBBox(new Circle(15, Color.GOLD))
                .with(new CollidableComponent(true))
                .build();
    }


}
