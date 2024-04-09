import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// Author: Kris Bosco
// Java class to hold all methods to make the game functional
public class GameMethods {

    public static final int MAX_ROUNDS = 6;
    private static int correctGuessesCount = 0;
    private static int roundCount;
    private static char highestRank = '2';
    private static List<String> correctChoices = new ArrayList<>();

    // Author: Kris Bosco
    // Main game method to check users selection with current rounds pattern
    public static void checkPattern(List<String> selectedCards, JPanel cardPanel, Map<String, ImageIcon> cardImages) {
        
        TextFileMethods.logExecution("Current Round: " + roundCount);
        
        // Input validation to check if user has selected any cards
        // If user selects no cards, then exit method and start over
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cards have been selected. Click Select Cards to begin.");
            GuiMethods.clearCardPanel(cardPanel);
            return;
        }

        // Write users selected cards to CardsDealt.txt
        TextFileMethods.logSelectedCards(roundCount, selectedCards, "FILE1");

        if (roundComplete(selectedCards)) {

            correctGuessesCount++;

            // Check if user has completed two successful guesses per round
            // Proceed to next round if all requirements are satisfied
            if (correctGuessesCount == 2) {
                roundCount++;
                correctGuessesCount = 0; 

                // Increment through rounds while round count is less than 6
                // Prompt user to continue through rounds or exit when a round is complete
                if (roundCount <= MAX_ROUNDS) {   
                    if (soldTwoGroups(selectedCards)) {      
                        winningMessages(roundCount - 1);
                        TextFileMethods.updateLastRoundWon(roundCount - 1);
                        TextFileMethods.logSelectedCards(roundCount, correctChoices, "FILE4");
                        int option = JOptionPane.showConfirmDialog(null, "Do you want to continue to the next round?", "Continue?", JOptionPane.YES_NO_OPTION);
                        
                        // Exit program if user chooses not to continue to next rount or exits out of the pop up
                        if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                            JOptionPane.showMessageDialog(null, "Thank you for playing! Have a nice day!");
                            System.exit(0);
                        }
                    } else {
                        GuiMethods.clearCardPanel(cardPanel);
                        return;
                    }
                } else {

                    // Else statement for when user has completed the last round
                    // LastWon.txt value updates to 6 and user is prompted if they would like to start over or exit
                    JOptionPane.showMessageDialog(null, "Congratulations!! You have completed all the rounds. The Art Dealer is quite happy with his new collection.");
                    TextFileMethods.logSelectedCards(roundCount, correctChoices, "FILE4");
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                        TextFileMethods.updateLastRoundWon(MAX_ROUNDS); 
                        JOptionPane.showMessageDialog(null, "Thank you for playing! Have a nice day!");
                        System.exit(0);
                    } else {
                        TextFileMethods.updateLastRoundWon(0); 
                        roundCount = 1;
                    }
                }
            } else {
                // Message for user to have them guess another round
                JOptionPane.showMessageDialog(null, "Well done! You guessed correctly. Try to guess correctly one more time to proceed to the next round.");
                TextFileMethods.logSelectedCards(roundCount, correctChoices, "FILE4");
            }
        } else {
            // Message for user if they have selected cards that do not match the pattern for the current round
            JOptionPane.showMessageDialog(null, "Incorrect cards selected. The Art Dealer isn't happy with the current selection. Maybe what he does like can be used as a hint?");
            TextFileMethods.logSelectedCards(roundCount, correctChoices, "FILE4");
        }

        // Clear the cardPanel before displaying new cards
        GuiMethods.clearCardPanel(cardPanel);

        correctChoices.clear();

        // Display selected cards as user inputs their selection
        for (String card : selectedCards) {
            GuiMethods.displayCardInPanel(card, cardImages, cardPanel, selectedCards, roundCount);
        }
    }

    // Author: Kris Bosco
    // Method to check if the users selected cards match the current pattern and if the user has sold two groups of correct cards to the "Art Dealer"
    // Method will return true if user has satisfied the patterns conditions
    public static boolean correctCards(int roundCount, List<String> selectedCards) {
        boolean isCorrect = false;
        switch (roundCount) {
            case 1:
                isCorrect = allRedCards(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 2:
                isCorrect = allClubs(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 3:
                isCorrect = allFaceCards(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 4:
                isCorrect = allSingleDigits(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 5:
                isCorrect = allSingleDigitPrimes(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 6:
                isCorrect = highestRank(selectedCards) && soldTwoGroups(selectedCards);
                break;
            default:
                isCorrect = false;
                break;
        }
       
        return isCorrect;
    }

    // Author: Kris Bosco
    // Method to display the winning messages per round completion
    private static void winningMessages(int round) {
        String message = "";
        switch (round) {
            case 1:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is quite pleased with all these red cards!");
                message = "USER HAS WON PATTERN: 1";
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is going clubbin now!");
                message = "USER HAS WON PATTERN: 2";
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer has made some new friends!");
                message = "USER HAS WON PATTERN: 3";
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is happy you singled out the good cards for him");
                message = "USER HAS WON PATTERN: 4";
                break;
            case 5:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is at his prime now!");
                message = "USER HAS WON PATTERN: 5";
                break;
            case 6:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer only wanted the best you had to offer!");
                message = "USER HAS WON PATTERN: 6";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Something went wrong.");
                break;
        }

        JOptionPane.showMessageDialog(null, message);

        TextFileMethods.userHasWonPattern(message);
    }

    // Author: Kris Bosco
    // Method to check if user has completed the round
    public static boolean roundComplete(List<String> selectedCards) {
        return correctCards(roundCount, selectedCards);
    }

    // Author: Kris Bosco
    // Method to return current round count. Used in Card Select button in GUI
    public static int getRoundCount() {
        return roundCount;
    }

    // Author: Kris Bosco
    // Method to determine the starting round of the game and set current round count per value stored in LastWon.txt
    // Method will return current round count used to determine what pattern the user is on when making guesses
    public static int determineStartingRound() {
        int startingRound = 1;
    
        // Create LastWon.txt if it does not exist
        File lastWonFile = new File("LastWon.txt");
        if (!lastWonFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastWonFile))) {
                writer.write("0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(lastWonFile))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                int lastWonRound = Integer.parseInt(line.trim());
                if (lastWonRound == MAX_ROUNDS) {

                    // Inform user that they have won all patterns if LastWon.txt has a value of 6
                    int option = JOptionPane.showConfirmDialog(null, "You have won all the rounds already. Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {

                        // If user wants to play again, update LastWon.txt to have a value of 0 and start the game over
                        try (FileWriter writer = new FileWriter(lastWonFile)) {
                            writer.write("0");
                        }
                        startingRound = 1;
                    } else {

                        // If user doesn't want to play again, exit the program
                        JOptionPane.showMessageDialog(null, "Thank you for playing! Have a nice day!");
                        System.exit(0);
                    }
                } else {

                    // If LastWon.txt is not equal to MAX_ROUNDS, set startingRound to (lastWonRound + 1)
                    startingRound = lastWonRound + 1;
                }
            } else {

                // If LastWon.txt is empty, start from the first round
                startingRound = 1;
            }
        } catch (IOException e) {

            // Handle file IO exception
            e.printStackTrace();
            startingRound = 1; 
        } catch (NumberFormatException e) {
            // Handle parsing error if the content of LastWon.txt is not a valid integer
            e.printStackTrace();  
            startingRound = 1; 
        }
    
        roundCount = startingRound;
    
        // Set round count to 1 if on a new game
        if (startingRound == 1) {
            roundCount = 1;
        }
    
        return roundCount;
    }    
        

    
    // Author: Kris Bosco
    // Method to apply asterisks to the users input to provide a visual indicator of the correct cards of the round
    private static void builder(String card, boolean correct) {

        StringBuilder newCard = new StringBuilder();

        if ((correctChoices.size() < 4) && correct) {
            newCard.append("*").append(card).append("*");
        } else if ((correctChoices.size() == 4) && correct) {
            newCard.append("*").append(card).append("*");
        } else if ((correctChoices.size() < 4) && !correct) {
            newCard.append(card);
        }

        correctChoices.add(newCard.toString());

        TextFileMethods.logExecution("In builder: " + correctChoices);
    }

    // Author: Kris Bosco
    // Method to only allow the card suits hearts or diamonds as correct choices when user is making selections
    // Will return true if only hearts or diamonds are selected in any combination of choices
    private static boolean allRedCards(List<String> selectedCards) {
        int hearts = 0;
        int diamonds = 0;


        for (String card : selectedCards) {
            if (card.contains("H")) {
                builder(card, true);
                hearts++;
            } else if (card.contains("D")) {
                builder(card, true);
                diamonds++;
            } else {
                builder(card, false);
            }
        }

        return (hearts == 4 || diamonds == 4) ||
                (hearts == 1 && diamonds == 3) ||
                (hearts == 3 && diamonds == 1) ||
                (hearts == 2 && diamonds == 2);
    }

    // Author: Kris Bosco
    // Method to only allow the card suit clubs as correct choices when user is making selections
    // Will return true if only clubs are selected by the user
    private static boolean allClubs(List<String> selectedCards) {
        int clubs = 0;

        for (String card : selectedCards) {
            if (card.contains("C")) {
                builder(card, true);
                clubs++;
            } else {
                builder(card, false);
            }
        }

        return clubs == 4;
    }

    // Author: Kris Bosco
    // Method to only allow face cards as correct choices when user is making selections
    // Will return true if only Jacks, Queens, or Kings are selected in any combination of choices
    private static boolean allFaceCards(List<String> selectedCards) {
        int faceCards = 0;

        for (String card : selectedCards) {
            if (card.contains("K") || card.contains("Q") || card.contains("J")) {
                builder(card, true);
                faceCards++;
            } else {
                builder(card, false);
            }
        }

        return faceCards == 4;
    }

    // Author: Kris Bosco
    // Method to only allow single digit cards as correct choices when user is making selections
    // Will return tru if only cards between 2-9 of any suit are selection in any combination of choices
    private static boolean allSingleDigits(List<String> selectedCards) {
        int singleDigits = 0;

        for (String card : selectedCards) {
            char rank = card.charAt(0);
            if (Character.isDigit(rank)) {
                builder(card, true);
                singleDigits++;
            } else {
                builder(card, false);
            }
        }

        return singleDigits == 4;
    }

    // Author: Kris Bosco
    // Method to only allow single digit prime cards as correct choices when user is making selections
    // Will return true if cards of any sute that contain 3, 5, 7 are selected in any combination of choices
    private static boolean allSingleDigitPrimes(List<String> selectedCards) {
        int primeDigits = 0;

        for (String card : selectedCards) {
            char rank = card.charAt(0);
            if (rank == '2' || rank == '3' || rank == '5' || rank == '7') {
                builder(card, true);
                primeDigits++;
            } else {
                builder(card, false);
            }
        }

        return primeDigits == 4;
    }

    // Author: Kris Bosco
    // Method to only allow the highest rank card of the current group of cards selected by the user to be correct when user is making selections
    // Will return true if user selects the same card for all suits and is the highest rank entered during the round.
    private static boolean highestRank(List<String> selectedCards) {

        for (String card : selectedCards) {
            char rank = card.charAt(0);
            if (rank > highestRank) {
                highestRank = rank;
            }
        }

        int count = 0;
        for (String card : selectedCards) {
            if (card.charAt(0) == highestRank) {
                builder(card, true);
                count++;
            } else {
                builder(card, false);
            }
        }

        return count == 4;
    }

    // Author: Kris Bosco
    // Method to check if user has sold two groups of four to the "Art Dealer"
    // Returns false if user has only sold one group
    private static boolean soldTwoGroups(List<String> selectedCards) {
        int size = selectedCards.size();
        if (size % 2 != 0 || size < 4) {
            return false;
        } else {
            return true;
        }

    }

}
