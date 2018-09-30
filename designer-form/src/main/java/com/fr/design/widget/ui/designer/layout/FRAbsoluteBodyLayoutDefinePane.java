package com.fr.design.widget.ui.designer.layout;

import com.fr.base.io.IOFile;
import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.properties.items.FRLayoutTypeItems;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.accessibles.AccessibleBodyWatermarkEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleWLayoutBorderStyleEditor;
import com.fr.design.widget.ui.designer.component.WidgetBoundPane;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.WAbsoluteBodyLayout;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.log.FineLoggerFactory;

import com.fr.report.core.ReportUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by ibm on 2017/8/2.
 */
public class FRAbsoluteBodyLayoutDefinePane extends FRAbsoluteLayoutDefinePane {
    private static final int EACH_ROW_COUNT = 4;

    private AccessibleWLayoutBorderStyleEditor borderStyleEditor;
    private AccessibleBodyWatermarkEditor watermarkEditor;
    private WidgetBoundPane boundPane;

    private UIComboBox layoutCombox;
    private WBodyLayoutType layoutType = WBodyLayoutType.ABSOLUTE;

    public FRAbsoluteBodyLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
    }


    public void initComponent() {
        super.initComponent();
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        boundPane = new WidgetBoundPane(creator);
        centerPane.add(boundPane, BorderLayout.CENTER);
        borderStyleEditor = new AccessibleWLayoutBorderStyleEditor();
        watermarkEditor = new AccessibleBodyWatermarkEditor();
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{
                        new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style")), borderStyleEditor},
                        new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark")), watermarkEditor}
                }, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);
        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        borderPane.add(jPanel, BorderLayout.CENTER);
        UIExpandablePane advancedPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, borderPane);
        centerPane.add(advancedPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.NORTH);
    }

    public JPanel createThirdPane() {
        initLayoutComboBox();
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel northPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Attr_Layout_Type")), layoutCombox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Scaling_Mode")), comboBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        jPanel.add(northPane, BorderLayout.NORTH);
        jPanel.add(centerPane, BorderLayout.CENTER);
        centerPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, IntervalConstants.INTERVAL_L5, 0, 0));
        return jPanel;

    }

    public void initLayoutComboBox() {
        Item[] items = FRLayoutTypeItems.ITEMS;
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Item item : items) {
            model.addElement(item);
        }
        layoutCombox = new UIComboBox(model);
        layoutCombox.setSelectedIndex(1);
    }

    @Override
    public String title4PopupWindow() {
        return "absoluteBodyLayout";
    }

    public void populateSubPane(WAbsoluteLayout ob) {
        layoutCombox.setSelectedIndex(1);
        borderStyleEditor.setValue(ob.getBorderStyle());
        boundPane.populate();
        watermarkEditor.setValue(ReportUtils.getWatermarkFromAttrMarkFile(getCurrentIOFile()));

    }

    public WAbsoluteBodyLayout updateSubPane() {
        WAbsoluteBodyLayout layout = (WAbsoluteBodyLayout) creator.toData();
        boundPane.update();
        Item item = (Item) layoutCombox.getSelectedItem();
        Object value = item.getValue();
        int state = 0;
        if (value instanceof Integer) {
            state = (Integer) value;
        }

        if (layoutType == WBodyLayoutType.ABSOLUTE) {
            ((XWFitLayout) creator.getBackupParent()).toData().resetStyle();
            if (state == WBodyLayoutType.FIT.getTypeValue()) {
                XWFitLayout.switch2FitBodyLayout(creator);
            }
        }
        layout.setBorderStyle((LayoutBorderStyle) borderStyleEditor.getValue());
        updateWatermark();
        return layout;
    }

    private void updateWatermark() {
        WatermarkAttr watermark = (WatermarkAttr) watermarkEditor.getValue();
        if (watermark != null) {
            IOFile ioFile = getCurrentIOFile();
            ioFile.addAttrMark(watermark);
        }
    }

    private IOFile getCurrentIOFile() {
        return WidgetPropertyPane.getInstance().getEditingFormDesigner().getTarget();
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }


}
