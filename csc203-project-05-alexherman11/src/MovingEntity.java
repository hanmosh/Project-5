import processing.core.PImage;

import java.util.List;

public abstract class MovingEntity extends AnimatedEntity {

    AStarPathingStrategy astar;
    public MovingEntity (String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images);
        astar = new AStarPathingStrategy();
    }
    public abstract boolean moveTo(Entity target, EventScheduler scheduler, WorldModel world);
}
