package oog.codechecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

/**
 * This class is used as a listener.
 * @author MINH
 *
 */
public class CCListener implements AuditListener {
    /** Variable for logging. */
    private Logger log = Logger.getLogger("CCListener");
    /** Store the checking results. */
    private Map<String, List<AuditEvent>> resultCheck;
    /** Constructor for resultcheck.
     * @param resultCheck which is the result.
     */
    public CCListener(Map<String, List<AuditEvent>> resultCheck) {
        this.resultCheck = resultCheck;
    }

    /**
     * Used to get resultcheck.
     * @return a resultCheck.
     */
    public Map<String, List<AuditEvent>> getResultCheck() {
        return resultCheck;
    }

    @Override
    public void addError(AuditEvent auditEvent) {
        log.debug("addError.START");
        log.debug("file=" + auditEvent.getFileName()
                + ";line=" + auditEvent.getLine()
                + ";column" + auditEvent.getColumn()
                + ";module=" + auditEvent.getModuleId()
                + ";msg=" + auditEvent.getMessage()
                + ";src name=" + auditEvent.getSourceName()
                + ";source" + auditEvent.getSource());
        String fileName = auditEvent.getFileName();
        List<AuditEvent> errorList = resultCheck.get(fileName);
        if (errorList == null) {
            errorList = new ArrayList<AuditEvent>();
        }
        errorList.add(auditEvent);
        resultCheck.put(fileName, errorList);
    }

    @Override
    public void addException(AuditEvent auditEvent, Throwable arg1) {
        log.debug("addException.START");
        log.debug("file=" + auditEvent.getFileName()
                + ";line=" + auditEvent.getLine()
                + ";column" + auditEvent.getColumn()
                + ";module=" + auditEvent.getModuleId()
                + ";msg=" + auditEvent.getMessage()
                + ";src name=" + auditEvent.getSourceName()
                + ";source" + auditEvent.getSource()
                + ";severity level=" + auditEvent.getSeverityLevel().getName());
        }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
        log.debug("auditFinished.FINISHED");
        log.debug("file=" + auditEvent.getFileName());
    }
    @Override
    public void auditStarted(AuditEvent auditEvent) {
        log.debug("auditStarted.STARTED");
        log.debug("file=" + auditEvent.getFileName());

    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {
        log.debug("fileFinished.FINISHED");
        log.debug("file=" + auditEvent.getFileName());
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {
        log.debug("fileStarted.STARTED");
        log.debug("file=" + auditEvent.getFileName());
    }

}
