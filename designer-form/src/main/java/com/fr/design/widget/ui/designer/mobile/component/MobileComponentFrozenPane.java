package com.fr.design.widget.ui.designer.mobile.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombocheckbox.UIComboCheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WSortLayout;
import com.fr.stable.ArrayUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hades
 * @date 2019/8/14
 */
public class MobileComponentFrozenPane extends BasicPane {

    private UIComboCheckBox uiComboCheckBox;

    public MobileComponentFrozenPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel frozenLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Component_Frozen"));
        uiComboCheckBox = new UIComboCheckBox(initData());
        JPanel wrapJPanel = UIComponentUtils.wrapWithBorderLayoutPane(uiComboCheckBox);
        Component[][] components = new Component[][]{
                new Component[]{frozenLabel, wrapJPanel}
        };
        JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        centerPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, IntervalConstants.INTERVAL_L5, 10, 0));
        JPanel holder = FRGUIPaneFactory.createBorderLayout_S_Pane();
        holder.add(centerPane, BorderLayout.NORTH);
        this.add(holder, BorderLayout.NORTH);
    }

    private String[] initData() {
        FormDesigner designer =  WidgetPropertyPane.getInstance().getEditingFormDesigner();
        XCreator selectedCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        Widget selectedModel = selectedCreator != null ? selectedCreator.toData() : null;

        if (selectedModel == null || !selectedModel.acceptType(WSortLayout.class)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> widgetList = ((WSortLayout) selectedModel).getNonContainerWidgetList();
        return widgetList.toArray(new String[0]);
    }

    public void update(XCreator xCreator) {
        List<String> selected = new ArrayList<>();
        WSortLayout wSortLayout = ((WSortLayout) xCreator.toData());
        Object[] values = uiComboCheckBox.getSelectedValues();
        for (Object widgetName : values) {
            selected.add((String) widgetName);
        }
        wSortLayout.updateFrozenWidgets(selected);
    }

    public void populate(XCreator xCreator) {
        WSortLayout wSortLayout = ((WSortLayout) xCreator.toData());
        List<String> all = wSortLayout.getNonContainerWidgetList();
        List<String> selected = wSortLayout.getFrozenWidgets();
        Map<Object, Boolean> map = new LinkedHashMap<>();
        for (String value : selected) {
            map.put(value, true);
        }
        all.removeAll(selected);
        for (String value : all) {
            map.put(value, false);
        }
        uiComboCheckBox.setSelectedValues(map);
    }

    @Override
    protected String title4PopupWindow() {
        return "ComponentFrozenPane";
    }
}