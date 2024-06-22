/**
 * Represents a group of users in the Mini Twitter application.
 * Participates in the Composite pattern as a composite node (group) containing UserMembers (users or other groups).
 * Implements the VisitorValidation interface for validating group IDs using the Visitor pattern.
 * Employs the Observer pattern to notify UI components of changes in the group list.
 */
import javax.swing.*;
import java.util.*;

public class UserGroup implements VisitorValidation {

  // Composite Pattern: List of all users and a map of user IDs to user objects
  public static List < String > userGroups = new ArrayList < > ();
  public static HashMap < String, UserGroup > userGroupMap = new HashMap < > ();

  // Observer Pattern: Lists of observers for list updates and followers update
  public static List < Runnable > listObservers = new ArrayList < > ();

  /**
   * Creates a "Root" group as the base level class for all
   * users and groups to fall under.
   */

  static {
    if (userGroups.size() == 0) {
      UserGroup userGroup = new UserGroup();
      userGroup.setUid("Root");
      userGroup.setName("Root");
      userGroups.add(userGroup.getUid());
      userGroupMap.put(userGroup.getUid(), userGroup);
    }
  }

  private String uid;

  private String name;

  private List < UserMember > children;

  private long creationTime;

  private long lastUpdateTime;

  /**
   * Creates a new UserGroup object with a randomly generated UUID,
   * an empty name, an empty list of children, and sets creation and update times.
   */
  public UserGroup() {
    this.uid = UUID.randomUUID().toString();
    name = "";
    children = new ArrayList < > ();
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

  public List < UserMember > getChildren() {
    return children;
  }

  public void setChildren(List < UserMember > children) {
    this.children = children;
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
   * Adds a User to this UserGroup if the user doesn't already belong to a group.
   * 
   * @param user The User object to add.
   * @return true if the user was added successfully, false if they already belong to a group.
   */
  public Boolean addUser(User user) {
    if (user.getUserGroup() == null || user.getUserGroup().length() == 0) {
      user.setUserGroup(getUid());
      children.add(new UserMember(user.getUid(), UserMember.CHILD_TYPE.USER));
      for (Runnable r: listObservers)
        r.run();
      return true;
    } else {
      JOptionPane.showMessageDialog(null, "User already in a group :" + user.getUserGroup());
      return false;
    }
  }

  /**
   * Adds a subgroup (another UserGroup) to this UserGroup.
   *
   * @param userGroup The UserGroup to add as a subgroup.
   * @return true (always succeeds for now).
   */
  public Boolean addGroup(UserGroup userGroup) {
    children.add(new UserMember(userGroup.getUid(), UserMember.CHILD_TYPE.GROUP));
    for (Runnable r: listObservers)
      r.run();
    return true;
  }

  /**
   * Adds a new user group to the application.
   * Updates the global list and map of user groups and notifies observers.
   * 
   * @param userGroup The UserGroup object to add.
   */
  public static void addUserGroup(UserGroup userGroup) {
    userGroups.add(userGroup.getUid());
    userGroupMap.put(userGroup.getUid(), userGroup);
    for (Runnable r: listObservers)
      r.run();
  }

  

  /**
   * Visitor pattern method for validating the user group's ID.
   * Checks if the ID contains spaces or is a duplicate of an existing ID.
   *
   * @param userIds A set of used user group IDs.
   * @return A VALIDATIONRESULT indicating the outcome of the validation.
   */
  @Override
  public VALIDATIONRESULT visit(HashSet < String > userIds) {
    if (getUid().contains(" "))
      return VALIDATIONRESULT.SPACE_IN_ID;
    else if (userIds.contains(getUid()))
      return VALIDATIONRESULT.DUPLICATED_ID;
    else {
      userIds.add(getUid());
      return VALIDATIONRESULT.SUCCESS;
    }
  }
}