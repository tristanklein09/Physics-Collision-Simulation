//TODO: Add card layout so that JPanels display properly

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Renderer extends JFrame{
    private static Renderer instance;

    //Card Layout
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    //Main Menu
    public JPanel mainMenuCard = new JPanel();
    public JLabel titleLabel = new JLabel("Collision Simulator", SwingConstants.CENTER);
    public JButton enterButton = new JButton("Enter Simulation");

    //Scenario1
    public JPanel scenario1Card = new JPanel();
    public JLabel scenario1Label = new JLabel("Scenario 1 running");

    Renderer() {
        instance = this;

        //Frame setup
        setTitle("Collision Simulator");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(cardPanel); //Card Layout lives on one container

        //Main menu card
        mainMenuCard.setLayout(new GridLayout(2, 1, 10, 10));
        mainMenuCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainMenuCard.add(titleLabel);
        mainMenuCard.add(enterButton);

        //Scenario 1 card
        scenario1Card.setLayout(new BorderLayout());
        scenario1Card.add(scenario1Label, BorderLayout.NORTH);

        //Add cards
        cardPanel.add(mainMenuCard, "MAIN_MENU");
        cardPanel.add(scenario1Card, "SCENARIO1");

        //Show initial cards
        cardLayout.show(cardPanel, "MAIN_MENU"); //show the container (cardPanel), the name is a reference to mainMenuCard
        setVisible(true);
    }

    public static Renderer getInstance() {
        return instance;
    }

    public void loadSandbox1() {
        PhysicsEngine physicsEngine = PhysicsEngine.getInstance();
        cardLayout.show(cardPanel, "SCENARIO1"); //Switches to the other card panel
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Renderer renderer = new Renderer();
            PhysicsEngine physicsEngine = PhysicsEngine.getInstance();

            //Event for the button
            renderer.enterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Takes us to the place where collisions happen
                    renderer.loadSandbox1();
                }
            });

            //Updates the scene at the deltaTime interval
            Timer timer = new Timer((int) (16), e ->{ //TODO: MAKE IT UPDATE AT BASED ON DELTA TIME
               renderer.repaint();
            });
            timer.start();;
        });
    }
}
