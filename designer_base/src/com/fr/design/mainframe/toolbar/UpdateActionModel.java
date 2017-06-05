package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;
import com.fr.stable.pinyin.PinyinFormat;
import com.fr.stable.pinyin.PinyinHelper;

/**
 * Created by XiaXiang on 2017/5/24.
 */

/**
 * action对象
 */
public class UpdateActionModel {
    private static final String SEPARATOR = "/";
    private String parentName;
    private String actionName;
    private String relatedKey;
    private String searchKey;
    private UpdateAction action;

    public UpdateActionModel(String parentName, UpdateAction action) {
        this.parentName = parentName;
        this.action = action;
        this.actionName = action.getName();
        setSearchKey(parentName, action);
    }

    /**
     * 获取搜索关键字，包括上级菜单名，菜单名，以及对应打开面板的所有文字信息（使其能够支持模糊搜索）
     * @param parentName
     * @param action
     */
    private void setSearchKey(String parentName, UpdateAction action) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(parentName).append(PinyinHelper.convertToPinyinString(parentName, SEPARATOR, PinyinFormat.WITHOUT_TONE))
                .append(PinyinHelper.getShortPinyin(parentName))
                .append(actionName).append(PinyinHelper.convertToPinyinString(actionName, SEPARATOR, PinyinFormat.WITHOUT_TONE))
                .append(PinyinHelper.getShortPinyin(actionName)).append(action.getSearchText());
        this.searchKey = buffer.toString();
    }

    /**
     * 获取上一层级菜单name
     * @return
     */
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * 获取action
     * @return
     */
    public UpdateAction getAction() {
        return action;
    }

    public void setAction(UpdateAction action) {
        this.action = action;
    }

    /**
     * 获取actionName
     * @return
     */
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getRelatedKey() {
        return relatedKey;
    }

    public void setRelatedKey(String relatedKey) {
        this.relatedKey = relatedKey;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
