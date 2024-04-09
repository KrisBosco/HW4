import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Author: Kris Bosco
// Java class to handle all or majority of the text file adjustments.
public class TextFileMethods {
    private static final String FILE1 = "CardsDealt.txt";
    private static final String FILE2 = "LastWon.txt";
    private static final String FILE3 = "ExecutionLog.txt";
    private static final String FILE4 = "ArtDealersSelection.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    // Author: Kris Bosco
    // Method to write to CardsDealt.txt and ArtDealersSelections.txt
    // Method will apply timestamp and add commas to users input when writing to the files
    public static void logSelectedCards(int roundCount, List<String> selectedCards, String filename) {
        
        String file = "";

        if (filename == "FILE1"){
            file = FILE1;
        }

        if (filename == "FILE4"){
            file = FILE4;
        }
        
        TextFileMethods.logExecution("Writing to: " + file);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (firstRunToday()) {
                writer.write("Date: " + DATE_FORMAT.format(new Date()));
                writer.newLine();
            }
            
            StringBuilder cardsBuilder = new StringBuilder();
            for (int i = 0; i < selectedCards.size(); i++) {
                String card = selectedCards.get(i);
                if (i % 4 == 0 && i > 0) {
                    writer.write(cardsBuilder.toString());
                    writer.newLine();
                    cardsBuilder.setLength(0);
                }
                cardsBuilder.append(card);
                if (i % 4 != 3) {
                    cardsBuilder.append(", ");
                }
            }
            if (cardsBuilder.length() > 0) {
                writer.write(cardsBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Author: Kris Bosco
    // Method to write to LastWon.txt and update current value held in file
    public static void updateLastRoundWon(int round) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE2))) {
            writer.write(String.valueOf(round));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Author: Kris Bosco
    // Method to log current executions of the program at desired locations in code
    public static void logExecution(String activity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE3, true))) {
            Date currentDate = new Date();
            writer.write("[" + DATE_FORMAT.format(currentDate) + "] " + activity);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Author: Kris Bosco
    // Method to check if the application was ran for the first time on current day
    // Method returns true if program has been ran for the first time then it will apply date time stamp to the text files
    // Method returns false if the program was ran consecutively on the same day
    private static boolean firstRunToday() {
        String todayDateString = DATE_FORMAT.format(new Date());
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Date:")) {
                    String dateString = line.substring(6).trim();
                    if (todayDateString.equals(dateString)) {
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; 
    }

    // Author: Kris Bosco
    // Method to write "USER HAS WON PATTERN: X" to ArtDealersSelection.txt
    public static void userHasWonPattern(String message) {
        String filePath = "ArtDealersSelection.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
