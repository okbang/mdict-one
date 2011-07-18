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
package openones.svnloader.daoimpl;

import java.util.List;

import openones.svnloader.dao.IDirManager;
import openones.svnloader.dao.entity.IDir;
import openones.svnloader.daoimpl.entity.Dir;
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.store.DirJpaController;
import openones.svnloader.daoimpl.store.exceptions.IllegalOrphanException;
import openones.svnloader.daoimpl.store.exceptions.NonexistentEntityException;
import openones.svnloader.engine.AppConstant;
import openones.svnloader.engine.SVNUtility;
import openones.svnloader.engine.Status;
import rocky.common.CommonUtil;


public class DirManager implements IDirManager {
    private DirJpaController jpaController;

    public DirManager() {
        jpaController = new DirJpaController();
    }

    /**
     * [Explain the description for this method here].
     * @param dirObj
     * @throws Exception
     * @see openones.svnloader.engine.manager.IDirManager#createDir(openones.svnloader.daoimpl.entity.Dir)
     */
    @Override
    public void createDir(IDir dir) throws Exception {
        try {
            dir.setStatus(Status.Added.ordinal());
            dir.setDirID(jpaController.getNextId());
            jpaController.create((Dir) dir);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * [Explain the description for this method here].
     * @param svnRepoObj
     * @param path
     * @return
     * @see openones.svnloader.engine.manager.IDirManager#findDir(openones.svnloader.daoimpl.entity.SVNRepo, java.lang.String)
     */
    @Override
    public Dir findDir(Object svnRepoObj, String path) {
        if (path == null) {
            return null;
        }
        SVNRepo svnRepo = (SVNRepo) svnRepoObj;
        
        Dir targetDir = null;
        String[] listPaths = path.split("/");
        
        if (!CommonUtil.isNNandNB(listPaths)) {
            return null;
        }
        
        String dirName = listPaths[listPaths.length - 1];
        String parentPath = null;
        if (dirName.equals(path)) {
            return findRoot(svnRepo);
        } else {
            parentPath = SVNUtility.getSubStringFromEnd(path, "/" + dirName);
        }

        List<Dir> listDir = jpaController.findByParentPathAndName(dirName, parentPath);
        if (!CommonUtil.isNNandNB(listDir.size())) {
            return null;
        }
        for (Dir dir : listDir) {
            if (dir.getSVNRepo().getSvnid() == svnRepo.getSvnid()) {
                targetDir = dir;
                break;
            }
        }
        return targetDir;
    }

    /**
     * [Explain the description for this method here].
     * @param svnRepoObj
     * @return
     * @see openones.svnloader.engine.manager.IDirManager#findDirRoot(openones.svnloader.daoimpl.entity.SVNRepo)
     */
    @Override
    public Dir findDirRoot(Object svnRepoObj) {
        SVNRepo svnRepo = (SVNRepo) svnRepoObj;
        Dir targetDir = null;

        List<Dir> listDir = svnRepo.getDirList();
        for (Dir dir : listDir) {
            if (dir.getDirName().equals(AppConstant.ROOTNAME)) {
                targetDir = dir;
                break;
            }
        }
        return targetDir;
    }

    /**
     * [Explain the description for this method here].
     * @param svnRepoObj
     * @return
     * @see openones.svnloader.engine.manager.IDirManager#findRoot(openones.svnloader.daoimpl.entity.SVNRepo)
     */
    @Override
    public Dir findRoot(Object svnRepoObj) {
        SVNRepo svnRepo = (SVNRepo) svnRepoObj;
        Dir targetDir = null;
        List<Dir> listDir = jpaController.findRoot(AppConstant.ROOTNAME);
        if (listDir.size() == 0) {
            return null;
        }
        for (Dir dir : listDir) {
            if (dir.getSVNRepo().getSvnid() == svnRepo.getSvnid()) {
                targetDir = dir;
                break;
            }
        }
        return targetDir;
    }

    /**
     * Update information in a dir
     * 
     * @param dir
     * @throws Exception 
     */
    @Override
    public void updateDir(IDir dir) throws Exception {
        jpaController.edit((Dir) dir);
    }

    /**
     * [Explain the description for this method here].
     * @param parentDirObj
     * @return
     * @see openones.svnloader.engine.manager.IDirManager#getDirs(openones.svnloader.daoimpl.entity.Dir)
     */
    @Override
    public List getDirs(IDir parentDirObj) {    
        Dir parentDir = (Dir) parentDirObj;
        List<Dir> dirList = jpaController.findDirEntities(parentDir.getDirID());
        
        return dirList;
    }

    /**
     * [Explain the description for this method here].
     * @return
     * @see openones.svnloader.dao.IDirManager#newDirInst()
     */
    @Override
    public IDir newDirInst() {
        return new Dir();
    }

}
