/**
 * The UserWindow class represents the individual view for a specific user in Mini Twitter.
 * It displays the user's information, following/followers, and news feed (tweets).
 * It also allows the user to follow other users and post new tweets.
 * 
 * This class uses the Observer pattern to update the following list and news feed 
 * whenever there are changes in the data model (User or Message classes).
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserWindow extends JFrame {

  /** Main panel for the user window layout. */

  private JPanel mainPanel;
  /** Dimensions of the user window. */
  public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private User user;

  private JTextField userId;
  private JButton followUser;

  private JTextArea tweetMessage;

  private JLabel lastUpdated, created;
  private JButton postTweet;
  private JList < String > followingList;

  // Observer pattern
  private Runnable newsObserver;
  private Runnable followerObserver;

  /**
   * Creates a new UserWindow to display the specified user's information.
   *
   * @param user The User object whose data will be shown in this window.
   */
  public UserWindow(User user) {
    setTitle(user.getName());
    this.user = user;
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    screenSize = new Dimension(dimension.width / 4, dimension.height * 2 / 4);

    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent componentEvent) {
        // do stuff
        screenSize = new Dimension(getWidth(), getHeight());
        refreshViews();
      }
    });
    setSize(screenSize);
    setLocation(0, 0);
    setLocationRelativeTo(null);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        if (followingList != null)
          User.followersObservers.remove(followingList);
        if (newsObserver != null)
          Message.messageObservers.remove(newsObserver);
      }
    });
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Refreshes the user interface by rebuilding the main panel and its components.
   */
  private void refreshViews() {
    if (mainPanel != null) {
      mainPanel.setVisible(false);
      remove(mainPanel);
    }
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    add(mainPanel);

    setVisible(true);

    addUserInfoPanel();
    addFollowingAndTweetPanel();
    addNewsFeedPanel();
    setupButtonListeners();
  }

  private void setupButtonListeners() {
    followUser.addActionListener(e -> {
      String user_id = userId.getText().trim();
      if (!User.usersMap.containsKey(user_id)) {
        JOptionPane.showMessageDialog(null, "User Not Found");
        return;
      }
      User myUser = User.usersMap.get(user_id);
      user.addFollowing(myUser);
    });

    postTweet.addActionListener(e -> {
      String messageText = tweetMessage.getText();
      if (messageText.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Enter Message First");
        return;
      }
      Message message = new Message();
      message.setText(messageText);
      message.setFrom(user.getUid());
      message.calculatePositivity();
      Message.sendMessage(message, user.getFollowers());
      tweetMessage.setText("");
    });
  }

  private JList < String > newsFeed;

  private void addNewsFeedPanel() {
    JPanel feed = new JPanel(new BorderLayout());
    feed.add(new JLabel("News Feed", JLabel.CENTER), BorderLayout.NORTH);

    newsFeed = new JList < String > ();

    if (newsObserver != null)
      Message.messageObservers.remove(newsObserver);

    newsObserver = () -> {
      DefaultListModel < String > defaultListModel = new DefaultListModel < String > ();
      for (String i: user.getNews()) {
        Message message = Message.messageMap.get(i);
        defaultListModel.addElement(" - " + message.getFrom() + " : " + message.getText());
      }
      newsFeed.setModel(defaultListModel);
      lastUpdated.setText("<html><center><b style='color:blue'>Last Time Updated: </b>" + Utils.formatMyDate(user.getLastUpdateTime()) + "</html>");
    };
    newsObserver.run();
    Message.messageObservers.add(newsObserver);
    feed.add(newsFeed, BorderLayout.CENTER);
    mainPanel.add(feed, BorderLayout.SOUTH);

    feed.setPreferredSize(new Dimension(screenSize.width, screenSize.height / 3));
    mainPanel.setBackground(Color.WHITE);
  }

  private void addFollowingAndTweetPanel() {

    JPanel followingPanel = new JPanel();
    followingPanel.setLayout(new BorderLayout());

    followingPanel.add(new JLabel("Currently Following", JLabel.CENTER), BorderLayout.NORTH);
    followingList = new JList < > ();
    if (followingList != null)
      User.followersObservers.remove(followingList);
    followerObserver = () -> {
      DefaultListModel < String > listModel = new DefaultListModel < > ();
      for (String i: user.getFollowings())
        listModel.addElement(" - " + i);
      followingList.setModel(listModel);
    };
    followerObserver.run();
    User.followersObservers.add(followerObserver);

    followingPanel.add(followingList, BorderLayout.CENTER);

    JPanel tweetPanel = new JPanel(new BorderLayout());

    JPanel tweet = new JPanel(new BorderLayout());
    tweet.add(new JLabel("Tweet Message", JLabel.CENTER), BorderLayout.NORTH);

    tweetMessage = new JTextArea();
    tweet.add(tweetMessage, BorderLayout.CENTER);
    tweet.setPreferredSize(new Dimension(screenSize.width * 2 / 3, 80));

    tweetPanel.add(tweet, BorderLayout.CENTER);

    postTweet = new JButton("Post Tweet");
    tweetPanel.add(postTweet, BorderLayout.EAST);

    followingPanel.add(tweetPanel, BorderLayout.SOUTH);
    mainPanel.add(followingPanel, BorderLayout.CENTER);
  }

  private void addUserInfoPanel() {
    JPanel userIdPanel = new JPanel();
    userIdPanel.setLayout(new GridLayout(2, 1));
    userIdPanel.add(new JLabel("User Id", JLabel.CENTER));
    userId = new JTextField();
    userIdPanel.add(userId);

    followUser = new JButton("Follow User");

    JPanel top = new JPanel();
    top.setLayout(new GridLayout(2, 2));

    created = new JLabel("<html><center><b style='color:blue'>Creation Time: </b>" + Utils.formatMyDate(user.getCreationTime()) + "</html>", JLabel.CENTER);
    top.add(created);
    lastUpdated = new JLabel("<html><center><b style='color:blue'>Last Time Updated: </b>" + Utils.formatMyDate(user.getLastUpdateTime()) + "</html>", JLabel.CENTER);
    top.add(lastUpdated);

    top.add(userIdPanel);
    top.add(followUser);
    mainPanel.add(top, BorderLayout.NORTH);

  }

}