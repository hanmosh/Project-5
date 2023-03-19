import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Fairy extends MovingEntity{
    public Fairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        eventScheduler.scheduleEvent(this, EventScheduler.createAnimationAction(this, 0), getAnimationPeriod());
    }

    //remember to change references
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Stump.class)));
        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();
            if (this.moveTo(fairyTarget.get(), scheduler, world)) {
                Sapling sapling = new Sapling(WorldModel.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(WorldModel.SAPLING_KEY), 0);
                sapling.addEntity(world);
                sapling.scheduleActions(world, imageStore, scheduler);
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getPosition();
            }
        }
        return newPos;
    }

    public boolean moveTo(Entity target, EventScheduler scheduler, WorldModel world) {
        Predicate<Point> canPassThrough = p -> world.withinBounds(p) && ((world.getOccupancyCell(p) == null) || (world.getOccupancyCell(p) instanceof Stump));
        BiPredicate<Point, Point> withinReach = (pos, goal) -> Functions.adjacent(pos, goal);
        List<Point> path = astar.computePath(getPosition(), target.getPosition(), canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);

        /*
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
         */
        if (withinReach.test(getPosition(), target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        }
        if (path.size() == 0) {
            return false;
        }
        else {
            world.moveEntity(scheduler, this, path.get(0));
            return false;
        }
    }
}
