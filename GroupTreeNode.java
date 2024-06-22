/**
 * A specialized tree node for representing user groups within the JTree component.
 * Extends DefaultMutableTreeNode to provide specific behavior for group nodes.
 * 
 */

 import javax.swing.*;
 import javax.swing.tree.DefaultMutableTreeNode;
 
 public class GroupTreeNode extends DefaultMutableTreeNode {
 
   private String title;
 
   public GroupTreeNode(String title) {
     super(title);
     this.title = title;
 
   }
 
   @Override
   public boolean isLeaf() {
     return false;
   }
 
   public static void expandAll(JTree tree) {
     expandAllNodes(tree, 0, tree.getRowCount());
 
   }
   private static void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
     for (int i = startingIndex; i < rowCount; ++i) {
       tree.expandRow(i);
     }
 
     if (tree.getRowCount() != rowCount) {
       expandAllNodes(tree, rowCount, tree.getRowCount());
     }
   }
 
 }