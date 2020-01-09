package PokeBounce.pokebounce;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class EvilPuffComponent extends Component {

    public void onUpdate(double trf) {
        Point2D velocity = entity.getObject("velocity");
        entity.translate(velocity);

        if (entity.getX() <= 0
                && entity.getBottomY() <= 600) {
            entity.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }
        if (entity.getRightX() >= 600
                && entity.getBottomY() <= 600) {
            entity.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            System.out.println("test");
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
