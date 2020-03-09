package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.ChartConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIColorButtonWithAuto;
import com.fr.design.i18n.Toolkit;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralUtils;

public class ChartTextAttrPaneWithAuto extends ChartTextAttrPane {

    private static final String AUTO = Toolkit.i18nText("Fine-Design_Basic_ChartF_Auto");
    private boolean isFontSizeAuto = false;
    private boolean isColorAuto = false;
    public static String[] FONT_SIZES_WITH_AUTO = new String[FONT_END - FONT_START + 2];
    static {
        FONT_SIZES_WITH_AUTO[0] = AUTO;

        for (int i = 1; i < FONT_SIZES_WITH_AUTO.length; i++) {
            FONT_SIZES_WITH_AUTO[i] = FONT_START + i - 1 + "";
        }
    }

    public ChartTextAttrPaneWithAuto() {
        super();
    }

    public ChartTextAttrPaneWithAuto(boolean isFontSizeAuto, boolean isColorAuto) {
        this.isFontSizeAuto = isFontSizeAuto;
        this.isColorAuto = isColorAuto;

        initState();
        initComponents();
    }

    protected void initFontColorState() {
        setFontColor(isColorAuto ? new UIColorButtonWithAuto(UIConstants.AUTO_FONT_ICON) : new UIColorButton(UIConstants.FONT_ICON));
    }

    protected Object[] getFontSizeComboBoxModel() {
        return isFontSizeAuto ? FONT_SIZES_WITH_AUTO : FONT_SIZES;
    }

    protected float updateFontSize() {
        if (isFontSizeAuto && ComparatorUtils.equals(getFontSizeComboBox().getSelectedItem(), AUTO)) {
            return ChartConstants.AUTO_FONT_SIZE;
        }

        return Float.parseFloat(GeneralUtils.objectToString(getFontSizeComboBox().getSelectedItem()));
    }

    protected void populateFontSize(FRFont frFont) {
        if (getFontSizeComboBox() != null && isFontSizeAuto) {
            if (frFont.getSize() == ChartConstants.AUTO_FONT_SIZE) {
                getFontSizeComboBox().setSelectedItem(AUTO);
            } else {
                getFontSizeComboBox().setSelectedItem(frFont.getSize() + "");
            }
        }

        if (getFontSizeComboBox() != null && !isFontSizeAuto) {
            getFontSizeComboBox().setSelectedItem(frFont.getSize());
        }
    }
}
