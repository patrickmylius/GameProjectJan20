package PokeBounce.pokebounce;

import PokeBounce.EntityType;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
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

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.physics.CollisionHandler;
/** Building power up
public class PowerUpHandler extends BasicGameApp {

    public PowerUpHandler() {
        super(EntityType.POWERUP, EntityType.ENEMY);

    }

    @Override
    protected void onCollisionBegin(Entity powerUp, Entity evilPuff) {
        evilPuff.removeFromWorld();
    }
}
*/