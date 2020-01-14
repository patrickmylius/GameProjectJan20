package PokeBounce.pokebounce;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class EvilPuffComponent extends Component {

    public boolean isPoweredUp() {
        return poweredUp;
    }

    public void setPoweredUp(boolean poweredUp) {
        this.poweredUp = poweredUp;
    }

    private boolean poweredUp;


    public void onUpdate(double trf) {
        Point2D velocity = entity.getObject("velocity");
        entity.translate(velocity);

        if (FXGL.getb("poweredUp")) {
            entity.getViewComponent().clearChildren();
            entity.getViewComponent().addChild(FXGL.texture("scaredEvilPuff.png"));
        } else {
            entity.getViewComponent().clearChildren();
            entity.getViewComponent().addChild(FXGL.texture("EvilPuff1.png"));
        }

        if (entity.getX() <= 0
                && entity.getBottomY() <= 600) {
            entity.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (entity.getRightX() >= 600
                && entity.getBottomY() <= 600) {
            entity.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (entity.getY() <= 0) {
            entity.setY(0);
            entity.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

        if (entity.getBottomY() >= 600) {
            entity.setY(600 - 55);
            entity.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));

        }



    }


}
