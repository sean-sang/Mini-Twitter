/**
 * Represents a user in the Mini Twitter application.
 * Participates in the Composite pattern as a leaf node (individual user) within UserGroups.
 * Implements the VisitorValidation interface for validating user IDs using the Visitor pattern.
 * Employs the Observer pattern to notify UI components of changes in the user list or followers.
 */

 import java.util.*;

 public class User implements VisitorValidation {
 
   // Composite Pattern: List of all users and a map of user IDs to user objects
   public static List < String > users = new ArrayList < > ();
   public static HashMap < String, User > usersMap = new HashMap < > ();
 
   // Observer Pattern: List of observers for list updates and followers update
   public static List < Runnable > listObservers = new ArrayList < > ();
 
   public static List < Runnable > followersObservers = new ArrayList < > ();
 
   private String uid;
   private String name;
 
   private List < String > followers;
 
   private List < String > followings;
 
   private List < String > news;
 
   private String userGroup;
 
   private long creationTime;
 
   private long lastUpdateTime;
 
   /**
    * Creates a new User object with a randomly generated UUID,
    * an empty name, empty lists for followings, followers, and news, 
    * and sets the creation and update time to the current timestamp.
    */
   public User() {
     uid = UUID.randomUUID().toString();
     name = "";
     followers = new ArrayList < > ();
     followings = new ArrayList < > ();
     news = new ArrayList < > ();
     creationTime = System.currentTimeMillis();
     lastUpdateTime = creationTime;
   }
 
   public String getUid() {
     return uid;
   }
 
   public void setUid(String uid) {
     this.uid = uid;
   }
 
   public String getName() {
     return name;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public List < String > getFollowers() {
     return followers;
   }
 
   public void setFollowers(List < String > followers) {
     this.followers = followers;
   }
 
   public List < String > getFollowings() {
     return followings;
   }
 
   public void setFollowings(List < String > followings) {
     this.followings = followings;
   }
 
   public List < String > getNews() {
     return news;
   }
 
   public void setNews(List < String > news) {
     this.news = news;
   }
 
   public String getUserGroup() {
     return userGroup;
   }
 
   public void setUserGroup(String userGroup) {
     this.userGroup = userGroup;
   }
 
   public long getCreationTime() {
     return creationTime;
   }
 
   public void setCreationTime(long creationTime) {
     this.creationTime = creationTime;
   }
 
   public long getLastUpdateTime() {
     return lastUpdateTime;
   }
 
   public void setLastUpdateTime(long lastUpdateTime) {
     this.lastUpdateTime = lastUpdateTime;
   }
 
   /**
    * Adds a new user to the global list of users and the user map.
    * Notifies the `listObservers` to update any UI elements displaying the user list.
    *
    * @param user The User object to add.
    * @return true if the user was added successfully, false otherwise (currently always true).
    */
   public static Boolean addUser(User user) {
     users.add(user.getUid());
     usersMap.put(user.getUid(), user);
     for (Runnable r: listObservers) //notifies observers
       r.run();
     return true;
   }
 
   /**
    * Adds a following relationship between this user and another user.
    * Updates both users' followers and followings lists and notifies the `followersObservers`.
    * 
    * @param user The User object to follow.
    * @return true if the following relationship was added successfully, false if it already exists or if trying to follow oneself.
    */
   public Boolean addFollowing(User user) {
     if (!user.getUid().equals(getUid()) && !followings.contains(user.getUid())) {
       followings.add(user.getUid());
       user.followers.add(getUid());
       for (Runnable r: followersObservers) //notifies observers
         r.run();
       return true;
     } else return false;
   }
 
   /**
    * Adds this user to the specified user group.
    *
    * @param userGroup The UserGroup object to add this user to.
    * @return true if the user was added to the group successfully.
    */
   public Boolean addToGroup(UserGroup userGroup) {
     return userGroup.addUser(this);
   }
   /**
    * Visitor pattern method for validating the user's ID.
    * Checks if the ID contains spaces or is a duplicate of an existing ID.
    *
    * @param usedIds A set of used user IDs.
    * @return A VALIDATIONRESULT indicating the outcome of the validation.
    */
   @Override
   public VALIDATIONRESULT visit(HashSet < String > usedIds) {
     if (getUid().contains(" "))
       return VALIDATIONRESULT.SPACE_IN_ID;
     else if (usedIds.contains(getUid()))
       return VALIDATIONRESULT.DUPLICATED_ID;
     else {
       usedIds.add(getUid());
       return VALIDATIONRESULT.SUCCESS;
     }
   }
 }