package com.fr.design.mainframe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import com.fr.base.BaseUtils;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.file.FILE;
import com.fr.general.ComparatorUtils;

import com.fr.stable.CodeUtils;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

public class DecodeDialog {

	private UITextField jt;
	private UIButton confirmButton;
	private UILabel hintsLabel;
	private JDialog jd;
	private FILE file;
	private boolean isPwdRight = false;

	public DecodeDialog(final FILE file) {
		this.file = file;
		
		jd = new JDialog();
		jd.setLayout(null);
		UILabel newNameLable = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ECP_Input_Pwd"));
		newNameLable.setBounds(20, 10, 130, 30);
		jt = new UITextField(StringUtils.EMPTY);
		jt.selectAll();
		jt.setBounds(130, 15, 150, 20);
		jd.add(newNameLable);
		jd.add(jt);
		
		hintsLabel = new UILabel();
		hintsLabel.setBounds(20, 50, 250, 30);
		hintsLabel.setForeground(Color.RED);
		hintsLabel.setVisible(false);

		confirmButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"));
		confirmButton.setBounds(180, 90, 60, 25);
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String key = jt.getText();
				if(ComparatorUtils.equals(key, XMLEncryptUtils.getKEY())){
					isPwdRight = true;
					jd.dispose();
					DesignerEnvManager.getEnvManager().setEncryptionKey(CodeUtils.passwordEncode(key));
				}else{
					isPwdRight = false;
					hintsLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ECP_Re_Input"));
					hintsLabel.setVisible(true);
				}
			}
		});

		UIButton cancelButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cancel"));
		cancelButton.setBounds(250, 90, 60, 25);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});

		jd.add(cancelButton);
		jd.add(confirmButton);
		jd.add(hintsLabel);
		jd.setSize(340, 180);
		jd.setModal(true);
		jd.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ECP_Decode"));
		jd.setResizable(false);
		jd.setAlwaysOnTop(true);
		jd.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
		jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GUICoreUtils.centerWindow(jd);
		jd.setVisible(true);
	}

	public boolean isPwdRight() {
		return isPwdRight;
	}
}