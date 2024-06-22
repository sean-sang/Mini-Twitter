/**
 * Represents a member (either a User or a UserGroup) within a UserGroup hierarchy.
 * 
 * This class is a fundamental part of the Composite design pattern, acting as 
 * the Component interface. It defines the common attributes and operations that 
 * both leaf components (User objects) and composite components (UserGroup objects) share.
 */

 public class UserMember {

    private String uid;
  
    /**
     * Indicates the type of the member (either USER or GROUP).
     */
    private CHILD_TYPE childType;
  
    /**
     * Creates a new UserMember object.
     *
     * @param uid       The unique identifier of the member (user or group).
     * @param childType The type of the member (USER or GROUP).
     */
    public UserMember(String uid, CHILD_TYPE childType) {
      this.uid = uid;
      this.childType = childType;
    }
  
    /**
     * Gets the unique identifier (uid) of the member.
     * @return The uid of the member.
     */
    public String getUid() {
      return uid;
    }
  
    /**
     * Sets the unique identifier (uid) of the member.
     * @param uid The new uid for the member.
     */
    public void setUid(String uid) {
      this.uid = uid;
    }
  
    /**
     * Gets the type of the member (USER or GROUP).
     * @return The CHILD_TYPE of the member.
     */
    public CHILD_TYPE getChildType() {
      return childType;
    }
  
    /**
     * Sets the type of the member (USER or GROUP).
     * @param childType The new CHILD_TYPE for the member.
     */
    public void setChildType(CHILD_TYPE childType) {
      this.childType = childType;
    }
  
    /**
     * Enumeration representing the possible types of members: USER or GROUP.
     */
    public static enum CHILD_TYPE {
      USER,
      GROUP
    }
  }