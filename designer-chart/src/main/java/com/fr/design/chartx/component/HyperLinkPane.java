package com.fr.design.chartx.component;

import com.fr.base.BaseFormula;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.NameObject;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormHyperlinkProvider;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ListMap;
import com.fr.stable.Nameable;
import com.fr.stable.bridge.StableFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.Component;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-09-25
 */
public class HyperLinkPane extends UIListControlPane implements UIObserver {

    private Map<String, BaseFormula> hyperLinkEditorMap;

    private UIObserverListener uiObserverListener;

    public HyperLinkPane() {
        super();
        this.setBorder(null);
    }

    @Override
    public NameableCreator[] createNameableCreators() {
        //面板初始化，需要在populate的时候更新
        Map<String, NameableCreator> nameCreators = new ListMap<String, NameableCreator>();
        NameableCreator[] creators = DesignModuleFactory.getHyperlinkGroupType().getHyperlinkCreators();
        for (NameableCreator creator : creators) {
            nameCreators.put(creator.menuName(), creator);
        }
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            nameCreators.put(nc.menuName(), nc);
        }
        return nameCreators.values().toArray(new NameableCreator[nameCreators.size()]);
    }

    public BasicBeanPane createPaneByCreators(NameableCreator creator) {
        Constructor<? extends BasicBeanPane> constructor;
        try {
            constructor = creator.getUpdatePane().getConstructor(HashMap.class, boolean.class);
            return constructor.newInstance(hyperLinkEditorMap, false);

        } catch (InstantiationException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            return super.createPaneByCreators(creator);
        } catch (InvocationTargetException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    @Override
    protected JPanel getLeftTopPane(UIToolbar topToolBar) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(getAddItemText()), topToolBar},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    public void saveSettings() {
        if (isPopulating) {
            return;
        }
        uiObserverListener.doChange();
    }

    public void update(NameJavaScriptGroup nameGroup) {

        Nameable[] nameables = update();

        nameGroup.clear();

        for (int i = 0; i < nameables.length; i++) {
            JavaScript javaScript = (JavaScript) ((NameObject) nameables[i]).getObject();
            String name = nameables[i].getName();
            NameJavaScript nameJava = new NameJavaScript(name, javaScript);
            nameGroup.addNameHyperlink(nameJava);
        }

    }

    public void populate(NameJavaScriptGroup nameGroup, Map<String, BaseFormula> hyperLinkEditorMap) {

        this.hyperLinkEditorMap = hyperLinkEditorMap;

        Map paneMap = getHyperlinkMap();

        //安装平台内打开插件时,添加相应按钮
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            paneMap.put(nc.getHyperlink(), nc.getUpdatePane());
        }

        List<UIMenuNameableCreator> list = refreshList(paneMap);
        NameObjectCreator[] creators = new NameObjectCreator[list.size()];
        for (int i = 0; list != null && i < list.size(); i++) {
            UIMenuNameableCreator uiMenuNameableCreator = list.get(i);
            creators[i] = new NameObjectCreator(uiMenuNameableCreator.getName(), uiMenuNameableCreator.getObj().getClass(), uiMenuNameableCreator.getPaneClazz());

        }

        refreshNameableCreator(creators);

        List<NameObject> nameObjects = new ArrayList<NameObject>();

        for (int i = 0; nameGroup != null && i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if (javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                UIMenuNameableCreator uiMenuNameableCreator = new UIMenuNameableCreator(javaScript.getName(), script, getUseMap(paneMap, script.getClass()));
                nameObjects.add(new NameObject(uiMenuNameableCreator.getName(), uiMenuNameableCreator.getObj()));

            }
        }

        this.populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }

    private Map getHyperlinkMap() {
        Map<Class, Class> map = new HashMap<Class, Class>();

        map.put(ReportletHyperlink.class, ReportletHyperlinkPane.class);
        map.put(EmailJavaScript.class, ChartEmailPane.class);
        map.put(WebHyperlink.class, WebHyperlinkPane.class);
        map.put(ParameterJavaScript.class, ParameterJavaScriptPane.class);

        map.put(JavaScriptImpl.class, JavaScriptImplPane.class);
        map.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.class);
        map.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.class);
        map.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.class);

        map.put(FormHyperlinkProvider.class, FormHyperlinkPane.class);
        return map;
    }

    private List<UIMenuNameableCreator> refreshList(Map map) {
        List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Reportlet"),
                new ReportletHyperlink(), getUseMap(map, ReportletHyperlink.class)));
        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Mail"), new EmailJavaScript(), NewChartEmailPane.class));
        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Web"),
                new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Dynamic_Parameters"),
                new ParameterJavaScript(), getUseMap(map, ParameterJavaScript.class)));
        list.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));

        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Float_Chart"),
                new ChartHyperPoplink(), getUseMap(map, ChartHyperPoplink.class)));
        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Cell"),
                new ChartHyperRelateCellLink(), getUseMap(map, ChartHyperRelateCellLink.class)));
        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Float"),
                new ChartHyperRelateFloatLink(), getUseMap(map, ChartHyperRelateFloatLink.class)));

        FormHyperlinkProvider hyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        list.add(new UIMenuNameableCreator(Toolkit.i18nText("Fine-Design_Chart_Link_Form"),
                hyperlink, getUseMap(map, FormHyperlinkProvider.class)));

        return list;
    }

    private Class<? extends BasicBeanPane> getUseMap(Map map, Object key) {
        if (map.get(key) != null) {
            return (Class<? extends BasicBeanPane>) map.get(key);
        }
        //引擎在这边放了个provider,当前表单对象
        for (Object tempKey : map.keySet()) {
            if (((Class) tempKey).isAssignableFrom((Class) key)) {
                return (Class<? extends BasicBeanPane>) map.get(tempKey);
            }
        }
        return null;
    }

    /**
     * 弹出列表的标题.
     *
     * @return 返回标题字符串.
     */
    @Override
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Report_Hyperlink");
    }

    @Override
    protected String getAddItemText() {
        return Toolkit.i18nText("Fine-Design_Basic_Add_Hyperlink");
    }

    //邮箱
    public static class NewChartEmailPane extends ChartEmailPane {
        @Override
        protected boolean needRenamePane() {
            return false;
        }
    }
}
