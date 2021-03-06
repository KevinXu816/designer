package com.fr.van.chart.column;

import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.data.condition.AbstractCondition;
import com.fr.data.condition.ListCondition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.LiteConditionPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.van.chart.designer.style.series.VanChartSeriesConditionPane;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 堆积和坐标轴设置
 */
public class VanChartCustomStackAndAxisConditionPane extends BasicBeanPane<ConditionAttr> {
    private static final long serialVersionUID = 2713124322060048526L;

    protected UIButtonGroup<Integer> XAxis;
    protected UIButtonGroup<Integer> YAxis;
    protected UIButtonGroup<Integer> isStacked;
    protected UIButtonGroup<Integer> isPercentStacked;

    private ConditionAttr conditionAttr;

    private LiteConditionPane liteConditionPane;

    public VanChartCustomStackAndAxisConditionPane() {

    }

    private void doLayoutPane() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.removeAll();

        //配置界面
        JPanel deployPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(deployPane);

        deployPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Deploy") + ":", null));
        deployPane.add(createDeployPane());

        //条件界面
        JPanel conditionPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(conditionPane);
        conditionPane.setBorder(BorderFactory.createEmptyBorder());

        conditionPane.add(liteConditionPane = new VanChartSeriesConditionPane());
        liteConditionPane.setPreferredSize(new Dimension(300, 300));
    }

    private JPanel createDeployPane() {
        isStacked = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_YES"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_NO")});
        isPercentStacked = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_YES"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_NO")});
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p, p};
        double[] rowSize = {p, p, p, p};

        return TableLayoutHelper.createTableLayoutPane(getDeployComponents(), rowSize, columnSize);
    }

    protected Component[][] getDeployComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_X_Axis")), XAxis},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Y_Axis")), YAxis},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stacked")), isStacked},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Percent_Stacked")), isPercentStacked},
        };

        isStacked.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBox();
            }
        });
        return components;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stack_And_Series");
    }

    private void checkBox() {
        isPercentStacked.setEnabled(isStacked.getSelectedIndex() == 0);
    }

    public void populateBean(ConditionAttr conditionAttr) {
        this.conditionAttr = conditionAttr;
        AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis) conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
        XAxis = new UIButtonGroup<Integer>(seriesStackAndAxis.getXAxisNamesArray());
        YAxis = new UIButtonGroup<Integer>(seriesStackAndAxis.getYAxisNameArray());

        doLayoutPane();
        XAxis.setSelectedIndex(seriesStackAndAxis.getXAxisIndex());
        YAxis.setSelectedIndex(seriesStackAndAxis.getYAxisIndex());
        isStacked.setSelectedIndex(seriesStackAndAxis.isStacked() ? 0 : 1);
        isPercentStacked.setSelectedIndex(seriesStackAndAxis.isPercentStacked() ? 0 : 1);

        if (conditionAttr.getCondition() == null) {
            this.liteConditionPane.populateBean(new ListCondition());
        } else {
            this.liteConditionPane.populateBean(conditionAttr.getCondition());
        }

        checkBox();
    }

    protected void updateStackAndPercent(AttrSeriesStackAndAxis seriesStackAndAxis) {
        seriesStackAndAxis.setStacked(isStacked.getSelectedIndex() == 0);
        if (seriesStackAndAxis.isStacked()) {
            seriesStackAndAxis.setPercentStacked(isPercentStacked.getSelectedIndex() == 0);
        } else {
            seriesStackAndAxis.setPercentStacked(false);
        }
    }

    public ConditionAttr updateBean() {
        AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);        seriesStackAndAxis.setXAxisIndex(XAxis.getSelectedIndex());
        seriesStackAndAxis.setYAxisIndex(YAxis.getSelectedIndex());

        updateStackAndPercent(seriesStackAndAxis);
        conditionAttr.addDataSeriesCondition(seriesStackAndAxis);

        AbstractCondition con = (AbstractCondition) this.liteConditionPane.updateBean();
        conditionAttr.setCondition(con);
        return conditionAttr;
    }

}