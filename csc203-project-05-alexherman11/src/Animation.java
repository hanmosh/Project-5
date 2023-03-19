public class Animation extends Action {
    public Animation(Entity entity, int repeatCount) {
        super(entity, repeatCount);
    }

    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();
        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity, EventScheduler.createAnimationAction(entity, Math.max(repeatCount - 1, 0)), entity.getAnimationPeriod());
        }
    }
}
