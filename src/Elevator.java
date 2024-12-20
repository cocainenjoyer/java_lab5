import java.util.*;

public class Elevator implements Runnable {
    private final int id;
    private int currentFloor = 1;
    private final PriorityQueue<Request> requests = new PriorityQueue<>(Comparator.comparingInt(r -> Math.abs(r.getFloor() - currentFloor)));
    private boolean movingUp = true;
    private final ElevatorGUI gui;
    private int totalDistance = 0;

    private final ElevatorController controller;

    public Elevator(int id, ElevatorGUI gui, ElevatorController controller) {
        this.id = id;
        this.gui = gui;
        this.controller = controller;
    }

    public synchronized void addRequest(Request request) {
        requests.offer(request);
        gui.logRequest("Elevator " + id + ": New request - from floor " + request.getFloor() + " to floor " + request.getTargetFloor());
        notifyAll();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (requests.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            processRequest();
        }
    }

    private void processRequest() {
        Request request;
        synchronized (this) {
            request = requests.poll();
        }
        if (request != null) {
            long waitingTime = System.currentTimeMillis() - request.getRequestTime();
            controller.recordWaitingTime(waitingTime);

            gui.logRequest("Elevator " + id + ": Processing request - from floor " + request.getFloor() + " to floor " + request.getTargetFloor());
            moveTo(request.getFloor());
            moveTo(request.getTargetFloor());
            gui.logRequest("Elevator " + id + ": Completed request - to floor " + request.getTargetFloor());
        }
    }

    private void moveTo(int targetFloor) {
        totalDistance += Math.abs(currentFloor - targetFloor);
        gui.updateElevatorStatus(id, "Moving from " + currentFloor + " to " + targetFloor);
        try {
            Thread.sleep(Math.abs(targetFloor - currentFloor) * 500); // Симуляция движения
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentFloor = targetFloor;
        gui.updateElevatorStatus(id, "Reached " + currentFloor);
    }

    public boolean isMovingTowards(int floor) {
        if (movingUp && floor >= currentFloor) return true;
        if (!movingUp && floor <= currentFloor) return true;
        return false;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
