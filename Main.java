/*Project Title: The Art Dealer
*
* Homework 4 Chief Programmer: Kris Bosco
* Date: 4/9/2024
*
* Team Members: Kris Bosco, Jesse Gemple, Karlin Clabon-Barnes, Kyle Peniston
* Class: CMP_SCI-4500 Keith Miller
*
* IDE: Visual Studio Code
* Programming Language: Java
* 
* Description:
* Program is designed to simulate a user-interactive game simulating the use of a generated 52 card playing deck.
* The game consists of six rounds of patterns the user will be able to play through. 
* The user's objective is to guess the pattern by selecting 4 of the 52 cards until the user is able to figure out what pattern
* the program is wanting.
*
* Functionality:
* User will have the ability to select 4 cards from the generated deck through text field using card codes to select the card from the deck. 
* User will have the ability to check the 4 selected cards to validate if a match as occured in the pattern.
* User will be able to quit the program any time using the "Quit" button or use of the red "X" button in upper right corner of the application
* Program will record each group of 4 cards the user has selected.
* Program will record each card from the group of 4 cards the program has selected.
* Program will record each round the user has won.
* Program will create a save state using file created within same directory to remember last round user has won.
* Program will create a log file of what the program is doing per each execution.
*
* Credit:
* Code examples and tutorials from W3schools, Stack Overflow, Udemy, Youtube and GeeksforGeeks
* were used in the development of this program.
* 
* 
*
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;

// Author: Kris Bosco
// Main java class. Used to initialize the program on launch.
public class Main {
    public static List<String> dealtCards = new ArrayList<>();
    public static Map<String, ImageIcon> cardImages = new HashMap<>();
    public static boolean gameOver = false;
    public static int currentRound;
    int startingRound = GameMethods.determineStartingRound();

    public static void main(String[] args) {
        // Initialize playing cards
        Deck.initializeDeck();
        Deck.loadCardImages(cardImages);

        // Determine current round based on integer stored in LastWon.txt
        currentRound = GameMethods.determineStartingRound();

        // Initialize the GUI
        Gui gui = new Gui(cardImages); 
        gui.setVisible(true);
    }
}

