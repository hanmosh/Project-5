import processing.core.PImage;

import java.util.List;

public class Tree extends Plant{

    public Tree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, health, images);
    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */

    public boolean transform(EventScheduler scheduler, ImageStore imageStore, WorldModel worldModel) {
        if (health <= 0) {
            Stump stump = new Stump(Stump.STUMP_KEY + "_" + getId(), getPosition(), imageStore.getImageList(Stump.STUMP_KEY));
            worldModel.removeEntity(scheduler, this);
            stump.addEntity(worldModel);
            return true;
        }
        return false;
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        eventScheduler.scheduleEvent(this, EventScheduler.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!transform(scheduler, imageStore, world)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        }
    }
}
