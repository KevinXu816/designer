package com.fr.design.mainframe.errorinfo;

import com.fr.base.FRContext;
import com.fr.general.IOUtils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.core.UUID;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class ErrorInfo {

    private String username;
    private String uuid;
    private String activekey;
    private String uploadtime;
    private String templateid;
    private String logid;
    private String log;

    public ErrorInfo(String username, String uuid, String activekey) {
        this.username = username;
        this.uuid = uuid;
        this.activekey = activekey;
        this.uploadtime = dateToString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getActivekey() {
        return activekey;
    }

    public void setActivekey(String activekey) {
        this.activekey = activekey;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    private String dateToString(){
        DateFormat df = FRContext.getDefaultValues().getDateTimeFormat();
        return df.format(new Date());
    }

    /**
     * 将出错对象存为json字符串, 并放到设计器缓存目录.
     * 等下一次上传到云中心.
     */
    public void saveAsJSON(){
        JSONObject jo = JSONObject.create();
        try {
            jo.put("username", username);
            jo.put("uuid", uuid);
            jo.put("activekey", activekey);
            jo.put("templateid", templateid);
            jo.put("uploadtime", uploadtime);
            jo.put("logid", logid);
            jo.put("log", log);
        } catch (JSONException ignore) {
        }

        saveFileToCache(jo);
    }

    private void saveFileToCache(JSONObject jo) {
        String content = jo.toString();
        String fileName = UUID.randomUUID() + ErrorInfoUploader.SUFFIX;
        File file = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), ErrorInfoUploader.FOLDER_NAME, fileName));
        try {
            StableUtils.makesureFileExist(file);
            FileOutputStream out = new FileOutputStream(file);
            InputStream in = new ByteArrayInputStream(content.getBytes(EncodeConstants.ENCODING_UTF_8));
            IOUtils.copyBinaryTo(in, out);
            out.close();
        } catch (IOException ignore) {
        }
    }
}
