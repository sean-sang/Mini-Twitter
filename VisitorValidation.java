/**
 * Visitor interface for implementing the Visitor design pattern to validate object IDs.
 *
 * Classes that implement this interface will define a `visit` method that operates
 * on a `HashSet<String>`, allowing them to check if an ID is valid (e.g., not a duplicate
 * and doesn't contain spaces) without needing to modify the classes being validated (User, UserGroup).
 */
import java.util.HashSet;
/**
 * The core method of the Visitor pattern for validation.
 *
 * @param usedIds A HashSet containing IDs that have already been used.
 * @return A VALIDATIONRESULT enum value indicating the validation outcome:
 *         DUPLICATED_ID if the ID is a duplicate,
 *         SPACE_IN_ID if the ID contains spaces, or
 *         SUCCESS if the ID is valid.
 */
public interface VisitorValidation {
  public VALIDATIONRESULT visit(HashSet < String > usedIds);

  public static enum VALIDATIONRESULT {
    DUPLICATED_ID,
    SPACE_IN_ID,
    SUCCESS
  }
}