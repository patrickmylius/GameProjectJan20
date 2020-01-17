package PokeBounce.pokebounce;

import PokeBounce.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

public class PowerUpHandler extends CollisionHandler {

    public PowerUpHandler() {
        super(EntityType.POWERUP, EntityType.PLAYER);

    }

    @Override
    protected void onCollisionBegin(Entity powerUp, Entity player) {
        powerUp.removeFromWorld();
        FXGL.<BasicGameApp>getAppCast().playerPowerUp();
        FXGL.runOnce(() -> {
            FXGL.<BasicGameApp>getAppCast().playerPowerOff();
            /** Sets duration of the player PoweredUp buff */
        }, Duration.seconds(6));
    }

}
