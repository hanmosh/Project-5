import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends Entity {
    protected double actionPeriod;
    protected double animationPeriod;

    public AnimatedEntity(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }
}
