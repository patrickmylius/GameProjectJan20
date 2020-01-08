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

    /**
     * Spawns and create, evilPuff
     */
    @Spawns("EvilPuff")
    public Entity EvilPuff(SpawnData data) {
        //PhysicsComponent enemyPhysics = new PhysicsComponent();
        //enemyPhysics.setBodyType(BodyType.DYNAMIC);

        return entityBuilder()

                .type(EntityType.ENEMY)
                .from(data)
                .viewWithBBox("EvilPuff.png")
                .with(new CollidableComponent(true))
                .with("velocity", new Point2D(1, 1))
                .build();


    }

    @Spawns("Coin")
    public Entity newCoin(SpawnData data) {

        return entityBuilder()
                .from(data)
                .type(EntityType.COIN)
                .at(getAppHeight() / (Math.random() * (20 + 1)),
                        getAppWidth() / (Math.random() * (20 + 1)))
                .viewWithBBox(new Circle(15, Color.GOLD))
                .with(new CollidableComponent(true))
                .build();
    }


}
