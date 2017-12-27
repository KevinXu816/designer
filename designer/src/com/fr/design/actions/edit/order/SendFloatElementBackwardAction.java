/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit.order;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * Send FloatElement backward.
 */
public class SendFloatElementBackwardAction extends AbstractFloatElementOrderAction {
    /**
     * Constructor
     */
	public SendFloatElementBackwardAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("M_Edit-Send_Backward"));
        this.setMnemonic('B');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, DEFAULT_MODIFIER));
	}

	@Override
	public void orderWithSelectedFloatElement(ElementCase report,
			FloatElement floatElement) {
		report.sendFloatElementBackward(floatElement);
		
	}
}