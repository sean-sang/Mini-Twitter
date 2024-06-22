/**
 * The main window of the Mini Twitter application.
 * This class is a Singleton, ensuring only one instance exists.
 * It manages the overall layout, tree view of users and groups,
 * and provides buttons for various actions.
 *
 * Design Patterns Used:
 * - Singleton: Ensures only one instance of this window.
 * - Observer: Uses observers to update the UI when data changes.
 */

 import javax.swing.*;
 import javax.swing.tree.DefaultMutableTreeNode;
 import javax.swing.tree.TreePath;
 import java.awt.*;
 import java.awt.event.ComponentAdapter;
 import java.awt.event.ComponentEvent;
 import java.util.List;
 import java.util.*;
 
 public class MiniTwitterWindow extends JFrame {
 
   // --- UI Components and Data ---
   public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   private JPanel mainPanel;
   private JPanel leftPanel;
 
   // Tree view for user/group hierarchy
   private DefaultMutableTreeNode rootTree;
   private JTree jtree;
 
   private JPanel centerPanel;
 
   private JTextField userId, groupId;
   private JButton addUser, addGroup, openUserView;
 
   // Singleton instance
   private static MiniTwitterWindow instance;
 
   /**
    * Singleton pattern: Returns the single instance of this window,
    * creating it if it doesn't exist. Sets basic window properties.
    * @return The single instance of MiniTwitterWindow.
    */
   public static MiniTwitterWindow getInstance() {
     if (instance == null) {
       // Create the window and set initial properties, centering it to user's screen
       instance = new MiniTwitterWindow();
       Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
       instance.setSize(dimension.width * 2 / 3, dimension.height * 3 / 4);
       instance.setLocation(0, 0);
       instance.setLocationRelativeTo(null);
       instance.setVisible(true);
       instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }
     return instance;
   }
 
   /**
    * Private constructor for Singleton pattern.
    * Sets up initial view and registers observers for UI updates.
    */
   public MiniTwitterWindow() {
 
     refreshViews();
     setTitle("Mini Twitter");
 
     // Observer Pattern: Register for updates from User, UserGroup, and Message
     User.listObservers.add(this::refreshViews);
     UserGroup.listObservers.add(this::refreshViews);
     Message.messageObservers.add(this::refreshViews);
 
     Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
     screenSize = new Dimension(dimension.width * 2 / 3, dimension.height * 3 / 4);
 
     addComponentListener(new ComponentAdapter() {
       public void componentResized(ComponentEvent componentEvent) {
 
         screenSize = new Dimension(getWidth(), getHeight());
         refreshViews();
       }
     });
   }
 
   /**
    * Refreshes the entire UI when a change in user/group/message data is observed.
    * Rebuilds the main panel, left panel (tree view), and center panel with updated data.
    */
   public void refreshViews() {
     if (mainPanel != null) {
       mainPanel.setVisible(false);
       remove(mainPanel);
     }
     mainPanel = new JPanel();
     mainPanel.setLayout(new BorderLayout());
     mainPanel.setBackground(Color.WHITE);
     add(mainPanel);
 
     addLeftPanel();
     addCenterPanel();
 
     // Auto selects the Root tree to be able to quickly add new users by default
     TreePath rootPath = new TreePath(rootTree);
     jtree.setSelectionPath(rootPath);
   }
 
   // --- UI Building and Layout ---
   private JButton validateButton;
   private JButton lastUpdatedUser;
 
   /**
    * Adds the center panel to the main panel.
    * The center panel contains buttons for adding users and groups, opening
    * user views, validating IDs, and displaying statistics about users,
    * groups, messages, and message positivity.
    */
   private void addCenterPanel() {
     centerPanel = new JPanel();
     centerPanel.setLayout(new BorderLayout());
 
     // Top part of the center panel
     JPanel topButtonPanel = new JPanel();
     GridLayout gridLayout = new GridLayout(4, 1);
     topButtonPanel.setLayout(gridLayout);
 
     // Bottom part of the center panel
     JPanel bottomButtonPanel = new JPanel();
     GridLayout gridLayoutBtm = new GridLayout(3, 2);
     bottomButtonPanel.setLayout(gridLayoutBtm);
 
     centerPanel.add(topButtonPanel, BorderLayout.NORTH);
     centerPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
 
     // Add User section
     JPanel userIdPanel = new JPanel();
     userIdPanel.setLayout(new GridLayout(2, 1));
     userIdPanel.add(new JLabel("User Id", JLabel.CENTER));
     userId = new JTextField();
     userIdPanel.add(userId);
 
     //        btnTop.add(userIdPanel);
 
     addUser = new JButton("Add User");
     Utils.addSplitButtons(topButtonPanel, userIdPanel, addUser);
 
     // Add Group section
     JPanel groupIdPanel = new JPanel();
     groupIdPanel.setLayout(new GridLayout(2, 1));
     groupIdPanel.add(new JLabel("Group Id", JLabel.CENTER));
     groupId = new JTextField();
     groupIdPanel.add(groupId);
 
     //        btnTop.add(groupIdPanel);
 
     addGroup = new JButton("Add Group");
     Utils.addSplitButtons(topButtonPanel, groupIdPanel, addGroup);
 
     // User View and Validation buttons
     openUserView = new JButton("Open User View");
     topButtonPanel.add(openUserView);
 
     validateButton = new JButton("User/Group ID verification");
     topButtonPanel.add(validateButton);
 
     // Statistics buttons providing information
     JButton usersButton = new JButton("<html><center>Users<br>" + User.users.size() + "</html>");
     JButton groupsButton = new JButton("<html><center>Groups<br>" + UserGroup.userGroups.size() + "</html>");
     JButton messagesButton = new JButton("<html><center>Messages<br>" + Message.messages.size() + "</html>");
 
     // Calculate and display average message positivity
     List < Double > messagePositivity = new ArrayList < > ();
     for (String i: Message.messages)
       Message.messageMap.get(i).visit(messagePositivity);
 
     OptionalDouble optionalDouble = messagePositivity.stream().mapToDouble(a -> a).average();
     JButton positiveButton = new JButton("<html><center>Positive<br>" + (optionalDouble.isPresent() ? optionalDouble.getAsDouble() : "0") + "%</html>");
 
     JButton lastUpdatedUserButton = new JButton("<html><center>Last Updated User</html>");
 
     // Add buttons to panel
     bottomButtonPanel.add(usersButton);
     bottomButtonPanel.add(groupsButton);
     bottomButtonPanel.add(messagesButton);
     bottomButtonPanel.add(positiveButton);
 
     // Add action listeners using lambda expressions
     usersButton.addActionListener(e -> JOptionPane.showMessageDialog(null, " - " + String.join("\n - ", User.users), "All Users", JOptionPane.INFORMATION_MESSAGE));
 
     groupsButton.addActionListener(e -> JOptionPane.showMessageDialog(null, " - " + String.join("\n - ", UserGroup.userGroups), "All Groups", JOptionPane.INFORMATION_MESSAGE));
 
     List < String > messages = new ArrayList < > ();
     for (String i: Message.messages)
       messages.add(Message.messageMap.get(i).getFrom() + ": " + Message.messageMap.get(i).getText());
     messagesButton.addActionListener(e -> JOptionPane.showMessageDialog(null, " - " + String.join("\n - ", messages), "All Messages", JOptionPane.INFORMATION_MESSAGE));
 
     positiveButton.addActionListener(e -> {
       double positivityPercentage = optionalDouble.isPresent() ? optionalDouble.getAsDouble() : 0.0;
       JOptionPane.showMessageDialog(null, "Positive Percentage: " + positivityPercentage + "%\n\n Words Affecting Positivity:\n" + Arrays.toString(Utils.positiveWords), "Message Positivity", JOptionPane.INFORMATION_MESSAGE);
     });
 
     lastUpdatedUser = new JButton("<html><center>Last User Updated</html>");
     bottomButtonPanel.add(lastUpdatedUser);
 
     mainPanel.add(centerPanel, BorderLayout.CENTER);
     configureButtons();
   }
 
   /**
    * Configures action listeners for the buttons in the center panel.
    * These listeners handle adding users, groups, opening user views,
    * validating IDs, and displaying information when buttons are clicked.
    */
   private void configureButtons() {
     addUser.addActionListener(e -> {
       String user = userId.getText();
       if (!user.isEmpty()) {
         DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
         if (selectedNode != null && selectedNode instanceof GroupTreeNode) { // Check if it's a group node
           UserGroup userGroup = UserGroup.userGroupMap.get(selectedNode.getUserObject()); // Get UserGroup from the node
 
           // Validate the new user ID and add user to selected group
           if (User.usersMap.containsKey(user)) {
             JOptionPane.showMessageDialog(null, "User Already Exists!", "Error", JOptionPane.ERROR_MESSAGE);
             return;
           }
 
           User tmpUser = new User();
           tmpUser.setName(user);
           tmpUser.setUid(user);
           User.addUser(tmpUser);
           userGroup.addUser(tmpUser);
           userId.setText("");
         } else {
           JOptionPane.showMessageDialog(null, "Please select a group to add the user under.", "Error", JOptionPane.ERROR_MESSAGE);
         }
       }
     });
 
     addGroup.addActionListener(e -> {
       //            System.out.println(jtree.getLastSelectedPathComponent());
       String userGroup = groupId.getText();
       if (!userGroup.isEmpty()) {
         String selectedSegment = jtree.getLastSelectedPathComponent() == null ? "Root" : jtree.getLastSelectedPathComponent().toString();
         UserGroup usersGroup = UserGroup.userGroupMap.get(selectedSegment);
         if (UserGroup.userGroups.contains(selectedSegment)) {
           UserGroup tmpUserGroup = new UserGroup();
           tmpUserGroup.setName(userGroup);
           tmpUserGroup.setUid(userGroup);
           UserGroup.addUserGroup(tmpUserGroup);
           usersGroup.addGroup(tmpUserGroup);
           groupId.setText("");
         }
       }
     });
 
     openUserView.addActionListener(e -> {
       String selectedSegment = jtree.getLastSelectedPathComponent() == null ? "Root" : jtree.getLastSelectedPathComponent().toString();
       if (User.usersMap.containsKey(selectedSegment)) {
         UserWindow userWindow = new UserWindow(User.usersMap.get(selectedSegment));
 
         // Prioritize the UserWindow when created
         userWindow.setAlwaysOnTop(true);
         userWindow.toFront();
         userWindow.requestFocus();
         userWindow.setLocationRelativeTo(null);
 
       } else {
         JOptionPane.showMessageDialog(null, "No User Selected");
       }
     });
 
     validateButton.addActionListener(e -> {
       final StringBuilder validationerror = new StringBuilder("");
       Runnable invalidRunnable = () -> {
         JOptionPane.showMessageDialog(null, validationerror.toString(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
       };
 
       Runnable validRunnable = () -> {
         JOptionPane.showMessageDialog(null, "All IDs are Valid", "Validation Success", JOptionPane.INFORMATION_MESSAGE);
       };
 
       //validate users
       HashSet < String > userIds = new HashSet < > ();
       for (String i: User.users) {
         User user = User.usersMap.get(i);
         VisitorValidation.VALIDATIONRESULT validity = user.visit(userIds);
         if (validity != VisitorValidation.VALIDATIONRESULT.SUCCESS) {
           validationerror.append(validity == VisitorValidation.VALIDATIONRESULT.SPACE_IN_ID ? " User IDs contain Spaces" : "Duplicated Users Ids");
           invalidRunnable.run();
           return;
         }
       }
 
       //Validate User Groups
       HashSet < String > userGroupIds = new HashSet < > ();
       for (String i: UserGroup.userGroups) {
         UserGroup userGroup = UserGroup.userGroupMap.get(i);
         VisitorValidation.VALIDATIONRESULT validity = userGroup.visit(userGroupIds);
         if (validity != VisitorValidation.VALIDATIONRESULT.SUCCESS) {
           validationerror.append(validity == VisitorValidation.VALIDATIONRESULT.SPACE_IN_ID ? "UserGroup Ids contain Spaces" : "Duplicated UserGroup Ids");
           invalidRunnable.run();
           return;
         }
       }
 
       validRunnable.run();
     });
 
     lastUpdatedUser.addActionListener(e -> {
       User lastUpdated = null;
       for (User user: User.usersMap.values())
         if (lastUpdated == null || user.getLastUpdateTime() > lastUpdated.getLastUpdateTime())
           lastUpdated = user;
       if (lastUpdated != null)
         JOptionPane.showMessageDialog(null, "Last Updated User Id is : " + lastUpdated.getUid());
       else
         JOptionPane.showMessageDialog(null, "No Users Found", "No Users Found", JOptionPane.ERROR_MESSAGE);
 
     });
   }
 
   /**
    * Adds the left panel to the main panel.
    * The left panel contains a tree view (`JTree`) displaying the hierarchy of
    * user groups and users. 
    */
 
   private void addLeftPanel() {
     leftPanel = new JPanel();
     leftPanel.setLayout(new BorderLayout());
     mainPanel.add(leftPanel, BorderLayout.WEST);
 
     JLabel jLabel = new JLabel("       Tree View   ", JLabel.CENTER);
     leftPanel.add(jLabel, BorderLayout.NORTH);
     rootTree = new GroupTreeNode(UserGroup.userGroupMap.get("Root").getName());
     populateTreeView(rootTree, UserGroup.userGroupMap.get("Root"));
     jtree = new JTree(rootTree);
     jtree.setCellRenderer(new GroupTreeCellRenderer());
     GroupTreeNode.expandAll(jtree);
     leftPanel.add(jtree, BorderLayout.CENTER);
 
     leftPanel.setPreferredSize(new Dimension(screenSize.width / 3, screenSize.height - 40));
 
     // Select the Root node
     TreePath rootPath = new TreePath(rootTree);
     jtree.setSelectionPath(rootPath);
   }
 
   /**
    * Recursively populates the tree view with user groups and users.
    * 
    * @param rootTree The root node of the tree.
    * @param userGroup The UserGroup object to add to the tree.
    */
 
   private void populateTreeView(DefaultMutableTreeNode rootTree, UserGroup userGroup) {
     if (userGroup == null)
       return;
 
     // Loop through each user or subgroup in the user group
     for (UserMember child: userGroup.getChildren()) {
       DefaultMutableTreeNode tree = null;
       if (child.getChildType() == UserMember.CHILD_TYPE.GROUP || child.getUid().equals("Root")) {
         String title = UserGroup.userGroupMap.get(child.getUid()).getName();
         tree = new GroupTreeNode(title);
         tree.setAllowsChildren(true);
         populateTreeView(tree, UserGroup.userGroupMap.get(child.getUid()));
       } else {
         tree = new DefaultMutableTreeNode(User.usersMap.get(child.getUid()).getName());
       }
       //            tree.setParent(rootTree);
       rootTree.add(tree);
     }
   }
 
 }