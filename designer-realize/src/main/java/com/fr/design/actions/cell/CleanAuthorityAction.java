package com.fr.design.actions.cell;

import com.fr.design.actions.ElementCaseAction;
import com.fr.design.mainframe.AuthorityPropertyPane;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Author : daisy
 * Date: 13-9-23
 * Time: 下午2:41
 */
public class CleanAuthorityAction extends ElementCaseAction {

    public CleanAuthorityAction(ElementCasePane t) {
        super(t);
//        this.setName(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Clear", "DashBoard-Potence"}));
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Clear_DashBoard_Potence"));
    }


    /**
     * 清楚权限动作
     *
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        ElementCasePane reportPane = getEditingComponent();
        Selection selection = reportPane.getSelection();
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (selection instanceof FloatSelection) {
            String name = ((FloatSelection) selection).getSelectedFloatName();
            TemplateElementCase ec = reportPane.getEditingElementCase();
            FloatElement fe = ec.getFloatElement(name);
            if (fe.isDoneAuthority(selectedRoles)) {
                fe.cleanAuthority(selectedRoles);
            }
            doAfterAuthority(reportPane);
            return;
        }
        CellSelection cellSelection = (CellSelection) selection;
        boolean isChooseColumnRow = cellSelection.getSelectedType() == CellSelection.CHOOSE_COLUMN
                || cellSelection.getSelectedType() == CellSelection.CHOOSE_ROW;
        if (isChooseColumnRow && cellSelection.getCellRectangleCount() == 1) {
            cleanColumnRow(cellSelection, reportPane, selectedRoles);
        } else {
            cleanCell(cellSelection, reportPane, selectedRoles);
        }
        doAfterAuthority(reportPane);
    }


    private void doAfterAuthority(ElementCasePane reportPane) {
        reportPane.repaint();
        reportPane.fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().repaint();
        if (EastRegionContainerPane.getInstance().getUpPane() instanceof AuthorityPropertyPane) {
            AuthorityPropertyPane authorityPropertyPane = (AuthorityPropertyPane) EastRegionContainerPane.getInstance().getUpPane();
            authorityPropertyPane.populate();
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
            EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(authorityPropertyPane);
        }
    }

    /**
     * 清除单元格对应的角色的权限
     *
     * @param cellSelection
     * @param reportPane
     */
    private void cleanCell(CellSelection cellSelection, ElementCasePane reportPane, String selectedRoles) {
        if (selectedRoles == null) {
            return;
        }
        TemplateElementCase elementCase = reportPane.getEditingElementCase();
        int cellRectangleCount = cellSelection.getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = cellSelection.getCellRectangle(rect);
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null) {
                        continue;
                    }
                    //清除权限
                    if (cellElement.isDoneAuthority(selectedRoles) || cellElement.isDoneNewValueAuthority(selectedRoles)) {
                        cellElement.cleanAuthority(selectedRoles);
                    }
                    if (cellElement.getWidget() == null) {
                        continue;
                    }
                    boolean isDoneAuthority = cellElement.getWidget().isDoneVisibleAuthority(selectedRoles) ||
                            cellElement.getWidget().isDoneUsableAuthority(selectedRoles);

                    if (isDoneAuthority) {
                        cellElement.getWidget().cleanAuthority(selectedRoles);
                    }
                }
            }
        }
    }

    private void cleanColumnRow(CellSelection cellSelection, ElementCasePane reportPane, String selectedRoles) {
        if (selectedRoles == null) {
            return;
        }
        TemplateElementCase elementCase = reportPane.getEditingElementCase();
        if (cellSelection.getSelectedType() == CellSelection.CHOOSE_COLUMN) {
            for (int col = cellSelection.getColumn(); col < cellSelection.getColumn() + cellSelection.getColumnSpan(); col++) {
                elementCase.removeColumnPrivilegeControl(col, selectedRoles);
            }
        } else {
            for (int row = cellSelection.getRow(); row < cellSelection.getRow() + cellSelection.getRowSpan(); row++) {
                elementCase.removeRowPrivilegeControl(row, selectedRoles);
            }
        }
    }

    /**
     * 是否需要撤销动作
     *
     * @return 不需要
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        return false;
    }
}