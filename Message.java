/*
 * Represents a message (tweet) within the Mini Twitter application.
 * Implements the VisitorMessage interface for calculating positivity,
 * the Observer pattern to notify UI of new messages, and overall 
 * uses Composite in conjunction with User, UserGroup, and UserMember. 
 */
import java.util.*;

public class Message implements VisitorPositivity {

  // Composite Pattern: List of all users and a map of user IDs to user objects
  public static List < String > messages = new ArrayList < > ();
  public static HashMap < String, Message > messageMap = new HashMap < > ();

  // Observer Pattern: List of observers for list updates and followers update
  public static List < Runnable > messageObservers = new ArrayList < > ();

  private String uid;
  private String text;
  private String from;
  private Double positivePercentage;

  // Creates a new Message object with a randomly generated UUID. 
  public Message() {
    this.uid = UUID.randomUUID().toString();
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public Double getPositivePercentage() {
    return positivePercentage;
  }

  public void setPositivePercentage(Double positivePercentage) {
    this.positivePercentage = positivePercentage;
  }

  /**
   * Analyzes the message text and calculates the percentage of positivity.
   * Splits the text into words, counts the occurrences of positive words from Utils class,
   * and sets the `positivePercentage` based off those count of those values.
   */
  public void calculatePositivity() {
    String[] parts = text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase().split("\\s+");  
    int positive = 0;
    for (String i : parts) {
        if (Arrays.stream(Utils.positiveWords).anyMatch(i::equalsIgnoreCase)) {
            positive++;
        }
    }
    setPositivePercentage((positive * 100.0) / (parts.length * 1.0));
  }

  /**
   * Sends the given message to the specified followers.
   * Updates the global list and map of messages, adds the message to the news 
   * feeds of the sender and followers, and notifies observers of the new message.
   *
   * @param message   The message to be sent.
   * @param followers A list of IDs of users who should receive the message.
   */
  public static void sendMessage(Message message, List < String > followers) {
    messages.add(message.getUid());
    messageMap.put(message.getUid(), message);
    for (String i: followers) {
      User follower = User.usersMap.get(i);
      follower.getNews().add(message.getUid());
      follower.setLastUpdateTime(System.currentTimeMillis());
    }
    User sender = User.usersMap.get(message.getFrom());
    sender.getNews().add(message.getUid());
    sender.setLastUpdateTime(System.currentTimeMillis());
    for (Runnable r: messageObservers)
      r.run();
  }

  /**
   * Visitor pattern method for collecting positivity percentages across messages.
   *
   * @param positiveness A list to which the message's positivity percentage is added.
   */
  @Override
  public void visit(List < Double > positiveness) {
    positiveness.add(getPositivePercentage());
  }
}