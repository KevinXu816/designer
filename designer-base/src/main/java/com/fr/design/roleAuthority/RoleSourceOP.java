package com.fr.design.roleAuthority;

import com.fr.general.NameObject;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.gui.itree.refreshabletree.UserObjectOP;


import java.util.*;

/**
 * Author : daisy
 * Date: 13-8-30
 * Time: 下午3:36
 */
public class RoleSourceOP implements UserObjectOP<RoleDataWrapper> {
	private static final int REPORT_PLATEFORM_MANAGE = 0;

	public RoleSourceOP() {
		super();
	}


	public List<Map<String, RoleDataWrapper>> init() {

		//用于存放角色
		List<Map<String, RoleDataWrapper>> allRoles = new ArrayList<Map<String, RoleDataWrapper>>();
		Map<String, RoleDataWrapper> report_roles = new LinkedHashMap<String, RoleDataWrapper>();
		addReportRoles(report_roles);
		allRoles.add(report_roles);

		return allRoles;
	}


	/**
	 * 获取报表平台的角色
	 */
	protected  void addReportRoles(Map<String, RoleDataWrapper> report_roles) {
		RoleDataWrapper tdw = new RoleDataWrapper(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Server_Platform_Manager"));
		report_roles.put(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Role"), tdw);
	}


	public boolean interceptButtonEnabled() {
		return true;
	}

	/**
	 * 移除名字是name的角色
	 *
	 * @param name
	 */
	public void removeAction(String name) {

	}

	/**
	 * 根据不同模式生成子节点
	 *
	 * @return
	 */
	@Override
	public ExpandMutableTreeNode[] load() {
		Map<String, RoleDataWrapper> report_roles = this.init().get(REPORT_PLATEFORM_MANAGE);
		List<ExpandMutableTreeNode> reportlist = new ArrayList<ExpandMutableTreeNode>(); //报表平台橘色
		addNodeToList(report_roles, reportlist);
		return reportlist.toArray(new ExpandMutableTreeNode[reportlist.size()]);
	}

	protected  void addNodeToList(Map<String, RoleDataWrapper> roleMap, List<ExpandMutableTreeNode> roleList) {
		ExpandMutableTreeNode[] roleNode = getNodeArrayFromMap(roleMap);
		for (int i = 0; i < roleNode.length; i++) {
			roleList.add(roleNode[i]);
		}
	}


	protected ExpandMutableTreeNode initReportRolseNode(Map<String, RoleDataWrapper> report_roles) {
		ExpandMutableTreeNode templateNode = new ExpandMutableTreeNode(new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Server_Platform_Manager"), REPORT_PLATEFORM_MANAGE), true);
		templateNode.addChildTreeNodes(getNodeArrayFromMap(report_roles));
		return templateNode;
	}


	protected  ExpandMutableTreeNode[] getNodeArrayFromMap(Map<String, RoleDataWrapper> map) {
		List<ExpandMutableTreeNode> roleList = new ArrayList<ExpandMutableTreeNode>();
		Iterator<Map.Entry<String, RoleDataWrapper>> entryIt = map.entrySet().iterator();
		while (entryIt.hasNext()) {
			Map.Entry<String, RoleDataWrapper> entry = entryIt.next();
			String name = entry.getKey();
			RoleDataWrapper t = entry.getValue();

			ExpandMutableTreeNode newChildTreeNode = new ExpandMutableTreeNode(new NameObject(name, t));
			roleList.add(newChildTreeNode);
			newChildTreeNode.add(new ExpandMutableTreeNode());
		}
		return roleList.toArray(new ExpandMutableTreeNode[roleList.size()]);
	}
}
