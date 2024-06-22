/*
 * Tree cell renderer for the JTree in Mini Twitter.
 * Implements the Strategy pattern to customize the visual representation of tree nodes.
 * Specifically, this renderer distinguishes group nodes from user nodes by setting a 
 * distinct icon (folder icon) for groups.
 */
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class GroupTreeCellRenderer extends DefaultTreeCellRenderer {

  private Icon groupIcon;
  private Icon userIcon;

  public GroupTreeCellRenderer() {
    groupIcon = UIManager.getIcon("FileView.directoryIcon");
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus) {
    Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

    if (value instanceof GroupTreeNode) {
      setIcon(groupIcon);
    }

    return c;
  }
}