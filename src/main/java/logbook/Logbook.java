package logbook;

import trip.Road;
import trip.Vehicle;

import java.util.LinkedList;
import java.util.Queue;


/**
 * The logbook: a queue of events in the order they happened. Used by the tests to verify behavior.
 * <p>
 * In part B several drivers write to the logbook at the same time, and a reader can wait for the next event with
 * {@code take()}, so every method must be thread-safe. Keep the {@code LinkedList}: make it safe with
 * {@code synchronized}, {@code wait()} and {@code notify()} or {@code notifyAll()}, not with a class from
 * {@code java.util.concurrent}.
 */
public class Logbook {

    /**
     * The events, oldest first. It must stay a {@code LinkedList}, which is not thread-safe on its own.
     */
    private final Queue<Event> queue = new LinkedList<>();
    private int eventCounter;

    /**
     * Creates an event with the given parameters and adds it to the logbook with
     * an event ID 1 greater than the previous event added to the logbook.
     * The first event added has ID 1.
     * TODO: make this method thread-safe, and wake any thread waiting in {@code take()}.
     *
     * @param vehicle the event's vehicle
     * @param road    the event's road
     * @param type    the event's type
     */
    public void record(Vehicle vehicle, Road road, EventType type) {
        synchronized (this){
            queue.add(new Event(vehicle, road, type, ++eventCounter));
            notifyAll();
        }
    }

    public void record(Vehicle vehicle, EventType type) {
        synchronized (this){
            record(vehicle, null, type);
        }
    }

    public void record(EventType type) {
        synchronized (this){
            record(null, null, type);
        }
    }

    /**
     * Retrieves and removes the head of the logbook, or returns null if the logbook is empty.
     * TODO: make this method thread-safe.
     *
     * @return the head of the logbook, or null if the logbook is empty
     */
    public Event poll() {
        synchronized (this){
            return queue.poll();
        }
    }

    /**
     * Retrieves and removes the head of the logbook, waiting if necessary until an event becomes available.
     *
     * @return the head of the logbook
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public Event take() throws InterruptedException {
        // TODO
        synchronized (this){
            while (queue.isEmpty()){
                wait();
            }
            return queue.poll();
        }
    }

    /**
     * Removes all events from the logbook and resets the ID counter.
     * TODO: make this method thread-safe.
     */
    public void clear() {
        synchronized (this){
            queue.clear();
            eventCounter = 0;
        }
    }

    /**
     * Returns the current event ID in the sequence. Starts at 0 when no events have been added to the logbook.
     * TODO: make this method thread-safe.
     *
     * @return the ID of the most recent event, or 0 if no events have been added
     */
    public int getCurrentEventId() {
        synchronized (this){
            return eventCounter;
        }
    }

    /**
     * Returns the current size of the logbook.
     * TODO: make this method thread-safe.
     *
     * @return the number of events in the logbook
     */
    public int size() {
        synchronized (this){
            return queue.size();
        }
    }
}
