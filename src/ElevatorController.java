import java.util.*;
import java.util.concurrent.*;

public class ElevatorController {
    private final List<Elevator> elevators = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final int maxFloor;
    private final int maxRequests;
    private final int delay;
    private int generatedRequests = 0;
    private long totalWaitingTime = 0;
    private int completedRequests = 0;

    public ElevatorController(int elevatorCount, int maxFloor, int maxRequests, int delay) {
        this.maxFloor = maxFloor;
        this.maxRequests = maxRequests;
        this.delay = delay;
        ElevatorGUI gui = new ElevatorGUI(elevatorCount);
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator(i + 1, gui, this));
        }
    }

    public void start() {
        for (Elevator elevator : elevators) {
            executorService.execute(elevator);
        }
        generateRequests();
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public synchronized void recordWaitingTime(long waitingTime) {
        totalWaitingTime += waitingTime;
        completedRequests++;
    }

    public synchronized void printFinalStatistics() {
        System.out.println("Average waiting time: " + (totalWaitingTime / (double) completedRequests) + " ms");
        int totalDistance = elevators.stream().mapToInt(Elevator::getTotalDistance).sum();
        System.out.println("Total distance traveled by all elevators: " + totalDistance + " floors");
    }

    public void assignRequest(Request request) {
        Elevator bestElevator = elevators.stream()
                .filter(e -> e.isMovingTowards(request.getFloor()))
                .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - request.getFloor())))
                .orElseGet(() -> elevators.stream()
                        .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - request.getFloor())))
                        .orElse(null));

        if (bestElevator != null) {
            bestElevator.addRequest(request);
        }
    }

    private void generateRequests() {
        new Thread(() -> {
            Random random = new Random();
            while (generatedRequests < maxRequests) {
                try {
                    Thread.sleep(delay);
                    int floor = random.nextInt(maxFloor) + 1;
                    int targetFloor = random.nextInt(maxFloor) + 1;
                    Request request = new Request(floor, targetFloor);
                    assignRequest(request);
                    generatedRequests++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("All requests have been generated.");
        }).start();
    }
}
