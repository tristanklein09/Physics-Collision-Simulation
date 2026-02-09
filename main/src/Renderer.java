import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Renderer extends JFrame{
    private static Renderer instance;
    public boolean simActive = false;
    public boolean antialiasing = true; //The checkbox is automatically selected
    public boolean showAABB = true;
    public boolean toggleGravity = true;
    public String[] shapeOptions = {"Circle"};

    public Insets subTitleInsets = new Insets(10, 10, 10, 10);
    public Insets labelInsets = new Insets(5, 5, 5, 5);
    public Insets buttonInsets = new Insets(10, 10, 10, 10);

    //Card Layout
    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    //Main Menu
    public JPanel mainMenuCard = new JPanel();
    public JPanel mainMenuCenterPanel = new JPanel();
    public JLabel titleLabel = new JLabel("Collision Simulator", SwingConstants.CENTER);
    public JButton enterButton = new JButton("Enter Simulation");
    public JCheckBox antialiasingCheckBox = new JCheckBox("Enable Antialiasing", true);

    //Scenario1
    public JPanel scenario1Card = new JPanel();

    //Debug Panel
    public JPanel debugPanel = new JPanel();
    public JScrollPane debugScrollPanel = new JScrollPane(debugPanel);
    public JLabel debugTitleLabel = new JLabel("Debug Menu", SwingConstants.CENTER);
    public JLabel spawnShapesLabel = new JLabel("Spawn shapes", SwingConstants.CENTER);
    public JCheckBox showAABBBox= new JCheckBox("Show AABBs", true); //Checkboxes
    public JCheckBox toggleGravityBox = new JCheckBox("Toggle Gravity", true);
    public JComboBox shapeComboBox = new JComboBox(shapeOptions);
    public JTextField shapeRadiusText = new JTextField(10);
    public JTextField shapeMassText = new JTextField(10);
    public JTextField shapeRestitutionText = new JTextField(10);
    public JTextField velocityText = new JTextField(10);
    public JTextField positionText = new JTextField(10);
    public JButton spawnShapeButton = new JButton("Spawn Shape");
    public JLabel shapeTypeLabel = new JLabel("Shape Type:");
    public JLabel shapeRadiusLabel = new JLabel("Radius:");
    public JLabel shapeMassLabel = new JLabel("Mass:");
    public JLabel shapeRestitutionLabel = new JLabel("Restitution:");
    public JLabel velocityLabel = new JLabel("Velocity (x,y):");
    public JLabel positionLabel = new JLabel("Position (x,y):");

    //Simulation Panel
    //All the drawing logic will happen here
    public SimulationPanel simulationPanel = new SimulationPanel(PhysicsEngine.getPEInstance(), this);

    Renderer() {
        instance = this;

        //Frame setup
        setTitle("Collision Simulator");
        setSize(1200, 900); //May be issue with 720p screens
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(cardPanel); //Card Layout lives on one container

        //Main menu card
        mainMenuCard.setLayout(new GridLayout(3, 1, 10, 10));
        mainMenuCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainMenuCenterPanel.setLayout(new BoxLayout(mainMenuCenterPanel, BoxLayout.Y_AXIS));
        antialiasingCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuCard.add(titleLabel);
        mainMenuCard.add(mainMenuCenterPanel);
        mainMenuCenterPanel.add(antialiasingCheckBox);
        mainMenuCard.add(enterButton);

        //Scenario 1 card
        scenario1Card.setLayout(new BorderLayout(10,10));
        scenario1Card.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        scenario1Card.add(simulationPanel, BorderLayout.CENTER);
        scenario1Card.add(debugScrollPanel, BorderLayout.EAST);

        //Debug meny
        debugScrollPanel.setPreferredSize(new Dimension(300, 900));
        debugScrollPanel.setLayout(new ScrollPaneLayout());
        debugScrollPanel.setViewportView(debugPanel);
        debugPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = subTitleInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        debugPanel.add(debugTitleLabel, gbc);
        debugTitleLabel.setFont(new Font(debugTitleLabel.getFont().getName(), Font.BOLD, 18));

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = labelInsets;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(showAABBBox, gbc);

        gbc.gridy = 2;
        gbc.insets = labelInsets;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(toggleGravityBox, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = subTitleInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        debugPanel.add(spawnShapesLabel, gbc);
        spawnShapesLabel.setFont(new Font(debugTitleLabel.getFont().getName(), Font.BOLD, 15));

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = labelInsets;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(shapeTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        debugPanel.add(shapeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(velocityLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        debugPanel.add(velocityText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(positionLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        debugPanel.add(positionText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(shapeRadiusLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        debugPanel.add(shapeRadiusText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(shapeMassLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        debugPanel.add(shapeMassText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        debugPanel.add(shapeRestitutionLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        debugPanel.add(shapeRestitutionText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = buttonInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        debugPanel.add(spawnShapeButton, gbc);

        simulationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));


        //Add cards
        cardPanel.add(mainMenuCard, "MAIN_MENU");
        cardPanel.add(scenario1Card, "SCENARIO1");

        //Show initial cards
        cardLayout.show(cardPanel, "MAIN_MENU"); //show the container (cardPanel), the name is a reference to mainMenuCard
        setVisible(true);
    }

    public static Renderer getRendererInstance() {
        if (instance == null) { //Handling null
            instance = new Renderer();
        }
        return instance;
    }

    public void loadSandbox1() {
        PhysicsEngine physicsEngine = PhysicsEngine.getPEInstance();
        SimulationPanel simulationPanel = new SimulationPanel(physicsEngine, this);
        cardLayout.show(cardPanel, "SCENARIO1"); //Switches to the other card panel


        //Spawning circles
        physicsEngine.spawnCircleBody(new Vector2D(400, 400), new Vector2D(400, 400), new Vector2D(0, 981), 1, 25, 1);
        physicsEngine.spawnCircleBody(new Vector2D(500, 500), new Vector2D(500, 500), new Vector2D(0, 981), 2, 50, 1);
        physicsEngine.spawnCircleBody(new Vector2D(300, 300), new Vector2D(-400, -400), new Vector2D(0, 981), 1.25, 30, 1);
        physicsEngine.spawnCircleBody(new Vector2D(100, 200), new Vector2D(-600, -500), new Vector2D(0, 981), 0.25, 15, 1);
        physicsEngine.spawnCircleBody(new Vector2D(150, 300), new Vector2D(500, -200), new Vector2D(0, 981), 1, 20, 1);
        physicsEngine.spawnCircleBody(new Vector2D(600, 600), new Vector2D(-500, 200), new Vector2D(0, 981), 1.5, 35, 1);


        simActive = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Renderer renderer = new Renderer();
            PhysicsEngine physicsEngine = PhysicsEngine.getPEInstance();

            //Main menu events
            if (!renderer.simActive) {
                //Event for the button
                renderer.enterButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Takes us to the place where collisions happen
                        renderer.loadSandbox1();
                    }
                });

                //Event for antialiasing Check Box
                renderer.antialiasingCheckBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);
                        if (isSelected) {
                           renderer.antialiasing = true;
                        } else if (!isSelected) {
                            renderer.antialiasing = false;
                        }
                    }
                });
            }

            //Updates the scene at the deltaTime interval
            Timer timer = new Timer((int) (physicsEngine.deltaTime * 1000), e ->{

                if (renderer.simActive) {
                    //TODO: Add a way so that it only changes when a resize event has happened - should increase performance as we're not constantly getting the dimensions
                    physicsEngine.setWorldBounds(
                            renderer.simulationPanel.getWidth(),
                            renderer.simulationPanel.getHeight()
                    );

                    physicsEngine.step(); //Updates the physics

                    //Events for the debug menu
                    renderer.showAABBBox.addItemListener(new ItemListener() { //Event for the show AABB checkbox
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);
                            if (isSelected) {
                                renderer.showAABB = true;
                            } else if (!isSelected) {
                                renderer.showAABB = false;
                            }
                        }
                    });
                    renderer.toggleGravityBox.addItemListener(new ItemListener() { //Event for gravity checkbox
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);
                            if (isSelected) {
                                renderer.toggleGravity = true;
                            } else if (!isSelected) {
                                renderer.toggleGravity = false;
                            }
                        }
                    });


                    renderer.simulationPanel.repaint();
                }
                renderer.repaint();
            });
            timer.start();;
        });
    }
}