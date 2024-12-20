public class Request {
    private final int floor;
    private final int targetFloor;
    private final long requestTime;

    public Request(int floor, int targetFloor) {
        this.floor = floor;
        this.targetFloor = targetFloor;
        this.requestTime = System.currentTimeMillis();
    }

    public int getFloor() {
        return floor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public long getRequestTime() {
        return requestTime;
    }
}
