import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Renderer extends JFrame{
    private static Renderer instance;
    public boolean simActive = false;

    //Card Layout
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    //Main Menu
    public JPanel mainMenuCard = new JPanel();
    public JLabel titleLabel = new JLabel("Collision Simulator", SwingConstants.CENTER);
    public JButton enterButton = new JButton("Enter Simulation");

    //Scenario1
    public JPanel scenario1Card = new JPanel();

    //Simulation Panel
    //All the drawing logic will happen here
    public SimulationPanel simulationPanel = new SimulationPanel(PhysicsEngine.getInstance());

    Renderer() {
        instance = this;

        //Frame setup
        setTitle("Collision Simulator");
        setSize(800, 800); //May be issue with 720p screens
        setResizable(false);
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
        scenario1Card.add(simulationPanel, BorderLayout.CENTER);
        //scenario1Card.add(scenario1Label, BorderLayout.NORTH);

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

        physicsEngine.spawnCircleBody(new Vector(400, 400), new Vector(150, 100), new Vector(0, 0), 1, 20, 1);

        simActive = true;
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
            Timer timer = new Timer((int) (physicsEngine.deltaTime * 1000), e ->{
                if (renderer.simActive) {
                    physicsEngine.step(); //Updates the physics
                    renderer.simulationPanel.repaint();
                }
                renderer.repaint();
            });
            timer.start();;
        });
    }
}