import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// Author: Kris Bosco
// Java class to house all methods that make up / design the GUI
public class Gui extends JFrame {
    private JPanel cardPanel;
    private JButton selectButton;
    private JButton sellButton;
    private JButton quitButton;
    private JButton displayButton;
    private JLabel welcomeLabel;
    private Map<String, ImageIcon> cardImages;

    // Author: Kyle Peniston
    // Method to initialize the GUI
    public Gui(Map<String, ImageIcon> cardImages) {
        this.cardImages = cardImages;
        setTitle("The Art Dealer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();
        setLocationRelativeTo(null);
    }

    // Author: Kyle Peniston
    // Method to layout the visible components on the GUI
    private void layoutComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(quitButton);
        

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(welcomeLabel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    // Author: Kyle Peniston
    // Method to display the welcome label and to create/initialize the buttons
    public void initComponents() {
        cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButton = new JButton("Select Cards");
        sellButton = new JButton("Sell to Art Dealer");
        quitButton = new JButton("Quit");
        displayButton = new JButton("History"); 
        welcomeLabel = new JLabel("<html><body>Card Selling Program. Built With Java.<br><br>" +
                "HW4 Chief Programmer: Kris Bosco<br>" +
                "Revision Date: March 24th, 2024<br><br>" +
                "Team Members: Kris Bosco, Jesse Gemple, Karlin Clabon-Barnes, Kyle Peniston<br>" +
                "CMP_SCI-4500 Keith Miller<br><br>" +
                "This program is a guessing game consisting of six rounds of challenges.<br>" +
                "See if you can guess what cards the Art Dealer is interested in.<br><br>" +
                "To begin guessing, click on the Select Cards button and select four cards from the deck.<br>" +
                "Once four cards have been selected, click the Sell To Art Dealer button to see if you guessed correctly.");

        // Author: Kris Bosco
        // Button to allow the user to select cards when guessing in the game
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiMethods.selectedCardCount == 4) {
                    GuiMethods.clearCardPanel(cardPanel);
                    GuiMethods.selectedCardCount = 0;
                    GuiMethods.userInput(e, cardImages, cardPanel, GameMethods.getRoundCount());
                } else {
                    GuiMethods.userInput(e, cardImages, cardPanel, GameMethods.getRoundCount());
                }
            }
        });

        // Author: Kris Bosco
        // Button to allow the user to check if their current selection satisfies the current rounds patterns
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedCards = GuiMethods.getSelectedCards();
                GameMethods.checkPattern(selectedCards, cardPanel, cardImages);
                selectedCards.clear();
                GuiMethods.selectedCardCount = 0;
            }
        });

        // Author: Kyle Peniston
        // Button to close applicaton
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiMethods.quitGame();
            }
        });

        // Author: Kris Bosco
        // Button to display the history of the users selections and correct cards chosen so far
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCorrectChoices();
            }
        });
    }

    // Author: Kris Bosco
    // Method to display correct choices when displayButton is clicked
    // Will take the data stored in the ArtDealersSelection.txt and display them in the history
    private void displayCorrectChoices() {
        JTextArea textArea = new JTextArea(20, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
    
        JFrame frame = new JFrame("Correct Choices");
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    
        // Read the contents of CardsDealt.txt and display them
        refreshTextArea(textArea);
    
        // Allows the history window to be updated in real timed
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTextArea(textArea);
            }
        });
        timer.start();
    }
    // Author: Kris Bosco
    // Method to apply the text area refresh to allow the history to be updated in real time for the user
    private void refreshTextArea(JTextArea textArea) {
        textArea.setText(""); 
        try (BufferedReader reader = new BufferedReader(new FileReader("ArtDealersSelection.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
