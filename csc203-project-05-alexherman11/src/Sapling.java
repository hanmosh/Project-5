import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant{
    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    private int healthLimit;

    public Sapling(String id, Point position, List<PImage> images, int health) {
        super(id, position, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health, images);
        this.healthLimit = SAPLING_HEALTH_LIMIT;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        health++;
        if (!transform(scheduler, imageStore, world)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        }
    }

    public boolean transform(EventScheduler scheduler, ImageStore imageStore, WorldModel worldModel) {
        if (health <= 0) {
            Stump stump = new Stump(Stump.STUMP_KEY + "_" + getId(), getPosition(), imageStore.getImageList(Stump.STUMP_KEY));
            worldModel.removeEntity(scheduler, this);
            stump.addEntity(worldModel);
            return true;

        } else if (health >= healthLimit) {
            Tree tree = new Tree(WorldModel.TREE_KEY + "_" + getId(), getPosition(), Functions.getNumFromRange(WorldModel.TREE_ACTION_MAX, WorldModel.TREE_ACTION_MIN), Functions.getNumFromRange(WorldModel.TREE_ANIMATION_MAX, WorldModel.TREE_ANIMATION_MIN), Functions.getIntFromRange(WorldModel.TREE_HEALTH_MAX, WorldModel.TREE_HEALTH_MIN), imageStore.getImageList(WorldModel.TREE_KEY));
            worldModel.removeEntity(scheduler, this);
            tree.addEntity(worldModel);
            tree.scheduleActions(worldModel, imageStore, scheduler);
            return true;
        }
        return false;
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        eventScheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        eventScheduler.scheduleEvent(this, EventScheduler.createAnimationAction(this, 0), getAnimationPeriod());
    }

}
