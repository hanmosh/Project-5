import processing.core.PImage;

import java.util.List;

public abstract class Plant extends AnimatedEntity{
    int health;
    public Plant(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images);
        this.health = health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
