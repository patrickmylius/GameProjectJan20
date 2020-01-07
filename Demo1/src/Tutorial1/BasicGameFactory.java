package Tutorial1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import static com.almasb.fxgl.dsl.FXGL.*;

public class BasicGameFactory implements EntityFactory {

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


}
