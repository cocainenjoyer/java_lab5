import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ElevatorGUI {
    private final JFrame frame;
    private final List<JLabel> elevatorLabels;
    private final JTextArea requestLog;

    public ElevatorGUI(int elevatorCount) {
        frame = new JFrame("Elevator Control System");
        frame.setLayout(new BorderLayout());

        JPanel elevatorPanel = new JPanel(new GridLayout(elevatorCount, 1));
        elevatorLabels = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            JLabel elevatorLabel = new JLabel("Elevator " + (i + 1) + ": Waiting...", SwingConstants.CENTER);
            elevatorLabels.add(elevatorLabel);
            elevatorPanel.add(elevatorLabel);
        }

        requestLog = new JTextArea();
        requestLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(requestLog);

        JButton callButton = new JButton("Generate Request");
        callButton.addActionListener(e -> RequestGenerator.generateRandomRequest());

        frame.add(elevatorPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(callButton, BorderLayout.SOUTH);

        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void updateElevatorStatus(int elevatorId, String status) {
        SwingUtilities.invokeLater(() -> elevatorLabels.get(elevatorId - 1).setText(status));
    }

    public void logRequest(String message) {
        SwingUtilities.invokeLater(() -> {
            requestLog.append(message + "\n");
            requestLog.setCaretPosition(requestLog.getDocument().getLength());
        });
    }
}
