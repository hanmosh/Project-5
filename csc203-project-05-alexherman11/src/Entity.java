import processing.core.PImage;

import java.util.List;

public abstract class Entity{
    protected String id;

    protected Point position;
    protected List<PImage> images;
    protected int imageIndex;
    protected int health;

    public Entity(String id, Point position, List<PImage> images) {
        this.setId(id);
        this.setPosition(position);
        this.setImages(images);
        this.imageIndex = 0;
    }

    public double getAnimationPeriod() {
        throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.getClass()));
    }

    public void nextImage() {
        setImageIndex(imageIndex + 1);
    }


    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.getClass()));
    }

    public void scheduleActions(WorldModel world, ImageStore imageStore, EventScheduler eventScheduler) {
        return;
    }

    public void addEntity(WorldModel worldModel) {
        if (worldModel.withinBounds(getPosition())) {
            worldModel.setOccupancyCell(getPosition(), this);
            worldModel.entities.add(this);
        }
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public int getHealth() {
        return 0;
    }

    public void setHealth(int health) {
        return;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public Point getPosition() {
        return position;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}
