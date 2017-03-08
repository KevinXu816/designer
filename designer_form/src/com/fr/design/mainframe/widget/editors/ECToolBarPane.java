package com.fr.design.mainframe.widget.editors;

import com.fr.base.BaseUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FSettingToolBar;
import com.fr.design.mainframe.FToolBarButton;
import com.fr.design.mainframe.FToolBarPane;
import com.fr.form.ui.Widget;
import com.fr.form.web.FLocation;
import com.fr.form.web.FToolBarManager;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.*;
import java.util.List;

/**
 * Created by harry on 2017-2-23.
 */
public class ECToolBarPane extends BasicBeanPane<FToolBarManager[]> {
    private static final int COLUMN = 2;
    private int row = 6;
    private DefaultTableModel toolbarButtonTableModel;
    private JTable layoutTable;
    private UICheckBox isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Use_ToolBar") + ":"); // 是否使用工具栏

    private FToolBarPane northToolBar;
    private FToolBarPane southToolBar;
    private FToolBarManager defaultToolBar;


    public ECToolBarPane() {
        toolbarButtonTableModel = new TableModel(row ,COLUMN);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel north = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UIButton defaultButton = new UIButton(Inter.getLocText("FR-Designer_Restore_Default"));
        // 恢复默认按钮
        defaultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                northToolBar.removeButtonList();
                northToolBar.removeAll();
                southToolBar.removeButtonList();
                southToolBar.removeAll();
                if (defaultToolBar == null) {
                    return;
                }
                FToolBarManager toolBarManager = defaultToolBar;
                toolBarManager.setToolBarLocation(FLocation.createTopEmbedLocation());
                FToolBarManager[] tbm = new FToolBarManager[] { toolBarManager };
                populateBean(tbm);
                ECToolBarPane.this.repaint();
            }
        });

        north.add(isUseToolBarCheckBox, BorderLayout.WEST);
        JPanel aa = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        aa.add(defaultButton);
        north.add(aa, BorderLayout.CENTER);
        this.add(north, BorderLayout.NORTH);

        northToolBar = new FToolBarPane();
        northToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
        northToolBar.setBackground(Color.lightGray);

        UIButton topButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_up.png"));
        topButton.setBorder(null);
        topButton.setOpaque(false);
        topButton.setContentAreaFilled(false);
        topButton.setFocusPainted(false);
        topButton.setRequestFocusEnabled(false);
        topButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSelectedtable()) {
                    WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
                    Widget widget = no.createWidget();
                    FToolBarButton tb = new FToolBarButton(no.optionIcon(), widget);
                    tb.setNameOption(no);
                    northToolBar.add(tb);
                    northToolBar.validate();
                    northToolBar.repaint();
                } else {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_ChooseOneButton"));
                }
            }
        });

        UIButton downButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/arrow/arrow_down.png"));
        downButton.setBorder(null);
        downButton.setMargin(null);
        downButton.setOpaque(false);
        downButton.setContentAreaFilled(false);
        downButton.setFocusPainted(false);
        downButton.setRequestFocusEnabled(false);
        downButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSelectedtable()) {
                    WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
                    Widget widget = no.createWidget();
                    FToolBarButton tb = new FToolBarButton(no.optionIcon(), widget);
                    tb.setNameOption(no);
                    southToolBar.add(tb);
                    southToolBar.validate();
                    southToolBar.repaint();
                } else {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_ChooseOneButton"));
                }
            }
        });

        initLayoutTable();

        JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
        center.setBackground(Color.white);
        center.add(topButton, BorderLayout.NORTH);
        JPanel small = FRGUIPaneFactory.createBorderLayout_S_Pane();
        small.setBackground(Color.white);
        small.add(new UILabel(StringUtils.BLANK), BorderLayout.NORTH);
        small.add(layoutTable, BorderLayout.CENTER);
        center.add(small, BorderLayout.CENTER);
        center.add(downButton, BorderLayout.SOUTH);
        southToolBar = new FToolBarPane();
        southToolBar.setPreferredSize(new Dimension(ImageObserver.WIDTH, 26));
        southToolBar.setBackground(Color.lightGray);
        JPanel movePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel northContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        FSettingToolBar top = new FSettingToolBar(Inter.getLocText("FR-Designer_ToolBar_Top"), northToolBar);
        northContentPane.add(top, BorderLayout.EAST);
        northContentPane.add(northToolBar, BorderLayout.CENTER);
        northContentPane.setBackground(Color.lightGray);

        JPanel southContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        FSettingToolBar bottom = new FSettingToolBar(Inter.getLocText("FR-Designer_ToolBar_Bottom"), southToolBar);
        southContentPane.add(bottom, BorderLayout.EAST);
        southContentPane.add(southToolBar, BorderLayout.CENTER);
        southContentPane.setBackground(Color.lightGray);

        movePane.add(northContentPane, BorderLayout.NORTH);
        movePane.add(center, BorderLayout.CENTER);
        movePane.add(southContentPane, BorderLayout.SOUTH);

        this.add(new JScrollPane(movePane), BorderLayout.CENTER);

        isUseToolBarCheckBox.setSelected(false);
    }

    private void initLayoutTable() {
        layoutTable = new JTable(toolbarButtonTableModel);
        layoutTable.setDefaultRenderer(Object.class, tableRenderer);
        layoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layoutTable.setColumnSelectionAllowed(false);
        layoutTable.setRowSelectionAllowed(false);
        layoutTable.setBackground(Color.white);
        int columnWidth = Integer.parseInt(Inter.getLocText("FR-Designer_LayoutTable_Column_Width"));
        for (int i = 0; i < layoutTable.getColumnModel().getColumnCount(); i++) {
            layoutTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
        }
        layoutTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1 && !SwingUtilities.isRightMouseButton(e)) {
                    WidgetOption no = (WidgetOption)layoutTable.getValueAt(layoutTable.getSelectedRow(), layoutTable.getSelectedColumn());
                    Widget widget = no.createWidget();
                    FToolBarButton tb = new FToolBarButton(no.optionIcon(), widget);
                    tb.setNameOption(no);
                    northToolBar.add(tb);
                    northToolBar.validate();
                    northToolBar.repaint();
                }
            }
        });
    }

    private boolean isSelectedtable() {
        for (int i = 0; i < layoutTable.getColumnCount(); i++) {
            if (layoutTable.isColumnSelected(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否被选中
     * @return 同上
     */
    public boolean isUseToolbar() {
        return this.isUseToolBarCheckBox.isSelected();
    }

    public void setDefaultToolBar(FToolBarManager defaultToolBar, WidgetOption[] buttonArray) {
        this.defaultToolBar = defaultToolBar;
        if (buttonArray != null) {
            for (int i = 0; i < buttonArray.length; i++) {
                toolbarButtonTableModel.setValueAt(buttonArray[i], i % row, i / row);
            }
        }

    }

    DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof WidgetOption) {
                WidgetOption nameOption = (WidgetOption)value;
                this.setText(nameOption.optionName());

                Icon icon = nameOption.optionIcon();
                if (icon != null) {
                    this.setIcon(icon);
                }
            }
            if (value == null) {
                this.setText(StringUtils.EMPTY);
                this.setIcon(null);
            }
            return this;
        }
    };

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Form-EC_toolbar", "Set"});
    }

    public void setCheckBoxSelected(boolean b) {
        this.isUseToolBarCheckBox.setSelected(b);
    }

    @Override
    public void populateBean(FToolBarManager[] toolBarManager) {
        if (ArrayUtils.isEmpty(toolBarManager)) {
            defaultToolBar.setToolBarLocation(FLocation.createTopEmbedLocation());
            toolBarManager = new FToolBarManager[] { defaultToolBar };
        }
        if (ArrayUtils.isEmpty(toolBarManager)) {
            return;
        }
        for (int i = 0; i < toolBarManager.length; i++) {
            FLocation location = toolBarManager[i].getToolBarLocation();
            if (location instanceof FLocation.Embed) {
                if (((FLocation.Embed)location).getPosition() == Constants.TOP) {
                    northToolBar.populateBean(toolBarManager[i].getToolBar());
                } else if (((FLocation.Embed)location).getPosition() == Constants.BOTTOM) {
                    southToolBar.populateBean(toolBarManager[i].getToolBar());
                }
            }
        }
    }

    @Override
    public FToolBarManager[] updateBean() {
        if(!isUseToolbar()){
            return new FToolBarManager[0];
        }
        List<FToolBarManager> toolBarManagerList = new ArrayList<FToolBarManager>();
        if (!northToolBar.isEmpty()) {
            FToolBarManager north = new FToolBarManager();
            north.setToolBar(northToolBar.updateBean());
            north.setToolBarLocation(FLocation.createTopEmbedLocation());
            toolBarManagerList.add(north);
        }

        if (!southToolBar.isEmpty()) {
            FToolBarManager south = new FToolBarManager();
            south.setToolBar(southToolBar.updateBean());
            south.setToolBarLocation(FLocation.createBottomEmbedLocation());
            toolBarManagerList.add(south);
        }
        return toolBarManagerList.toArray(new FToolBarManager[toolBarManagerList.size()]);
    }

    private class TableModel extends DefaultTableModel {
        public TableModel(int i, int j) {
            super(i, j);
        }

        // 禁止jtable的双击编辑功能
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
