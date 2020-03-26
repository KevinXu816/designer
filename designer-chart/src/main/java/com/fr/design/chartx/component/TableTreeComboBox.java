package com.fr.design.chartx.component;

import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.icombobox.UIComboBoxEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.MenuSelectionManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-12-24
 */
public class TableTreeComboBox extends FRTreeComboBox {

    private JTextField textField;

    public TableTreeComboBox(JTree tree, boolean showRoot) {
        super(tree, null, showRoot, false);
        textField = (JTextField) (this.getEditor().getEditorComponent());
        textField.addKeyListener(treeKeyListener);
        tree.setCellRenderer(tableNameTreeRenderer);
    }

    @Override
    protected void dealSamePath(TreePath parent, TreeNode node, UITextField textField) {
        matchLeafNode(parent, node, textField);
    }

    @Override
    public void setSelectedItemString(String name) {
        super.setSelectedItemString(name);
        this.textField.setText(name);
    }

    public void resetText() {
        Object selectedItem = this.getSelectedItem();
        if (!ComparatorUtils.equals(selectedItem, textField.getText())) {
            textField.setText(GeneralUtils.objectToString(selectedItem));
        }
    }

    @Override
    protected UIComboBoxEditor createEditor() {
        return new TableTreeComboBoxEditor(this);
    }

    private boolean matchLeafNode(TreePath parent, TreeNode node, UITextField textField) {
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            TreeNode n = (TreeNode) e.nextElement();
            TreePath path = parent.pathByAddingChild(n);
            TreeNode pathNode = (TreeNode) path.getLastPathComponent();
            if (pathNode.getChildCount() == 0) {
                if (pathToString(path).toUpperCase().contains(textField.getText().toUpperCase())) {
                    tree.scrollPathToVisible(path);
                    tree.setSelectionPath(path);
                    return true;
                }
            } else {
                if (matchLeafNode(path, pathNode, textField)) {
                    return true;
                }
            }
        }
        return false;
    }

    private KeyListener treeKeyListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {
                TreePath treePath = tree.getSelectionPath();
                if (treePath == null) {
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath
                        .getLastPathComponent();
                if (node.isLeaf()) {
                    TableTreeComboBox.this.setSelectedItem(treePath);
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                }
            }
        }
    };

    private TreeCellRenderer tableNameTreeRenderer = new DefaultTreeCellRenderer() {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObj = node.getUserObject();
                if (userObj != null) {
                    this.setText(ChartGEOJSONHelper.getPresentNameWithPath(userObj.toString()));
                }
            }
            return this;
        }
    };

    class TableTreeComboBoxEditor extends FrTreeSearchComboBoxEditor {

        public TableTreeComboBoxEditor(FRTreeComboBox comboBox) {
            super(comboBox);
            textField.addMouseListener(mouseListener);
        }

        private MouseListener mouseListener = new MouseListener() {

            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {
                setPopupVisible(true);
            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        };

    }

}