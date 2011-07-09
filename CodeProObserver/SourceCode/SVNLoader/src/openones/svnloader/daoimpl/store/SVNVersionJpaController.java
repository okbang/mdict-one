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
import openones.svnloader.daoimpl.entity.Revision;
import openones.svnloader.daoimpl.entity.SVNVersion;
import openones.svnloader.daoimpl.entity.SVNVersionPK;
import openones.svnloader.daoimpl.store.exceptions.NonexistentEntityException;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;


/**
 *
 */
public class SVNVersionJpaController {

    public EntityManager getEntityManager() {
        return PersistentManager.getEntityManager();
    }

    public void create(SVNVersion SVNVersion) throws PreexistingEntityException, Exception {
        if (SVNVersion.getSVNVersionPK() == null) {
            SVNVersion.setSVNVersionPK(new SVNVersionPK());
        }
        SVNVersion.getSVNVersionPK().setRevisionID(SVNVersion.getRevision().getRevisionID());
        SVNVersion.getSVNVersionPK().setDirID(SVNVersion.getDir().getDirID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            Revision revision = SVNVersion.getRevision();
            if (revision != null) {
                revision = em.getReference(revision.getClass(), revision.getRevisionID());
                SVNVersion.setRevision(revision);
            }
            Dir dir = SVNVersion.getDir();
            if (dir != null) {
                dir = em.getReference(dir.getClass(), dir.getDirID());
                SVNVersion.setDir(dir);
            }
            em.persist(SVNVersion);
            if (revision != null) {
                revision.getSVNVersionList().add(SVNVersion);
                revision = em.merge(revision);
            }
            if (dir != null) {
                dir.getSVNVersionList().add(SVNVersion);
                dir = em.merge(dir);
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSVNVersion(SVNVersion.getSVNVersionPK()) != null) {
                throw new PreexistingEntityException("SVNVersion " + SVNVersion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void edit(SVNVersion SVNVersion) throws NonexistentEntityException, Exception {
        SVNVersion.getSVNVersionPK().setRevisionID(SVNVersion.getRevision().getRevisionID());
        SVNVersion.getSVNVersionPK().setDirID(SVNVersion.getDir().getDirID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNVersion persistentSVNVersion = em.find(SVNVersion.class, SVNVersion.getSVNVersionPK());
            Revision revisionOld = persistentSVNVersion.getRevision();
            Revision revisionNew = SVNVersion.getRevision();
            Dir dirOld = persistentSVNVersion.getDir();
            Dir dirNew = SVNVersion.getDir();
            if (revisionNew != null) {
                revisionNew = em.getReference(revisionNew.getClass(), revisionNew.getRevisionID());
                SVNVersion.setRevision(revisionNew);
            }
            if (dirNew != null) {
                dirNew = em.getReference(dirNew.getClass(), dirNew.getDirID());
                SVNVersion.setDir(dirNew);
            }
            SVNVersion = em.merge(SVNVersion);
            if (revisionOld != null && !revisionOld.equals(revisionNew)) {
                revisionOld.getSVNVersionList().remove(SVNVersion);
                revisionOld = em.merge(revisionOld);
            }
            if (revisionNew != null && !revisionNew.equals(revisionOld)) {
                revisionNew.getSVNVersionList().add(SVNVersion);
                revisionNew = em.merge(revisionNew);
            }
            if (dirOld != null && !dirOld.equals(dirNew)) {
                dirOld.getSVNVersionList().remove(SVNVersion);
                dirOld = em.merge(dirOld);
            }
            if (dirNew != null && !dirNew.equals(dirOld)) {
                dirNew.getSVNVersionList().add(SVNVersion);
                dirNew = em.merge(dirNew);
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SVNVersionPK id = SVNVersion.getSVNVersionPK();
                if (findSVNVersion(id) == null) {
                    throw new NonexistentEntityException("The sVNVersion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void destroy(SVNVersionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNVersion SVNVersion;
            try {
                SVNVersion = em.getReference(SVNVersion.class, id);
                SVNVersion.getSVNVersionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The SVNVersion with id " + id + " no longer exists.", enfe);
            }
            Revision revision = SVNVersion.getRevision();
            if (revision != null) {
                revision.getSVNVersionList().remove(SVNVersion);
                revision = em.merge(revision);
            }
            Dir dir = SVNVersion.getDir();
            if (dir != null) {
                dir.getSVNVersionList().remove(SVNVersion);
                dir = em.merge(dir);
            }
            em.remove(SVNVersion);
            //em.getTransaction().commit();
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public List<SVNVersion> findSVNVersionEntities() {
        return findSVNVersionEntities(true, -1, -1);
    }

    public List<SVNVersion> findSVNVersionEntities(int maxResults, int firstResult) {
        return findSVNVersionEntities(false, maxResults, firstResult);
    }

    private List<SVNVersion> findSVNVersionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SVNVersion.class));
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

    public SVNVersion findSVNVersion(SVNVersionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SVNVersion.class, id);
        } finally {
            //em.close();
        }
    }

    public int getSVNVersionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SVNVersion> rt = cq.from(SVNVersion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            //em.close();
        }
    }

}
