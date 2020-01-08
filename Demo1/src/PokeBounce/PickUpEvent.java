package PokeBounce;

import javafx.event.Event;
import javafx.event.EventType;

import javax.swing.text.html.parser.Entity;

public class PickUpEvent extends Event {

    public static final EventType<PickUpEvent> ANY
            = new EventType<>(Event.ANY, "PICKUP_EVENT");

    public static final EventType<PickUpEvent> COIN
            = new EventType<>(ANY, "COIN_PICKUP");

    public static final EventType<PickUpEvent> POWERUP
            = new EventType<>(ANY, "POWER_PICKUP");

    private Entity pickup;

    public PickUpEvent(EventType<? extends Event> eventType, Entity pickup) {
        super(eventType);
        this.pickup = pickup;
    }

    public Entity getPickup() {
        return pickup;
    }



}
