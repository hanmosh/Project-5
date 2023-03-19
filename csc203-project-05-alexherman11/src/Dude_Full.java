import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;


public class Dude_Full extends Dude {
    public Dude_Full(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, resourceLimit, images);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(House.class)));
        if (fullTarget.isPresent() && moveTo(fullTarget.get(), scheduler, world)) {
            transform(scheduler, imageStore, world);
        } else {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        }
    }

    public void transform(EventScheduler scheduler, ImageStore imageStore, WorldModel worldModel) {
        Dude_Not_Full dude = new Dude_Not_Full(getId(), getPosition(), actionPeriod, animationPeriod, resourceLimit, getImages());

        worldModel.removeEntity(scheduler, this);

        dude.addEntity(worldModel);
        dude.scheduleActions(worldModel, imageStore, scheduler);
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        eventScheduler.scheduleEvent(this, EventScheduler.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public boolean moveTo(Entity target, EventScheduler scheduler, WorldModel world) {
        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && ((world.getOccupancyCell(p) == null) || (world.getOccupancyCell(p) instanceof Stump));
        BiPredicate<Point, Point> withinReach = (pos, goal) -> Functions.adjacent(pos, goal);
        List<Point> path = astar.computePath(getPosition(), target.getPosition(), canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        /*
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
         */
        if (withinReach.test(position, target.getPosition())) {
            return true;
        }
        if (path.size() == 0) {
            return false;
        }
        world.moveEntity(scheduler, this, path.get(0));
        return false;
    }
}
