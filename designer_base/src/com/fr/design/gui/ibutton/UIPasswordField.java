package com.fr.design.gui.ibutton;import com.fr.design.event.UIObserver;import com.fr.design.event.UIObserverListener;import javax.swing.*;/** * Author : Shockway * Date: 13-10-21 * Time: 下午3:23 */public class UIPasswordField extends JPasswordField implements UIObserver {	public void registerChangeListener(UIObserverListener listener) {	}	public boolean shouldResponseChangeListener() {		return false;	}}