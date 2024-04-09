import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Author: Kyle Peniston
// Java class to create/initialize the playing deck
public class Deck {
    private static final List<String> deck = new ArrayList<>();

    // Author: Kyle Peniston
    // Initialize playing deck.
    public static void initializeDeck() {
        String[] suits = {"S", "H", "D", "C"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                String card = rank + suit;
                deck.add(card);
            }
        }
    }

    // Author: Kyle Peniston
    // Method to load card images.
    public static void loadCardImages(Map<String, ImageIcon> cardImages) {
        try {
            String[] suits = {"S", "H", "D", "C"};
            String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

            for (String suit : suits) {
                for (String rank : ranks) {
                    String cardName = rank + suit;
                    String imagePath = "images/" + cardName + ".png";
                    cardImages.put(cardName, new ImageIcon(Deck.class.getResource(imagePath)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
