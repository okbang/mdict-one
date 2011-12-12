/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package openones.svnloader.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openones.svnloader.dao.DaoManager;
import openones.svnloader.dao.IDirManager;
import openones.svnloader.dao.IRevisionManager;
import openones.svnloader.dao.ISVNFileManager;
import openones.svnloader.dao.ISVNRepoManager;
import openones.svnloader.dao.ISVNVersionManager;
import openones.svnloader.dao.entity.IDir;
import openones.svnloader.dao.entity.IRevision;
import openones.svnloader.dao.entity.ISVNFile;
import openones.svnloader.dao.entity.ISVNFilePK;
import openones.svnloader.dao.entity.ISVNRepo;
import openones.svnloader.dao.entity.ISVNVersion;
import openones.svnloader.dao.entity.ISVNVersionPK;
import openones.svnloader.tools.CodeCheckerUtil;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;

import rocky.common.CommonUtil;
import rocky.sizecounter.ISizeCounter;
import rocky.sizecounter.SizeCounterFactory;
import rocky.sizecounter.SizeMetaData;
import rocky.sizecounter.UnsupportedFileType;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

public class SVNLoaderBiz {
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SVNLoaderBiz.class);
    private final long startRevision = 0;
    private final long endRevision = -1; // head

    private String svnUrl;
    private String svnUsername;
    private String svnPassword;
    private String projectCode;

    /**
     * index indicated that it will begin reporting at this revision Set this variable at constructor
     */
    private long svnStartRevision;

    DaoManager daoManager = DaoManager.getInstance();

    private ISVNRepoManager svnRepoManager = daoManager.newSVNRepoManagerInst();
    private IDirManager dirManager = daoManager.newDirManagerInst();
    private ISVNFileManager svnFileManager = daoManager.newSVNFileManagerInst();
    private ISVNVersionManager svnVersionManager = daoManager.newSVNVersionManagerInst();
    private IRevisionManager revisionManager = daoManager.newRevisionManagerInst();
    
    /** SVNRepository. */
    private SVNRepository repository;
    /** Temporary path. */
    private String tempPath;

    /** SVNRepo. */
    private ISVNRepo svnRepoRoot;
    
    /** Dir. */
    private IDir dirRoot;
    ISizeCounter counter = SizeCounterFactory.getDefaultInstance();

    public SVNLoaderBiz(String svnUrl, String svnUsername, String svnPassword, String workingCopyPath, String projectCode,
            long svnStartRevision) {
        super();
        this.svnUrl = svnUrl;
        this.svnUsername = svnUsername;
        this.svnPassword = svnPassword;
        this.tempPath = SVNUtility.trimCharAtEnd(workingCopyPath, File.separatorChar);
        this.projectCode = projectCode;
        this.svnStartRevision = svnStartRevision;

//        svnRepoManager = new SVNRepoManager();
//        dirManager = new DirManager();
//        svnFileManager = new SVNFileManager();
//        svnVersionManager = new SVNVersionManager();
//        revisionManager = new RevisionManager();

        // omit character "/" at url
        this.svnUrl = SVNUtility.trimCharAtEnd(this.svnUrl, '/');
        SVNUtility.setupLibrary();
        try {
            // Create the temp folder
            File tempPathFile = new File(tempPath);
            if (tempPathFile.isDirectory() && tempPathFile.exists()) {
                LOGGER.debug("The temp folder is existed.");
            } else if (!tempPathFile.mkdirs()) {
                LOGGER.error("Could not create temp folder '" + tempPath);
            }
            // set up repository
            repository = SVNUtility.buildSVNRepository(this.svnUrl, this.svnUsername, this.svnPassword);
        } catch (Exception ex) {
            StringBuilder builder = new StringBuilder();
            builder.append(ex.getMessage() + "\n");
            StackTraceElement[] listError = ex.getStackTrace();
            for (StackTraceElement stackTraceElement : listError) {
                builder.append(stackTraceElement.toString() + "\n");
            }
            LOGGER.error(builder.toString());
            System.exit(0);
        }
    }

    /**
     * get all revision a url
     * 
     * @return list Revision
     */
    public java.util.List<Long> getAllRevision() {
        Collection logEntries = null;
        List<Long> listRevision = new ArrayList<Long>();
        try {
            logEntries = repository.log(new String[] { "" }, null, startRevision, endRevision, true, true);
            Iterator entries = logEntries.iterator();
            while (entries.hasNext()) {
                SVNLogEntry logEntry = (SVNLogEntry) entries.next();
                listRevision.add(logEntry.getRevision());
            }
        } catch (Exception ex) {
            StringBuilder builder = new StringBuilder();
            builder.append("Error when get all revision");
            builder.append(ex.getMessage() + "\n");
            StackTraceElement[] listError = ex.getStackTrace();
            for (StackTraceElement stackTraceElement : listError) {
                builder.append(stackTraceElement.toString() + "\n");
            }
            LOGGER.error(builder.toString());
            System.exit(0);
        }
        return listRevision;
    }

    /**
     * export source form SVN Server to local with a dir path
     * 
     * @param path
     * @param revision
     * @throws Exception
     */
    public void exportSVN(String path, long revisionNum) throws Exception {
        SVNRepository repo;
        File exportDir = new File(path);
        if (exportDir.exists()) {

            SVNUtility.deletePath(path);
        }
        exportDir.mkdirs();
        repo = SVNUtility.buildSVNRepository(this.svnUrl, this.svnUsername, this.svnPassword);
        SVNNodeKind nodeKind = repo.checkPath("", revisionNum);
//        LOGGER.debug("SVNNodeKind: " + nodeKind);
        if (nodeKind == SVNNodeKind.NONE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL ''{0}''", this.svnUrl);            
            throw new SVNException(err);
        } else if (nodeKind == SVNNodeKind.FILE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
                    "Entry at URL '{0}' is a file while directory was expected", this.svnUrl);            
            throw new SVNException(err);
        }
        
        //The ISVNReporterBaton interface should be implemented 
        //by callers of update, checkout, etc. operations of 
        //SVNRepository drivers in order to describe the state of local items.
        ISVNReporterBaton reporterBaton = new ExportReporterBaton(revisionNum);
        
        ISVNEditor exportEditor = new ExportEditor(exportDir);
        try {
            repo.update(revisionNum, null, true, reporterBaton, exportEditor);

        } catch (final java.lang.OutOfMemoryError e) {
            throw new Exception("Error: Out of Memory when expot svn at svn: "
                    + repository.getLocation().toDecodedString() + ", Revision: " + Long.toString(revisionNum));
        }
    }

    /**
     * Load data from SVN repository (svnURL into relationship database (JPA)
     * @throws Exception 
     * @throws PreexistingEntityException 
     * 
     */
    public void transfer() {
        Collection logEntries = null;
        long startRev = startRevision;
        boolean newRepo = true;
        //EntityManager em = PersistentManager.getEntityManager();
        try {
            LOGGER.info("Transfer for URL: " + this.svnUrl);
            // check if url is exist in database
            // get latest revision for continue update at latest revision updated
            // SVNRepo 
            ISVNRepo repoItem = svnRepoManager.findRepoByURL(this.svnUrl);
            if (repoItem != null) {
                svnRepoRoot = repoItem;
                dirRoot = dirManager.findDirRoot(svnRepoRoot);
                long revsionID = svnRepoRoot.getLastestRevisionID().longValue();
                IRevision itemRe = revisionManager.findRevision(revsionID);

                // if this database has latest Revision, not update form SVN
                long latestSVNRevision = repository.getLatestRevision();
                if (itemRe.getRevisionNum() == latestSVNRevision) {
                    return;
                } else {
                    startRev = itemRe.getRevisionNum() + 1;               	
                    newRepo = false;
                }
            }

            // get log entries form current revision in database to newest revision
            // if this new url, and svnStartRevision != -1, get log entries at SVNStartRevision
            if (newRepo && this.svnStartRevision != -1) {
                startRev = this.svnStartRevision;                
            }
            logEntries = repository.log(new String[] { "" }, null, startRev, endRevision, true, true);            
            Iterator entries = logEntries.iterator();

            // build dir Structure for new repo
            if (entries.hasNext() && newRepo) {
                SVNLogEntry logEntry = (SVNLogEntry) entries.next();
                processForFirstRevision(logEntry);
            }
            // audit for change revision next
            while (entries.hasNext()) {
                SVNLogEntry logEntry = (SVNLogEntry) entries.next();
                processLogEntry(logEntry);
            }

        } catch (Exception ex) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("Error when transfer: ");
//            builder.append(ex.getMessage() + "\n");
//            StackTraceElement[] listError = ex.getStackTrace();
//            for (StackTraceElement stackTraceElement : listError) {
//                builder.append(stackTraceElement.toString() + "\n");
//            }
//            LOGGER.error(builder.toString());
            LOGGER.error("Loading '" + this.svnUrl + "'", ex);
        } finally {
            //em.close();
            DaoManager.getInstance().close();
        }
    }

    private void processForFirstRevision(SVNLogEntry logEntry) throws Exception {
        IRevision currentRevision = null;
        LOGGER.info("Process at revision: " + logEntry.getRevision() + " for url: " + this.svnUrl);
//        EntityManager em = PersistentManager.getEntityManager();
//        em.getTransaction().begin();
        DaoManager.getInstance().beginTransaction();
        try {
            exportSVN(this.tempPath, logEntry.getRevision());
            // create repo and Dir Root
            if(this.projectCode == null){
                svnRepoRoot = svnRepoManager.createSVNRepo(this.svnUrl);
            }
            else {
            svnRepoRoot = svnRepoManager.createSVNRepo(this.svnUrl, this.projectCode);
            }
            currentRevision = createRevisionByLogEntry(logEntry);

            dirRoot = DaoManager.getInstance().newDirManagerInst().newDirInst();
            dirRoot.setSVNRepo(svnRepoRoot);
            dirRoot.setDirName(AppConstant.ROOTNAME);
            dirRoot.setRevision(currentRevision);
            dirManager.createDir(dirRoot);            

            // build tree
            buildDirFileFromRoot(repository, svnRepoRoot, currentRevision);            

            // update revision
            svnRepoRoot.setLastestRevisionID(BigInteger.valueOf(currentRevision.getRevisionID()));
            

            // delete path
            SVNUtility.deletePath(tempPath);
            // em.getTransaction().commit();
            DaoManager.getInstance().commitTransaction();

        } catch (Exception ex) {
            //em.getTransaction().rollback();
            DaoManager.getInstance().rollbackTransaction();
            throw ex;
        }
    }

    /**
     * Build structure Tree for source code at First Revision Structure Tree will map with folder in Dir table and map
     * file in File and Version table
     * 
     * @param repository
     * @param svnRepo
     * @param revision
     * @throws Exception
     */
    private void buildDirFileFromRoot(SVNRepository repository, ISVNRepo svnRepo, IRevision revision) throws Exception {
        buildDirAndFileToDB(repository, svnRepo, dirRoot, "", revision);
    }

    private void buildDirAndFileToDB(SVNRepository repository, ISVNRepo svnRepo, IDir parentDir, String path,
            IRevision revision) throws Exception {
        try {
            Collection entries = repository.getDir(path, revision.getRevisionNum(), null, (Collection) null);

            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                SVNDirEntry entry = (SVNDirEntry) iterator.next();

                if (entry.getKind() == SVNNodeKind.DIR) {
                    IDir currentDir = DaoManager.getInstance().newDirManagerInst().newDirInst();
                    currentDir.setSVNRepo(svnRepo);
                    String parentPath = parentDir.getParentPath();
                    if (parentPath == null) {
                        parentPath = "";
                    }

                    currentDir.setParentDirID(parentDir.getDirID());
                    currentDir.setParentPath((parentPath.equals("") ? "" : parentPath + "/") + parentDir.getDirName());
                    currentDir.setDirName(entry.getName());
                    currentDir.setRevision(revision);
                    dirManager.createDir(currentDir);
                    buildDirAndFileToDB(repository, svnRepo, currentDir, path.equals("") ? entry.getName() : (path
                            + "/" + entry.getName()), revision);
                } else if (entry.getKind() == SVNNodeKind.FILE) {
                    //ISVNFilePK svnFilePK = new SVNFilePK(parentDir.getDirID(), entry.getName(), revision.getRevisionID());
                    ISVNFilePK svnFilePK = DaoManager.getInstance().newSVNFilePKManagerInst()
                                                     .newSVNFilePKInst(parentDir.getDirID(),
                                                                       entry.getName(),
                                                                       revision.getRevisionID());
                    ISVNFile currentFile = DaoManager.getInstance().newSVNFileManagerInst().newSVNFileInst();
                    currentFile.setSVNFilePK(svnFilePK);
                    currentFile.setDir(parentDir);
                    svnFileManager.createFile(currentFile);
                    // create a version into DB

                    //SVNVersionPK svnVersionPK = new SVNVersionPK(parentDir.getDirID(), entry.getName(),
                    //        entry.getRevision());
                    ISVNVersionPK svnVersionPK = DaoManager.getInstance().newSVNVersionPKManagerInst().newSVNVersionPKInst(parentDir.getDirID(), entry.getName(),
                                    entry.getRevision());
                    ISVNVersion currentVersion = DaoManager.getInstance().newSVNVersionManagerInst().newSVNVersionInst(svnVersionPK);
                    currentVersion.setDir(parentDir);
                    currentVersion.setSVNAction(AppConstant.ADDED);
                    currentVersion.setRevision(revision);
                    String pathLocal = "/" + (path.equals("") ? "" : path + "/") + entry.getName();
                    pathLocal = pathLocal.replace('/', File.separatorChar); // Due to: Program need to save to local Disk > change
                                                              // "/P/F/F5.txt" to "\P\F\F5.txt"
                    pathLocal = this.tempPath + pathLocal;
                    countContentFile(currentVersion, pathLocal);
                    svnVersionManager.createVersion(currentVersion);
                } else {
                    throw new Exception("Unknown kind of entry");
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void processLogEntry(SVNLogEntry logEntry) {
        LOGGER.info("Process at revision: " + logEntry.getRevision() + " for url: " + this.svnUrl);        
        //EntityManager em = PersistentManager.getEntityManager();
        //em.getTransaction().begin();
        DaoManager.getInstance().beginTransaction();
        try {
            IRevision revision = createRevisionByLogEntry(logEntry);            
            // update revision
            svnRepoRoot.setLastestRevisionID(BigInteger.valueOf(revision.getRevisionID()));            
            svnRepoManager.updateSVNRepo(svnRepoRoot);               
            Hashtable<String, SVNLogEntryPath> listAddModifyPath = new Hashtable<String, SVNLogEntryPath>();
            Hashtable<String, SVNLogEntryPath> listDeletePath = new Hashtable<String, SVNLogEntryPath>();

            Set changedPathsSet = logEntry.getChangedPaths().keySet();                       
            for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {

                SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());                
                String currentPath = SVNUtility.changeToRelativePath(repository, entryPath.getPath());                
                /*
                 * Special case: change log audit both Dir in repository and dir out respository we need omit dir out of
                 * repository
                 */
                if (currentPath == null) {
                    continue;
                }
                if (entryPath.getType() == AppConstant.ADDED || entryPath.getType() == AppConstant.MODIFIED) {
                    listAddModifyPath.put(currentPath, entryPath);                    
                } else if (entryPath.getType() == AppConstant.DELETED) {
                    listDeletePath.put(currentPath, entryPath);
                }

            }
            if (listAddModifyPath.size() > 0) {            	
                proccessRepoForAddMod("", dirRoot, revision, listAddModifyPath);                
            }
            if (listDeletePath.size() > 0) {
                // Todo: There is a bug at here
                proccessDeleteDirFile(listDeletePath, revision);
            }

            // complete transaction            
            //em.getTransaction().commit();
            DaoManager.getInstance().commitTransaction();
            //em.clear();            
        } catch (Exception ex) {
            LOGGER.error("Loading SVN entry", ex);
            //em.getTransaction().rollback();
            DaoManager.getInstance().rollbackTransaction();
            //throw ex;
        } catch (final OutOfMemoryError e) {
//            throw new Exception("Out of Memory when load data svn at svn: " + this.svnUrl + ", Revision: "
//                    + Long.toString(logEntry.getRevision()));
        }

    }

    private IRevision createRevisionByLogEntry(SVNLogEntry logEntry) throws Exception {
        IRevision currentRevision = null;
        long revisionNum = logEntry.getRevision();        
        currentRevision = DaoManager.getInstance().newRevisionManagerInst().newRevisionInst();        
        
        currentRevision.setAuthor(logEntry.getAuthor());         
        currentRevision.setComment(logEntry.getMessage());
        currentRevision.setRevisionNum(revisionNum);
        java.util.Date utilDate = logEntry.getDate();
        currentRevision.setDateLog(new java.sql.Date(utilDate.getTime()));
        currentRevision.setSVNRepo(svnRepoRoot);
        revisionManager.createRevision(currentRevision);        
        return currentRevision;
    }

    private void proccessRepoForAddMod(String path, IDir parentDir, IRevision revision,
            Hashtable<String, SVNLogEntryPath> listAddModifyPath) throws Exception {
        Collection entries = repository.getDir(path, revision.getRevisionNum(), null, (Collection) null);
        
        Iterator iterator = entries.iterator();
        CodeCheckerUtil codeCheckerUtil = new CodeCheckerUtil();
        while (iterator.hasNext()) {
            SVNDirEntry entry;

            String key;
            String parentPath;
            entry = (SVNDirEntry) iterator.next();
            parentPath = parentDir.getParentPath();            
            if (parentPath == null) {
                parentPath = "";
            }            
            parentPath = (parentPath.equals("") ? "" : parentPath + "/") + parentDir.getDirName();
            key = parentPath + "/" + entry.getName();// key has $. EX: "$/P"
            if (entry.getKind() == SVNNodeKind.DIR) {
                IDir currentDir;
                boolean isCopyPath = false;
                if (listAddModifyPath.containsKey(key)) {
                    SVNLogEntryPath entryPath = listAddModifyPath.get(key);
                    currentDir = DaoManager.getInstance().newDirManagerInst().newDirInst();

                    currentDir.setParentDirID(parentDir.getDirID());
                    currentDir.setParentPath(parentPath);
                    currentDir.setDirName(entry.getName());
                    currentDir.setRevision(revision);
                    currentDir.setSVNRepo(svnRepoRoot);
                    currentDir.setCopyFormPath(entryPath.getCopyPath());                    
                    currentDir.setCopyRevision(BigInteger.valueOf(entryPath.getCopyRevision()));
                    dirManager.createDir(currentDir);

                    // remove key after hashtable after use it
                    listAddModifyPath.remove(key);
                    /*******************************
                     * special case copy from an other Folder to new folder
                     * 
                     */
                    if (entryPath.getCopyPath() != null && !entryPath.getCopyPath().equals("")) {                    	
                        isCopyPath = true;
                    }
                } else {
                    currentDir = dirManager.findDir(svnRepoRoot, parentPath + "/" + entry.getName());                    
                }

                if (currentDir == null) {
                    String message = "Can't not find a dir: " + parentPath + " at revison: "
                            + revision.getRevisionNum() + "\n";

                    LOGGER.error(message);
                    throw new Exception(message);
                }
                // continue for inner dir
                if (isCopyPath) {
                    String tempURL = repository.getLocation().toDecodedString();
                    String specificURL = tempURL + "/"
                            + (path.equals("") ? entry.getName() : (path + "/" + entry.getName()));
                    String pathLocal = this.tempPath + "/"
                            + (path.equals("") ? entry.getName() : (path + "/" + entry.getName()));
                    pathLocal = pathLocal.replace("/", File.separator);                    
                    // export source code at Special URL --sub URL
                    // This export in order to minimize data transform from SVN to Local
                    exportSVNBySpecificPath(specificURL, pathLocal, revision.getRevisionNum());

                    // build tree structure for "copy folder" in SVN
                    buildDirAndFileToDB(repository, svnRepoRoot, currentDir, path.equals("") ? entry.getName() : (path
                            + "/" + entry.getName()), revision);
                } else {
                    if (listAddModifyPath.size() > 0 && checkGoToSubFolder(listAddModifyPath, key)) {
                        proccessRepoForAddMod(path.equals("") ? entry.getName() : (path + "/" + entry.getName()),
                                currentDir, revision, listAddModifyPath);                                                
                    }
                }

            }// else kind of entry if file. process at new file
            else if (entry.getKind() == SVNNodeKind.FILE && listAddModifyPath.containsKey(key)) {

                SVNLogEntryPath entryPath = listAddModifyPath.get(key); // key sample: '$/P/F1.txt'

                if (entryPath.getType() == AppConstant.ADDED) {
                    LOGGER.debug("parentDir.getDirID()=" + parentDir.getDirID() + "Entry name=" + entry.getName() + " revisionId=" + revision.getRevisionID());
                    //SVNFilePK svnFilePK = new SVNFilePK(parentDir.getDirID(), entry.getName(), revision.getRevisionID());
                    ISVNFilePK svnFilePK = DaoManager.getInstance().newSVNFilePKManagerInst()
                                           .newSVNFilePKInst(parentDir.getDirID(), entry.getName(),
                                                             revision.getRevisionID());
                    ISVNFile currentFile = DaoManager.getInstance().newSVNFileManagerInst().newSVNFileInst();
                    currentFile.setSVNFilePK(svnFilePK);
                    currentFile.setDir(parentDir);

                    svnFileManager.createFile(currentFile);
                }
                // create a version into DB

                //SVNVersionPK svnVersionPK = new SVNVersionPK(parentDir.getDirID(), entry.getName(), entry.getRevision());
                ISVNVersionPK svnVersionPK = DaoManager.getInstance().newSVNVersionPKManagerInst()
                                             .newSVNVersionPKInst(parentDir.getDirID(), entry.getName(),
                                                                  entry.getRevision());
                ISVNVersion currentVersion = DaoManager.getInstance().newSVNVersionManagerInst().newSVNVersionInst(svnVersionPK);
                currentVersion.setCopyFromPath(entryPath.getCopyPath());
                currentVersion.setCopyRevision(BigInteger.valueOf(entryPath.getCopyRevision()));
                currentVersion.setDir(parentDir);
                currentVersion.setSVNAction(entryPath.getType());

                currentVersion.setRevision(revision);

                String pathFileInSVN = (path.equals("") ? "" : path + "/") + entry.getName();// sample: "/P/F/F5.txt"
                                                                                             // this is URL in SVN
                                                                                             // server
                String tempFilePath = this.tempPath + entry.getName();
                tempFilePath = tempFilePath.replace("/", "\\");
                String extFile = CommonUtil.getExtension(entry.getName());
                if (counter.isCountable(extFile)) {
                    exportFile(pathFileInSVN, tempFilePath, revision.getRevisionNum());
                    // When conf.properties declared the implementation of CodeChecker
                    if (CodeCheckerUtil.isImplemented()) {
                        Map<String, List<AuditEvent>> resultCheck = codeCheckerUtil.check(tempFilePath);
                        List<AuditEvent> errorList = resultCheck.get(tempFilePath);
                        if (errorList == null) {
                            errorList = new ArrayList<AuditEvent>();
                        }
                        currentVersion.setNmStaticBug(BigInteger.valueOf(errorList.size()));
                    }
                    countContentFile(currentVersion, tempFilePath);
                }
                svnVersionManager.createVersion(currentVersion);

                // remove key after hashtable after use it
                listAddModifyPath.remove(key);
            }
        }
    }

    private void proccessDeleteDirFile(Hashtable<String, SVNLogEntryPath> listDeletePath, IRevision revision)
            throws Exception {
        Enumeration<SVNLogEntryPath> en = listDeletePath.elements();

        while (en.hasMoreElements()) {
            SVNLogEntryPath entryPath = en.nextElement();
            IDir currentDir;
            if (entryPath.getKind() == SVNNodeKind.DIR) {
                String currentPath = SVNUtility.changeToRelativePath(repository, entryPath.getPath());

                currentDir = dirManager.findDir(svnRepoRoot, currentPath);
                if (currentDir == null) {
                    String message = "Can't not find a disk: " + currentPath + " at revison: "
                            + revision.getRevisionNum() + "\n";
                    message += "Entry path: "
                            + entryPath.getType()
                            + " "
                            + entryPath.getPath()
                            + ((entryPath.getCopyPath() != null) ? " (from " + entryPath.getCopyPath() + " revision "
                                    + entryPath.getCopyRevision() + ")" : "");
                    LOGGER.error(message);
                    break;
                    //throw new Exception(message);
                }

                // Update status for Dir and subDir to deleted
                deleteDirRecursive(currentDir, revision);
            } else if (entryPath.getKind() == SVNNodeKind.FILE) {
                ISVNVersion svnVersion;
                ISVNVersionPK svnVersionPK;
                String currentPath = SVNUtility.changeToRelativePath(repository, entryPath.getPath());
                String[] listPaths = currentPath.split("/");
                String fileName = listPaths[listPaths.length - 1];
                String dirPath = SVNUtility.getSubStringFromEnd(currentPath, "/" + fileName);

                currentDir = dirManager.findDir(svnRepoRoot, dirPath);
                // Todo: There is a null exception at below statement
                // Thach Debug
                if ((currentDir == null) || (revision == null)) {
                    LOGGER.debug("currentDir=" + currentDir + ";revision+" + revision);
                } else {
                    //svnVersionPK = new SVNVersionPK(currentDir.getDirID(), fileName, revision.getRevisionID());
                    svnVersionPK = DaoManager.getInstance().newSVNVersionPKManagerInst().newSVNVersionPKInst(currentDir.getDirID(), fileName, revision.getRevisionID());
                    //svnVersion = new SVNVersion(svnVersionPK);
                    svnVersion = DaoManager.getInstance().newSVNVersionManagerInst().newSVNVersionInst(svnVersionPK);
                    svnVersion.setCopyFromPath(entryPath.getCopyPath());
                    svnVersion.setCopyRevision(BigInteger.valueOf(entryPath.getCopyRevision()));
                    svnVersion.setDir(currentDir);
                    svnVersion.setSVNAction(entryPath.getType());
                    svnVersion.setRevision(revision);
                    svnVersion.setNMComment(BigInteger.valueOf(0));
                    svnVersionManager.createVersion(svnVersion);
                }
            } else if (entryPath.getKind() == SVNNodeKind.UNKNOWN){
            	break;
            } else {
                throw new Exception("Unknown kind of entry at entryPath: " + entryPath.getPath() + ", type: "
                        + entryPath.getType() + "at revision: " + String.valueOf(revision.getRevisionNum()));
            }
        }

    }

    /**
     * Count LOC and number of comments of file.
     * @param version in/out
     * @param pathLocal
     */
    private void countContentFile(ISVNVersion version, String pathLocal) {
        // code count line of code and comment here
        ISizeCounter counter = SizeCounterFactory.getDefaultInstance();
        SizeMetaData sizeMd;
        try {
            sizeMd = counter.countSize(pathLocal);
            version.setSize(BigInteger.valueOf(sizeMd.getSize()));
            version.setNMComment(BigInteger.valueOf(sizeMd.getComment()));
//            version.setNmloc(BigInteger.valueOf(sizeMd.getSize()));
            if (sizeMd.getUnit() != null) {
                version.setUnit(sizeMd.getUnit().toString());
            }
        } catch (UnsupportedFileType e) {
            version.setNMComment(BigInteger.valueOf(0));
        }

    }

    /**
     * Export a file from SVN to Local disk
     * 
     * @param FilePathInSVN
     *            : URL relative in SVN. Sample:"ProgNewsDesk/trunk/SVN2RDB/src/svn2rdb/ConsoleSVN2RDB.java"
     * @param pathLocal
     *            : place to save to local PC . Sample: "D:\\Temp\\DemoExport\\"
     * @param revsion
     *            : Version want export
     */
    private void exportFile(String filePathInSVN, String pathLocal, long revisionNum) throws Exception {
        /*
         * This Map will be used to get the file properties. Each Map key is a property name and the value associated
         * with the key is the property value.
         */
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Gets the contents and properties of the file located at filePath in the repository at the latest revision
            repository.getFile(filePathInSVN, revisionNum, fileProperties, byteArrayOutputStream);
            File exportDir = new File(this.tempPath);
            // if working folder is exist, delete it in order making empty folder
            if (exportDir.exists()) {

                SVNUtility.deletePath(this.tempPath);
            }
            exportDir.mkdirs();
            FileOutputStream f2 = new FileOutputStream(pathLocal);
            byteArrayOutputStream.writeTo(f2);
            f2.close();
        } catch (Exception ex) {
            Exception er = new Exception("Can not export file:" + filePathInSVN + " at revision:"
                    + String.valueOf(revisionNum));
            throw er;
        }
    }

    /**
     * export a specific URl to local
     * 
     * @param specificURL
     *            : URL on SVN
     * @param pathLocal
     * @param revisionNum
     *            : Revision which you want to export
     * @throws Exception
     */
    private void exportSVNBySpecificPath(String specificURL, String pathLocal, long revisionNum) throws Exception {
        SVNRepository repo;
        try {
            File exportDir = new File(pathLocal);
            if (exportDir.exists()) {

                SVNUtility.deletePath(pathLocal);
            }
            exportDir.mkdirs();

            repo = SVNUtility.buildSVNRepository(specificURL, this.svnUsername, this.svnPassword);
            SVNNodeKind nodeKind = repo.checkPath("", revisionNum);
            if (nodeKind == SVNNodeKind.NONE) {
                SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL ''{0}''",
                        specificURL);
                throw new SVNException(err);
            } else if (nodeKind == SVNNodeKind.FILE) {
                SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
                        "Entry at URL ''{0}'' is a file while directory was expected", this.svnUrl);
                throw new SVNException(err);
            }
            ISVNReporterBaton reporterBaton = new ExportReporterBaton(revisionNum);
            ISVNEditor exportEditor = new ExportEditor(exportDir);

            repo.update(revisionNum, null, true, reporterBaton, exportEditor);

        } catch (final java.lang.OutOfMemoryError e) {
            throw new Exception("Error: Out of Memory when export svn at svn: " + specificURL + ", Revision: "
                    + Long.toString(revisionNum));
        }
    }

    /**
     * Check if in hashTable, has a key begin with specific string, continue go to sub folder
     * 
     * @param listAddModifyPath
     * @param key
     * @return
     */
    private boolean checkGoToSubFolder(Hashtable<String, SVNLogEntryPath> listAddModifyPath, String path) {
        Set<String> setKey = listAddModifyPath.keySet();
        Iterator<String> iteratorKey = setKey.iterator();
        while (iteratorKey.hasNext()) {
            String keyCompare = iteratorKey.next();
            if (keyCompare.startsWith(keyCompare)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update status Dir which you want to delete to "1" and also update sub Dir to Deleted status
     * 
     * @param dir
     * @param revision
     * @throws Exception 
     */
    private void deleteDirRecursive(IDir dir, IRevision revision) throws Exception {
        dir.setStatus(Status.Deleted.ordinal());
        dir.setDeletedRevisionID(BigInteger.valueOf(revision.getRevisionNum()));
        dirManager.updateDir(dir);
        List<IDir> subDirs = dirManager.getDirs(dir);
        for (IDir subDir : subDirs) {
            deleteDirRecursive(subDir, revision);
        }
    }

}
