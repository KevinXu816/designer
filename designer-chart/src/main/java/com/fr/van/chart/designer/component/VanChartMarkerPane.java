package com.fr.van.chart.designer.component;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.marker.VanChartCommonMarkerPane;
import com.fr.van.chart.designer.component.marker.VanChartImageMarkerPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

/**
 * 标记点设置界面
 */
public class VanChartMarkerPane extends BasicPane {
    private static final long serialVersionUID = 7206339620703021514L;
    private UIButtonGroup<String> commonORCustom;
    private JPanel centerPane;
    private CardLayout cardLayout;

    private VanChartCommonMarkerPane commonMarkerPane;
    
    private BasicBeanPane imageMarkerPane;

    public VanChartMarkerPane() {
        this.setLayout(new BorderLayout(0, 4));

        String[] array = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Rule"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom")};
        commonORCustom = new UIButtonGroup<String>(array, array);

        commonORCustom.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkCenterPane();
            }
        });

        commonMarkerPane = createCommonMarkerPane();
        imageMarkerPane = createImageMarkerPane();

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout) {

            @Override
            public Dimension getPreferredSize() {
                if(commonORCustom.getSelectedIndex() == 0){
                    return commonMarkerPane.getPreferredSize();
                } else {
                    return imageMarkerPane.getPreferredSize();
                }
            }
        };
        centerPane.add(commonMarkerPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Rule"));
        centerPane.add(imageMarkerPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom"));

        layoutComponents();
    }

    public void checkLargePlot(boolean large){
        if(large){
            commonORCustom.setSelectedIndex(0);
            checkCenterPane();
        }
        commonORCustom.setEnabled(!large);
    }

    protected BasicBeanPane<VanChartAttrMarker> createImageMarkerPane() {
        return new VanChartImageMarkerPane();
    }

    protected VanChartCommonMarkerPane createCommonMarkerPane() {
        return new VanChartCommonMarkerPane(){
            protected double[] getcolumnSize () {
                double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
                double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
                return new double[] {d, s};
            }
        };
    }

    protected void layoutComponents() {
        this.add(TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Point_Style"), commonORCustom), BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    protected void layoutComponentsWithOutNorth() {
        this.add(centerPane, BorderLayout.CENTER);
    }

    private void checkCenterPane() {
        cardLayout.show(centerPane, commonORCustom.getSelectedItem());
    }

    protected String title4PopupWindow(){
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker");
    }

    public void populate(VanChartAttrMarker marker) {
        if(marker == null){
            marker = new VanChartAttrMarker();
        }
        commonORCustom.setSelectedIndex(marker.isCommon() ? 0 : 1);
        if(marker.isCommon()){
            commonMarkerPane.populateBean(marker);
        } else {
            imageMarkerPane.populateBean(marker);
            commonMarkerPane.setDefaultValue();
        }

        checkCenterPane();
    }

    public VanChartAttrMarker update() {
        VanChartAttrMarker marker = new VanChartAttrMarker();
        if(commonORCustom.getSelectedIndex() == 0){
            commonMarkerPane.updateBean(marker);
        } else {
            imageMarkerPane.updateBean(marker);
        }
        return marker;
    }

}