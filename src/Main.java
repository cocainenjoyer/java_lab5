import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Диалог для ввода параметров
        int maxRequests = 10; // Значение по умолчанию
        int delay = 1000;     // Значение по умолчанию

        try {
            String maxRequestsInput = JOptionPane.showInputDialog(null,
                    "Enter the maximum number of requests:",
                    "Elevator System Configuration",
                    JOptionPane.QUESTION_MESSAGE);
            if (maxRequestsInput != null) {
                maxRequests = Integer.parseInt(maxRequestsInput);
            }

            String delayInput = JOptionPane.showInputDialog(null,
                    "Enter the delay between requests (in milliseconds):",
                    "Elevator System Configuration",
                    JOptionPane.QUESTION_MESSAGE);
            if (delayInput != null) {
                delay = Integer.parseInt(delayInput);
            }

            if (maxRequests <= 0 || delay <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Both values must be positive integers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input. Please enter positive integers only.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Создание и запуск контроллера
        ElevatorController controller = new ElevatorController(2, 10, maxRequests, delay);
        RequestGenerator.setController(controller);
        controller.start();

        // Вывод статистики при завершении программы
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nFinal Statistics:");
            controller.printFinalStatistics();
        }));
    }
}
