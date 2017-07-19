package com.fr.design.gui.style;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.data.core.FormatField;
import com.fr.data.core.FormatField.FormatContents;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Format pane to edit java.text.Format.
 *
 * @author zhou
 * @since 2012-5-24上午10:57:00
 */
public class FormatPane extends AbstractBasicStylePane{
    private static final long serialVersionUID = 724330854437726751L;

    private static final int LABLE_X = 4;
    private static final int LABLE_Y = 18;
    private static final int LABLE_DELTA_WIDTH = 8;
    private static final int LABLE_HEIGHT = 15; //标签背景的范围
    private static final int CURRENCY_FLAG_POINT = 6;

    private static final Integer[] TYPES = new Integer[]{FormatContents.NULL, FormatContents.NUMBER, FormatContents.CURRENCY, FormatContents.PERCENT, FormatContents.SCIENTIFIC,
            FormatContents.DATE, FormatContents.TIME, FormatContents.TEXT};

    private static final Integer[] DATETYPES = new Integer[]{FormatContents.NULL, FormatContents.DATE, FormatContents.TIME,};

    private Format format;

    private UIComboBox typeComboBox;
    private UIComboBox textField;
    private UILabel sampleLabel;
    private JPanel contentPane;
    private JPanel txtCenterPane;
    private JPanel centerPane;
    private JPanel formatFontPane;
    private FRFontPane frFontPane;
    private boolean isRightFormate;
    private boolean isDate = false;
    private boolean isFormat = false;
    /**
     * Constructor.
     */
    public FormatPane() {
        this.initComponents(TYPES);
    }

    public static void main(String[] args){
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new FormatPane(), BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(290, 400);
        jf.setVisible(true);
    }

    protected void initComponents(Integer[] types) {
        this.setLayout(new BorderLayout(0, 4));
        iniSampleLable();
        contentPane = new JPanel(new BorderLayout(0, 4)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 70);
            }
        };
        typeComboBox = new UIComboBox(types);
        UIComboBoxRenderer render = createComBoxRender();
        typeComboBox.setRenderer(render);
        typeComboBox.addItemListener(itemListener);
        contentPane.add(sampleLabel, BorderLayout.NORTH);
        centerPane = new JPanel(new CardLayout());
        centerPane.add(new JPanel(), "hide");
        centerPane.setPreferredSize(new Dimension(0, 0) );
        centerPane.add(contentPane, "show");
        formatFontPane = new JPanel(new BorderLayout());
        formatFontPane.add(centerPane, BorderLayout.NORTH);
        formatFontPane.add(new FRFontPane(), BorderLayout.CENTER);
        txtCenterPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.add(txtCenterPane, BorderLayout.CENTER);
        textField = new UIComboBox(FormatField.getInstance().getFormatArray(getFormatContents()));
        textField.addItemListener(textFieldItemListener);
        textField.setEditable(true);
        txtCenterPane.add(textField, BorderLayout.NORTH);
        frFontPane = new FRFontPane();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("FR-Base_Format")+"   ", SwingConstants.LEFT), typeComboBox },
                new Component[]{null,centerPane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_FRFont"), SwingConstants.LEFT), frFontPane},
                new Component[]{null,null}
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1},{1, 1}, {1, 1}, {1, 3}, {1, 1}};
        JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_MEDIUM);
        this.add(panel,BorderLayout.CENTER);
    }

    protected UIComboBoxRenderer createComBoxRender(){
        return new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Integer) {
                    label.setText(" " + FormatField.getInstance().getName((Integer) value));
                }
                return label;
            }
        };
    }

    private void iniSampleLable() {
        Border innterborder = new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 4);
        Font tmpFont = null;
        Border border = BorderFactory.createTitledBorder(innterborder, Inter.getLocText("FR-Base_StyleFormat_Sample"), TitledBorder.LEFT, 0, tmpFont, UIConstants.LINE_COLOR);
        sampleLabel = new UILabel(FormatField.getInstance().getFormatValue()) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                int width = getWidth();
                Color orignal = g.getColor();
                g.setColor(getBackground());
                g.fillRect(LABLE_X, LABLE_Y, width - LABLE_DELTA_WIDTH, LABLE_HEIGHT);
                g.setColor(UIConstants.LINE_COLOR);
                FontMetrics cellFM = g.getFontMetrics();
                int textWidth = cellFM.stringWidth(getText());
                GraphHelper.drawString(g, getText(), (width - textWidth) / 2, 26);
                g.setColor(orignal);
            }
        };
        sampleLabel.setHorizontalAlignment(UILabel.CENTER);
        sampleLabel.setBorder(border);
    }


    @Override
    /**
     * 得到合适的大小
     */
    public Dimension getPreferredSize() {
        if (this.typeComboBox.getSelectedIndex() == FormatContents.NULL) {
            return typeComboBox.getPreferredSize();
        }
        return super.getPreferredSize();
    }

    /**
     * 弹出框标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Base_Format");
    }

    /**
     * Populate
     */
    public void populateBean(Format format) {
        this.format = format;

        if (format == null) {
            this.typeComboBox.setSelectedIndex(FormatContents.NULL);
        } else {
            if (format instanceof CoreDecimalFormat) {
                // check all value
                String pattern = ((CoreDecimalFormat) format).toPattern();
                boolean isCurrency1 = (pattern.length() > 0 && pattern.charAt(0) == '¤');
                boolean isCurrency2 = (pattern.length() > 0 && pattern.charAt(0) == '$');
                boolean isCurrency = isCurrency1 || isCurrency2;
                boolean isCurrency4 = (pattern.length() > CURRENCY_FLAG_POINT && ComparatorUtils.equals(pattern.substring(0, CURRENCY_FLAG_POINT), "#,##0;"));
                if (isCurrency || isCurrency4) {
                    setPatternComboBoxAndList(FormatContents.CURRENCY, pattern);
                } else if (pattern.endsWith("%")) {
                    setPatternComboBoxAndList(FormatContents.PERCENT, pattern);
                } else if (pattern.indexOf("E") > 0) {
                    setPatternComboBoxAndList(FormatContents.SCIENTIFIC, pattern);
                } else {
                    setPatternComboBoxAndList(FormatContents.NUMBER, pattern);
                }
            } else if (format instanceof SimpleDateFormat) { // date and time
                String pattern = ((SimpleDateFormat) format).toPattern();
                if (!isTimeType(pattern)) {
                    setPatternComboBoxAndList(FormatContents.DATE, pattern);
                } else {
                    setPatternComboBoxAndList(FormatContents.TIME, pattern);
                }
            } else if (format instanceof TextFormat) { // Text
                this.typeComboBox.setSelectedItem(FormatContents.TEXT);
            }
        }
    }


    /**
     * 判断是否是数组有模式
     *
     * @param stringArray 字符串数组
     * @param pattern   格式
     * @return  是否是数组有模式
     */
    public static int isArrayContainPattern(String[] stringArray, String pattern) {
        for (int i = 0; i < stringArray.length; i++) {
            if (ComparatorUtils.equals(stringArray[i], pattern)) {
                return i;
            }
        }

        return -1;
    }

    private void setPatternComboBoxAndList(int formatStyle, String pattern) {
        this.typeComboBox.setSelectedItem(formatStyle);
        int i = isArrayContainPattern(FormatField.getInstance().getFormatArray(formatStyle), pattern);
        if (i == -1) {
            this.textField.setSelectedIndex(0);
        } else {
            this.textField.setSelectedIndex(i);
        }
    }

    private boolean isTimeType(String pattern) {
        return pattern.matches(".*[Hhmsa].*");
    }

    /**
     * update
     */
    public Format update() {
        String patternString = String.valueOf(textField.getSelectedItem());
        if (getFormatContents() == FormatContents.TEXT) {
            return FormatField.getInstance().getFormat(getFormatContents(), patternString);
        }
        if (isRightFormate) {
            if (StringUtils.isNotEmpty(patternString)) {
                return FormatField.getInstance().getFormat(getFormatContents(), patternString);
            }
        }
        return null;
    }

    private int getFormatContents() {
        return (Integer) typeComboBox.getSelectedItem();
    }

    /**
     * Refresh preview label.
     */
    private void refreshPreviewLabel() {
        this.sampleLabel.setText(FormatField.getInstance().getFormatValue());
        this.sampleLabel.setForeground(UIManager.getColor("Label.foreground"));
        try {
            isRightFormate = true;
            if (StringUtils.isEmpty(String.valueOf(textField.getSelectedItem()))) {
                return;
            }
            this.sampleLabel.setText(FormatField.getInstance().getFormatValue(getFormatContents(), String.valueOf(textField.getSelectedItem())));
        } catch (Exception e) {
            this.sampleLabel.setForeground(Color.red);
            this.sampleLabel.setText(e.getMessage());
            isRightFormate = false;
        }
    }

    private boolean isTextOrNull() {
        int contents = getFormatContents();
        return contents == FormatContents.TEXT || contents == FormatContents.NULL;
    }

    /**
     * Radio selection listener.
     */
    ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                int contents = getFormatContents();
                String[] items = FormatField.getInstance().getFormatArray(contents);
                CardLayout cardLayout = (CardLayout) centerPane.getLayout();

                if (isTextOrNull()) {
                    centerPane.setPreferredSize(new Dimension(0, 0) );
                    cardLayout.show(centerPane, "hide");
                } else {
                    textField.removeAllItems();
                    for (int i = 0; i < items.length; i++) {
                        textField.addItem(items[i]);
                    }
                    centerPane.setPreferredSize(new Dimension(270, 70) );
                    cardLayout.show(centerPane, "show");
                }
                isFormat = true;
            }

        }
    };

    ItemListener textFieldItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                isFormat = true;
                refreshPreviewLabel();
            }
        }
    };

    @Override
    /**
     * populate
     */
    public void populateBean(Style style) {
        this.populateBean(style.getFormat());
        isFormat = false;
        this.frFontPane.populateBean(style.getFRFont());
    }

    @Override
    /**
     * update
     */
    public Style update(Style style) {
        if (isFormat){
            isFormat = false;
            return style.deriveFormat(this.update());
        } else {
            return style.deriveFRFont(this.frFontPane.update(style.getFRFont()));
        }
    }

    /**
     * 默认只显示百分比的编辑下拉.
     */
    public void justUsePercentFormat() {
        typeComboBox.setEnabled(false);
        this.typeComboBox.setSelectedItem(FormatContents.PERCENT);
    }

    public void setForDataSheet() {
        Integer[] otherTypes = new Integer[]{FormatContents.NULL, FormatContents.NUMBER, FormatContents.CURRENCY, FormatContents.PERCENT, FormatContents.SCIENTIFIC,};
        this.typeComboBox = new UIComboBox(otherTypes);
        UIComboBoxRenderer render = new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Integer) {
                    label.setText(" " + FormatField.getInstance().getName((Integer) value));
                }
                return label;
            }
        };
        typeComboBox.setRenderer(render);
        typeComboBox.addItemListener(itemListener);
        this.add(typeComboBox, BorderLayout.NORTH);
    }

    public void setComboBoxModel(boolean isDate) {
        if (this.isDate != isDate) {
            this.isDate = isDate;
            this.typeComboBox.setSelectedIndex(0);
            if (isDate) {
                for (int i = 0; i < DATETYPES.length; i++) {
                    this.typeComboBox.addItem(DATETYPES[i]);
                }
                for (int i = 0; i < TYPES.length; i++) {
                    this.typeComboBox.removeItemAt(1);
                }
            } else {
                for (int i = 0; i < TYPES.length; i++) {
                    this.typeComboBox.addItem(TYPES[i]);
                }
                for (int i = 0; i < DATETYPES.length; i++) {
                    this.typeComboBox.removeItemAt(1);
                }
            }
        }
    }

}