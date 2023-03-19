public interface Moving {
    boolean moveTo(Entity target, EventScheduler scheduler, WorldModel world, SingleStepPathingStrategy ssp);

}
