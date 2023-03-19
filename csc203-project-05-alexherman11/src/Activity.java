public class Activity extends Action{

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore);
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(world, imageStore, scheduler);
    }
}
