import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {
    double animationPeriod;

    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, EventScheduler.createAnimationAction(this, 0), getAnimationPeriod());
    }
}
