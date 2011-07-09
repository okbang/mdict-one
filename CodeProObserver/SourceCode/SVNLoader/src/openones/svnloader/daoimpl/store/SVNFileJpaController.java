/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.store;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import openones.svnloader.daoimpl.entity.Dir;
import openones.svnloader.daoimpl.entity.SVNFile;
import openones.svnloader.daoimpl.entity.SVNFilePK;
import openones.svnloader.daoimpl.store.exceptions.NonexistentEntityException;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;


/**
 *
 */
public class SVNFileJpaController {

    public EntityManager getEntityManager() {
        return PersistentManager.getEntityManager();
    }

    public void create(SVNFile SVNFile) throws PreexistingEntityException, Exception {
        if (SVNFile.getSVNFilePK() == null) {
            SVNFile.setSVNFilePK(new SVNFilePK());
        }
        SVNFile.getSVNFilePK().setDirID(SVNFile.getDir().getDirID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            Dir dir = SVNFile.getDir();
            if (dir != null) {
                dir = em.getReference(dir.getClass(), dir.getDirID());
                SVNFile.setDir(dir);
            }
            em.persist(SVNFile);
            if (dir != null) {
                dir.getSVNFileList().add(SVNFile);
                dir = em.merge(dir);
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSVNFile(SVNFile.getSVNFilePK()) != null) {
                throw new PreexistingEntityException("SVNFile " + SVNFile + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void edit(SVNFile SVNFile) throws NonexistentEntityException, Exception {
        SVNFile.getSVNFilePK().setDirID(SVNFile.getDir().getDirID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNFile persistentSVNFile = em.find(SVNFile.class, SVNFile.getSVNFilePK());
            Dir dirOld = persistentSVNFile.getDir();
            Dir dirNew = SVNFile.getDir();
            if (dirNew != null) {
                dirNew = em.getReference(dirNew.getClass(), dirNew.getDirID());
                SVNFile.setDir(dirNew);
            }
            SVNFile = em.merge(SVNFile);
            if (dirOld != null && !dirOld.equals(dirNew)) {
                dirOld.getSVNFileList().remove(SVNFile);
                dirOld = em.merge(dirOld);
            }
            if (dirNew != null && !dirNew.equals(dirOld)) {
                dirNew.getSVNFileList().add(SVNFile);
                dirNew = em.merge(dirNew);
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SVNFilePK id = SVNFile.getSVNFilePK();
                if (findSVNFile(id) == null) {
                    throw new NonexistentEntityException("The sVNFile with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void destroy(SVNFilePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNFile SVNFile;
            try {
                SVNFile = em.getReference(SVNFile.class, id);
                SVNFile.getSVNFilePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The SVNFile with id " + id + " no longer exists.", enfe);
            }
            Dir dir = SVNFile.getDir();
            if (dir != null) {
                dir.getSVNFileList().remove(SVNFile);
                dir = em.merge(dir);
            }
            em.remove(SVNFile);
            //em.getTransaction().commit();
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public List<SVNFile> findSVNFileEntities() {
        return findSVNFileEntities(true, -1, -1);
    }

    public List<SVNFile> findSVNFileEntities(int maxResults, int firstResult) {
        return findSVNFileEntities(false, maxResults, firstResult);
    }

    private List<SVNFile> findSVNFileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SVNFile.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            //em.close();
        }
    }

    public SVNFile findSVNFile(SVNFilePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SVNFile.class, id);
        } finally {
            //em.close();
        }
    }

    public int getSVNFileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SVNFile> rt = cq.from(SVNFile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            //em.close();
        }
    }

}
