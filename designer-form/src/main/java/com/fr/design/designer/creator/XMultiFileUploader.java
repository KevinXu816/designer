/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.properties.mobile.MultiFileUploaderPropertyUI;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.MultiFileEditor;

import com.fr.stable.ArrayUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;

/**
 * @author richer
 * @since 6.5.3
 */
public class XMultiFileUploader extends XFieldEditor {

    public XMultiFileUploader(MultiFileEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
                super.supportedDescriptor(),
                new CRPropertyDescriptor[]{
                        new CRPropertyDescriptor("singleFile", this.data.getClass())
                                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Single_File_Upload"))
                                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
                        new CRPropertyDescriptor("accept", this.data.getClass())
                                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_File_Allow_Upload_Files"))
                                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
                        new CRPropertyDescriptor("maxSize", this.data.getClass())
                                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_File_Size_Limit"))
                                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")
                });
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            JPanel choosePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            choosePane.add(new UITextField(10), BorderLayout.CENTER);
            UIButton btn = new UIButton("...");
            btn.setPreferredSize(new Dimension(24, 24));
            choosePane.add(btn, BorderLayout.EAST);
            editor.add(choosePane, BorderLayout.NORTH);
            JPanel opPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            editor.add(opPane, BorderLayout.CENTER);

            JPanel filePane1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
            opPane.add(filePane1);
            UILabel label1 = new UILabel(BaseUtils.readIcon("com/fr/web/images/form/resources/files_up_delete_16.png"));
            filePane1.add(label1, BorderLayout.WEST);
            filePane1.add(new UILabel("file1.png"), BorderLayout.CENTER);

            JPanel filePane2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
            opPane.add(filePane2);
            UILabel label2 = new UILabel(BaseUtils.readIcon("com/fr/web/images/form/resources/files_up_delete_16.png"));
            filePane2.add(label2, BorderLayout.WEST);
            filePane2.add(new UILabel("file2.xml"), BorderLayout.CENTER);
        }
        return editor;
    }

    @Override
    public Dimension initEditorSize() {
        return MIDDLE_PREFERRED_SIZE;
    }

    /**
     * 该组件是否可以拖入参数面板
     * 这里控制 文件预定义控件在工具栏不显示
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }

    @Override
    protected String getIconName() {
        return "files_up.png";
    }

    @Override
    public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
        return new WidgetPropertyUIProvider[]{ new MultiFileUploaderPropertyUI(this)};
    }
}
