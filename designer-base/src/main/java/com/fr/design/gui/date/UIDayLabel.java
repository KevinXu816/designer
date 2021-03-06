package com.fr.design.gui.date;

import com.fr.design.gui.ilable.UILabel;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;



public class UIDayLabel extends UILabel {

    private Date date = null;

    /**
     * 日期格式（TODAY/TIP用）
     */
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    /**
     * 日格式
     */
    final SimpleDateFormat dayFormat = new SimpleDateFormat("d");

    public UIDayLabel(Date date) {
        this(date, true);
    }

    public UIDayLabel(Date date, boolean isSmallLabel) {
        setHorizontalAlignment(UILabel.CENTER);
        setFont(new Font(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Song_TypeFace"), 0, 12));
        this.date = date;
        setPreferredSize(new Dimension(30, 18));
        if (isSmallLabel) {
            setText(dayFormat.format(date));
        } else {
            setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Today")+":" + dateFormat.format(new Date()));
        }
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}