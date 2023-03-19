import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Dude_Not_Full extends Dude{

    private int resourceCount;
    public Dude_Not_Full(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, resourceLimit, images);
        this.resourceCount = 0;
    }

    public boolean transform(EventScheduler scheduler, ImageStore imageStore, WorldModel worldModel) {
        if (resourceCount >= resourceLimit) {
            Dude_Full dude = new Dude_Full(getId(), getPosition(), actionPeriod, animationPeriod, resourceLimit, getImages());

            worldModel.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            dude.addEntity(worldModel);
            dude.scheduleActions(worldModel, imageStore, scheduler);

            return true;
        }
        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));
        if (target.isEmpty() || !moveTo(target.get(), scheduler, world) || !transform(scheduler, imageStore, world)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        }
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        eventScheduler.scheduleEvent(this, EventScheduler.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public boolean moveTo(Entity target, EventScheduler scheduler, WorldModel world) {
        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && ((world.getOccupancyCell(p) == null) || (world.getOccupancyCell(p) instanceof Stump));
        BiPredicate<Point, Point> withinReach = (pos, goal) -> Functions.adjacent(pos, goal);
        List<Point> path = astar.computePath(getPosition(), target.getPosition(), canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        if (withinReach.test(position, target.getPosition())) {
            resourceCount += 1;
            target.setHealth(target.getHealth() -1);
            return true;
        }
        if (path.size() != 0) {
            world.moveEntity(scheduler, this, path.get(0));
        }
        return false;
    }
}
