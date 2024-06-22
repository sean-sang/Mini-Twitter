/**
 * Utility class containing helper functions for the Mini Twitter application.
 * Provides methods for UI layout, positive word detection, and date formatting.
 */

 import javax.swing.*;
 import java.awt.*;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 
 public class Utils {
 
   /**
    * Adds a split button layout to a panel.
    * This arranges a text input panel (usually containing a label and text field)
    * next to a button, with some spacing in between.
    *
    * @param panel        The panel to add the split buttons to.
    * @param inputPanel   The panel containing the input elements (label and text field).
    * @param button       The button to add next to the input panel.
    */
   public static void addSplitButtons(JPanel panel, JPanel userIdPanel, JButton addUser) {
     GridLayout gridLayout = new GridLayout(1, 2);
     gridLayout.setHgap(10);
     gridLayout.setVgap(10);
 
     JPanel container = new JPanel();
     container.setLayout(gridLayout);
     container.add(userIdPanel);
     container.add(addUser);
 
     panel.add(container);
   }
 
   /**
    * Array of positive words used for sentiment analysis of messages.
    * These words are all in uppercase to make the positivity check case-insensitive.
    */
   public static String[] positiveWords = {
     "Good",
     "Great",
     "Excellent",
     "Awesome",
     "Cool"
   };
 
   /**
    * Formats a given timestamp (milliseconds) into a human-readable date and time string.
    * Uses the format: "E, dd-MMM-yyyy HH:mm:ss" (e.g., "Mon, 24-Jun-2024 15:30:00").
    *
    * @param date The timestamp in milliseconds since the epoch.
    * @return The formatted date and time string.
    */
   public static String formatMyDate(long date) {
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss");
     return simpleDateFormat.format(new Date(date));
   }
 }