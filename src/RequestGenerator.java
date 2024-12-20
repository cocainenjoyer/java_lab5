public class RequestGenerator {
    private static ElevatorController controller;

    public static void setController(ElevatorController elevatorController) {
        controller = elevatorController;
    }

    public static void generateRandomRequest() {
        if (controller != null) {
            int floor = (int) (Math.random() * controller.getMaxFloor()) + 1;
            int targetFloor = (int) (Math.random() * controller.getMaxFloor()) + 1;
            Request request = new Request(floor, targetFloor);
            controller.assignRequest(request);
        }
    }
}
