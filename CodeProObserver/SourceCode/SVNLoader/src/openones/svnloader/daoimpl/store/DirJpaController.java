/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.store;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import openones.svnloader.daoimpl.entity.Dir;
import openones.svnloader.daoimpl.entity.Revision;
import openones.svnloader.daoimpl.entity.SVNFile;
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.entity.SVNVersion;
import openones.svnloader.daoimpl.store.exceptions.IllegalOrphanException;
import openones.svnloader.daoimpl.store.exceptions.NonexistentEntityException;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;

/**
 * 
 */
public class DirJpaController {

    public EntityManager getEntityManager() {
        return PersistentManager.getEntityManager();
    }

    public void create(Dir dir) throws Exception {
        if (dir.getSVNVersionList() == null) {
            dir.setSVNVersionList(new ArrayList<SVNVersion>());
        }
        if (dir.getSVNFileList() == null) {
            dir.setSVNFileList(new ArrayList<SVNFile>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();

            SVNRepo SVNRepo = dir.getSVNRepo();
            if (SVNRepo != null) {
                SVNRepo = em.getReference(SVNRepo.getClass(), SVNRepo.getSvnid());
                dir.setSVNRepo(SVNRepo);
            }
            Revision revision = dir.getRevision();
            if (revision != null) {
                revision = em.getReference(revision.getClass(), revision.getRevisionID());
                dir.setRevision(revision);
            }
            List<SVNVersion> attachedSVNVersionList = new ArrayList<SVNVersion>();
            for (SVNVersion SVNVersionListSVNVersionToAttach : dir.getSVNVersionList()) {
                SVNVersionListSVNVersionToAttach = em.getReference(SVNVersionListSVNVersionToAttach.getClass(),
                        SVNVersionListSVNVersionToAttach.getSVNVersionPK());
                attachedSVNVersionList.add(SVNVersionListSVNVersionToAttach);
            }
            dir.setSVNVersionList(attachedSVNVersionList);
            List<SVNFile> attachedSVNFileList = new ArrayList<SVNFile>();
            for (SVNFile SVNFileListSVNFileToAttach : dir.getSVNFileList()) {
                SVNFileListSVNFileToAttach = em.getReference(SVNFileListSVNFileToAttach.getClass(),
                        SVNFileListSVNFileToAttach.getSVNFilePK());
                attachedSVNFileList.add(SVNFileListSVNFileToAttach);
            }
            dir.setSVNFileList(attachedSVNFileList);
            em.persist(dir);
            if (SVNRepo != null) {
                SVNRepo.getDirList().add(dir);
                SVNRepo = em.merge(SVNRepo);
            }
            if (revision != null) {
                revision.getDirList().add(dir);
                revision = em.merge(revision);
            }
            for (SVNVersion SVNVersionListSVNVersion : dir.getSVNVersionList()) {
                Dir oldDirOfSVNVersionListSVNVersion = SVNVersionListSVNVersion.getDir();
                SVNVersionListSVNVersion.setDir(dir);
                SVNVersionListSVNVersion = em.merge(SVNVersionListSVNVersion);
                if (oldDirOfSVNVersionListSVNVersion != null) {
                    oldDirOfSVNVersionListSVNVersion.getSVNVersionList().remove(SVNVersionListSVNVersion);
                    oldDirOfSVNVersionListSVNVersion = em.merge(oldDirOfSVNVersionListSVNVersion);
                }
            }
            for (SVNFile SVNFileListSVNFile : dir.getSVNFileList()) {
                Dir oldDirOfSVNFileListSVNFile = SVNFileListSVNFile.getDir();
                SVNFileListSVNFile.setDir(dir);
                SVNFileListSVNFile = em.merge(SVNFileListSVNFile);
                if (oldDirOfSVNFileListSVNFile != null) {
                    oldDirOfSVNFileListSVNFile.getSVNFileList().remove(SVNFileListSVNFile);
                    oldDirOfSVNFileListSVNFile = em.merge(oldDirOfSVNFileListSVNFile);
                }
            }
            // em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDir(dir.getDirID()) != null) {
                throw new PreexistingEntityException("Dir " + dir + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                // em.close();
            }
        }
    }

    public void edit(Dir dir) throws IllegalOrphanException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // em.getTransaction().begin();
            Dir persistentDir = em.find(Dir.class, dir.getDirID());
            SVNRepo SVNRepoOld = persistentDir.getSVNRepo();
            SVNRepo SVNRepoNew = dir.getSVNRepo();
            Revision revisionOld = persistentDir.getRevision();
            Revision revisionNew = dir.getRevision();
            List<SVNVersion> SVNVersionListOld = persistentDir.getSVNVersionList();
            List<SVNVersion> SVNVersionListNew = dir.getSVNVersionList();
            List<SVNFile> SVNFileListOld = persistentDir.getSVNFileList();
            List<SVNFile> SVNFileListNew = dir.getSVNFileList();
            List<String> illegalOrphanMessages = null;
            for (SVNVersion SVNVersionListOldSVNVersion : SVNVersionListOld) {
                if (!SVNVersionListNew.contains(SVNVersionListOldSVNVersion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SVNVersion " + SVNVersionListOldSVNVersion
                            + " since its dir field is not nullable.");
                }
            }
            for (SVNFile SVNFileListOldSVNFile : SVNFileListOld) {
                if (!SVNFileListNew.contains(SVNFileListOldSVNFile)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SVNFile " + SVNFileListOldSVNFile
                            + " since its dir field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (SVNRepoNew != null) {
                SVNRepoNew = em.getReference(SVNRepoNew.getClass(), SVNRepoNew.getSvnid());
                dir.setSVNRepo(SVNRepoNew);
            }
            if (revisionNew != null) {
                revisionNew = em.getReference(revisionNew.getClass(), revisionNew.getRevisionID());
                dir.setRevision(revisionNew);
            }
            List<SVNVersion> attachedSVNVersionListNew = new ArrayList<SVNVersion>();
            for (SVNVersion SVNVersionListNewSVNVersionToAttach : SVNVersionListNew) {
                SVNVersionListNewSVNVersionToAttach = em.getReference(SVNVersionListNewSVNVersionToAttach.getClass(),
                        SVNVersionListNewSVNVersionToAttach.getSVNVersionPK());
                attachedSVNVersionListNew.add(SVNVersionListNewSVNVersionToAttach);
            }
            SVNVersionListNew = attachedSVNVersionListNew;
            dir.setSVNVersionList(SVNVersionListNew);
            List<SVNFile> attachedSVNFileListNew = new ArrayList<SVNFile>();
            for (SVNFile SVNFileListNewSVNFileToAttach : SVNFileListNew) {
                SVNFileListNewSVNFileToAttach = em.getReference(SVNFileListNewSVNFileToAttach.getClass(),
                        SVNFileListNewSVNFileToAttach.getSVNFilePK());
                attachedSVNFileListNew.add(SVNFileListNewSVNFileToAttach);
            }
            SVNFileListNew = attachedSVNFileListNew;
            dir.setSVNFileList(SVNFileListNew);
            dir = em.merge(dir);
            if (SVNRepoOld != null && !SVNRepoOld.equals(SVNRepoNew)) {
                SVNRepoOld.getDirList().remove(dir);
                SVNRepoOld = em.merge(SVNRepoOld);
            }
            if (SVNRepoNew != null && !SVNRepoNew.equals(SVNRepoOld)) {
                SVNRepoNew.getDirList().add(dir);
                SVNRepoNew = em.merge(SVNRepoNew);
            }
            if (revisionOld != null && !revisionOld.equals(revisionNew)) {
                revisionOld.getDirList().remove(dir);
                revisionOld = em.merge(revisionOld);
            }
            if (revisionNew != null && !revisionNew.equals(revisionOld)) {
                revisionNew.getDirList().add(dir);
                revisionNew = em.merge(revisionNew);
            }
            for (SVNVersion SVNVersionListNewSVNVersion : SVNVersionListNew) {
                if (!SVNVersionListOld.contains(SVNVersionListNewSVNVersion)) {
                    Dir oldDirOfSVNVersionListNewSVNVersion = SVNVersionListNewSVNVersion.getDir();
                    SVNVersionListNewSVNVersion.setDir(dir);
                    SVNVersionListNewSVNVersion = em.merge(SVNVersionListNewSVNVersion);
                    if (oldDirOfSVNVersionListNewSVNVersion != null && !oldDirOfSVNVersionListNewSVNVersion.equals(dir)) {
                        oldDirOfSVNVersionListNewSVNVersion.getSVNVersionList().remove(SVNVersionListNewSVNVersion);
                        oldDirOfSVNVersionListNewSVNVersion = em.merge(oldDirOfSVNVersionListNewSVNVersion);
                    }
                }
            }
            for (SVNFile SVNFileListNewSVNFile : SVNFileListNew) {
                if (!SVNFileListOld.contains(SVNFileListNewSVNFile)) {
                    Dir oldDirOfSVNFileListNewSVNFile = SVNFileListNewSVNFile.getDir();
                    SVNFileListNewSVNFile.setDir(dir);
                    SVNFileListNewSVNFile = em.merge(SVNFileListNewSVNFile);
                    if (oldDirOfSVNFileListNewSVNFile != null && !oldDirOfSVNFileListNewSVNFile.equals(dir)) {
                        oldDirOfSVNFileListNewSVNFile.getSVNFileList().remove(SVNFileListNewSVNFile);
                        oldDirOfSVNFileListNewSVNFile = em.merge(oldDirOfSVNFileListNewSVNFile);
                    }
                }
            }
            // em.getTransaction().commit();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                Integer id = dir.getDirID();
//                if (findDir(id) == null) {
//                    throw new NonexistentEntityException("The dir with id " + id + " no longer exists.");
//                }
//            }
//            throw ex;
        } finally {
            if (em != null) {
                // em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // em.getTransaction().begin();
            Dir dir;
            try {
                dir = em.getReference(Dir.class, id);
                dir.getDirID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dir with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SVNVersion> SVNVersionListOrphanCheck = dir.getSVNVersionList();
            for (SVNVersion SVNVersionListOrphanCheckSVNVersion : SVNVersionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dir (" + dir + ") cannot be destroyed since the SVNVersion "
                        + SVNVersionListOrphanCheckSVNVersion
                        + " in its SVNVersionList field has a non-nullable dir field.");
            }
            List<SVNFile> SVNFileListOrphanCheck = dir.getSVNFileList();
            for (SVNFile SVNFileListOrphanCheckSVNFile : SVNFileListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dir (" + dir + ") cannot be destroyed since the SVNFile "
                        + SVNFileListOrphanCheckSVNFile + " in its SVNFileList field has a non-nullable dir field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SVNRepo SVNRepo = dir.getSVNRepo();
            if (SVNRepo != null) {
                SVNRepo.getDirList().remove(dir);
                SVNRepo = em.merge(SVNRepo);
            }
            Revision revision = dir.getRevision();
            if (revision != null) {
                revision.getDirList().remove(dir);
                revision = em.merge(revision);
            }
            em.remove(dir);
            // em.getTransaction().commit();
        } finally {
            if (em != null) {
                // em.close();
            }
        }
    }

    public List<Dir> findDirEntities() {
        return findDirEntities(true, -1, -1);
    }

    public List<Dir> findDirEntities(int maxResults, int firstResult) {
        return findDirEntities(false, maxResults, firstResult);
    }

    private List<Dir> findDirEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dir.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            // em.close();
        }
    }

    public Dir findDir(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dir.class, id);
        } finally {
            // em.close();
        }
    }

    public int getDirCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dir> rt = cq.from(Dir.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            // em.close();
        }
    }

    public int getNextId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Dir.getMaxId");
            return ((Integer) q.getSingleResult()) + 1;
        } catch (Exception ex) {
            return 1;
        } finally {
            // em.close();
        }
    }

    public List<Dir> findByParentPathAndName(String dirName, String parentPath) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Dir.findByParentPathAndName");
            q.setParameter("dirName", dirName);
            q.setParameter("parentPath", parentPath);

            return q.getResultList();
        } catch (Exception ex) {
            return null;
        } finally {
            // em.close();
        }
    }

    public List<Dir> findRoot(String rootName) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Dir.findByDirName");
            q.setParameter("dirName", rootName);
            return q.getResultList();
        } catch (Exception ex) {
            return null;
        } finally {
            // em.close();
        }
    }
    
    /**
     * Get List of Dir by parent DirID
     * @param parentDirID
     * @return
     */
    public List<Dir> findDirEntities(int parentDirID) {
        EntityManager em = getEntityManager();
        List<Dir> listDir= new ArrayList<Dir>();
       
        try {
            Query q = em.createNamedQuery("Dir.findByParentDirID");
            q.setParameter("parentDirID", parentDirID);

            return q.getResultList();
        } catch (Exception ex) {
            return listDir;
        } finally {
            // em.close();
        }
    }

}
