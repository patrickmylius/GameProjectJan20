/**package PokeBounce.pokebounce;


import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import com.almasb.fxgl.entity.component.Component;

import java.awt.*;

public class BackGround extends Component {
    private int speed = 0;

    private AnimatedTexture texture;
    private AnimationChannel backGroundMove, lightMove;

    public BackGround() {
        backGroundMove = new AnimationChannel
                ("BackgroundImage.png", 4, 600, 600, Duration.seconds(1), 1, 1);
        lightMove = new AnimationChannel
                ("BackgroundImage2.png", 4, 600, 600, Duration.seconds(1), 0, 3);

        texture = new AnimatedTexture(backGroundMove);


    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);

    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speed * tpf);

        if (speed < 1) {

            if (texture.getAnimationChannel() == backGroundMove) {
                texture.loopAnimationChannel(lightMove);
            }
            speed = (int) (speed * 0.9);

            if (FXGLMath.abs(speed) < 1) {
                speed = 0;
                texture.loopAnimationChannel(backGroundMove);
            }
        }
    }

    public void move() {
        speed = 0;


    }


} */