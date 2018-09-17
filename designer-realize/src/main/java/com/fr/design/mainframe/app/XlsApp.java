package com.fr.design.mainframe.app;

import com.fr.base.extension.FileExtension;
import com.fr.design.i18n.Toolkit;
import com.fr.exception.PermissionDeniedException;
import com.fr.exception.TplLockedException;
import com.fr.file.FILE;
import com.fr.io.importer.ExcelReportImporter;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;

/**
 * Created by juhaoyu on 2018/6/27.
 */
class XlsApp extends AbstractWorkBookApp {

    @Override
    public String[] defaultExtensions() {

        return new String[]{FileExtension.XLS.getExtension()};
    }

    @Override
    public WorkBook asIOFile(FILE tplFile) {

        WorkBook workbook = null;
        try {
            workbook = new ExcelReportImporter().generateWorkBookByStream(tplFile.asInputStream());
        } catch (PermissionDeniedException exp) {
            FineLoggerFactory.getLogger().error(Toolkit.i18nText("Fine-Design_Basic_Template_Permission_Denied") + tplFile, exp);
        } catch (TplLockedException exp) {
            FineLoggerFactory.getLogger().error(tplFile + Toolkit.i18nText("Fine-Design_Basic_Template_Status_Locked"), exp);
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error("Failed to generate xls from " + tplFile, exp);
        }
        return workbook;
    }
}
