/**
 * Visitor interface for implementing the Visitor design pattern
 * to calculate and collect message positivity.
 * 
 * Classes that implement this interface will define a `visit` method
 * that operates on a `List<Double>`, allowing them to collect or 
 * process positivity data from Message objects without modifying 
 * the Message class itself.
 */
import java.util.List;

/**
 * This method is called by Message objects to allow the visitor
 * to perform operations related to message positivity.
 *
 * @param positiveness The list of positivity percentages to be updated 
 *                     or processed by the visitor.
 */

public interface VisitorMessage {
  public void visit(List < Double > positive);
}