import java.util.*;

/**
 * Keeps track of events that have been scheduled.
 */
public final class EventScheduler {
    public PriorityQueue<Event> eventQueue;
    public Map<Entity, List<Event>> pendingEvents;
    public double currentTime;

    public EventScheduler() {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.currentTime = 0;
    }

    public void updateOnTime(double time) {
        double stopTime = this.currentTime + time;
        while (!this.eventQueue.isEmpty() && this.eventQueue.peek().time <= stopTime) {
            Event next = this.eventQueue.poll();
            removePendingEvent(next);
            this.currentTime = next.time;
            next.action.executeAction(this);
        }
        this.currentTime = stopTime;
    }

    private void removePendingEvent(Event event) {
        List<Event> pending = this.pendingEvents.get(event.entity);

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void unscheduleAllEvents(Entity entity) {
        List<Event> pending = pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                eventQueue.remove(event);
            }
        }
    }

    public void scheduleEvent(Entity entity, Action action, double afterPeriod) {
        double time = currentTime + afterPeriod;

        Event event = new Event(action, time, entity);

        eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = pendingEvents.getOrDefault(entity, new LinkedList<>());
        pending.add(event);
        pendingEvents.put(entity, pending);
    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }
}
