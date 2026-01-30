import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    public JFrame mainMenuFrame = new JFrame();
    public JPanel mainMenuPanel = new JPanel();
    public JLabel titleLabel = new JLabel("Collision Simulator", SwingConstants.CENTER);
    public JButton enterButton = new JButton("Enter Simulation");

    Main() {
        mainMenuFrame.setTitle("Collision Simulator");
        mainMenuFrame.setLayout(new BorderLayout(10,10));
        mainMenuPanel.setLayout(new GridLayout(2, 1, 10, 10));
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainMenuFrame.setSize(600, 600);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setVisible(true);

        mainMenuFrame.add(mainMenuPanel, BorderLayout.CENTER);

        mainMenuPanel.add(titleLabel);
        mainMenuPanel.add(enterButton);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
