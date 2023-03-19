/**
 * An action that can be taken by an entity
 */
public abstract class Action {

    protected Entity entity;
    protected WorldModel world;
    protected ImageStore imageStore;
    protected int repeatCount;

    public Action(Entity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public Action(Entity entity, WorldModel worldModel, ImageStore imageStore) {
        this.entity = entity;
        this.world = worldModel;
        this.imageStore = imageStore;
    }

    public abstract void executeAction(EventScheduler scheduler);



}
