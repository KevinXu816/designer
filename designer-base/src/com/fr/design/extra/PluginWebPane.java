package com.fr.design.extra;

import com.fr.base.TemplateUtils;
import com.fr.general.IOUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richie on 16/3/19.
 */
public class PluginWebPane extends JFXPanel {

    private WebEngine webEngine;

    public PluginWebPane(final String installHome, final String mainJs) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                Scene scene = new Scene(root);
                PluginWebPane.this.setScene(scene);
                WebView webView = new WebView();
                webEngine = webView.getEngine();
                try{
                    InputStream inp = IOUtils.readResource(StableUtils.pathJoin(installHome, mainJs));
                    if (inp == null) {
                        throw new IOException("Not found template: " +  mainJs);
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inp, StableUtils.RESOURCE_ENCODER));
                    BufferedReader read = new BufferedReader(reader);
                    StringBuffer sb = new StringBuffer();
                    String line;
                    Map<String, Object> map4Tpl = new HashMap<String, Object>();

                    map4Tpl.put("servletURL", "file:///" + URLEncoder.encode(installHome, "UTF-8"));
                    while ((line = read.readLine()) != null) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(line);
                    }

                    String htmlString = TemplateUtils.renderParameter4Tpl(sb.toString(), map4Tpl);
                    reader.close();
                    inp.close();
                    webEngine.loadContent(htmlString);

                    webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                        @Override
                        public void handle(WebEvent<String> event) {
                            showAlert(event.getData());
                        }
                    });
                    JSObject obj = (JSObject) webEngine.executeScript("window");
                    obj.setMember("PluginHelper", PluginWebBridge.getHelper(webEngine));
                    webView.setContextMenuEnabled(false);//屏蔽右键
                    root.setCenter(webView);
                }catch (Exception e){

                }

            }
        });
    }

    private void showAlert(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(PluginWebPane.this, message);
            }
        });
    }
}
