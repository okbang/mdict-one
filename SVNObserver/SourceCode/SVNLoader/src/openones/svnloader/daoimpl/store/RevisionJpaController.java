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
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.entity.SVNVersion;
import openones.svnloader.daoimpl.store.exceptions.IllegalOrphanException;
import openones.svnloader.daoimpl.store.exceptions.NonexistentEntityException;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;


/**
 *
 */
public class RevisionJpaController {

    public EntityManager getEntityManager() {
        return PersistentManager.getEntityManager();
    }

    public void create(Revision revision) throws PreexistingEntityException, Exception {
        if (revision.getSVNVersionList() == null) {
            revision.setSVNVersionList(new ArrayList<SVNVersion>());
        }
        if (revision.getDirList() == null) {
            revision.setDirList(new ArrayList<Dir>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNRepo SVNRepo = revision.getSVNRepo();
            if (SVNRepo != null) {
                SVNRepo = em.getReference(SVNRepo.getClass(), SVNRepo.getSvnid());
                revision.setSVNRepo(SVNRepo);
            }
            List<SVNVersion> attachedSVNVersionList = new ArrayList<SVNVersion>();
            for (SVNVersion SVNVersionListSVNVersionToAttach : revision.getSVNVersionList()) {
                SVNVersionListSVNVersionToAttach = em.getReference(SVNVersionListSVNVersionToAttach.getClass(), SVNVersionListSVNVersionToAttach.getSVNVersionPK());
                attachedSVNVersionList.add(SVNVersionListSVNVersionToAttach);
            }
            revision.setSVNVersionList(attachedSVNVersionList);
            List<Dir> attachedDirList = new ArrayList<Dir>();
            for (Dir dirListDirToAttach : revision.getDirList()) {
                dirListDirToAttach = em.getReference(dirListDirToAttach.getClass(), dirListDirToAttach.getDirID());
                attachedDirList.add(dirListDirToAttach);
            }
            revision.setDirList(attachedDirList);
            em.persist(revision);
            if (SVNRepo != null) {
                SVNRepo.getRevisionList().add(revision);
                SVNRepo = em.merge(SVNRepo);
            }
            for (SVNVersion SVNVersionListSVNVersion : revision.getSVNVersionList()) {
                Revision oldRevisionOfSVNVersionListSVNVersion = SVNVersionListSVNVersion.getRevision();
                SVNVersionListSVNVersion.setRevision(revision);
                SVNVersionListSVNVersion = em.merge(SVNVersionListSVNVersion);
                if (oldRevisionOfSVNVersionListSVNVersion != null) {
                    oldRevisionOfSVNVersionListSVNVersion.getSVNVersionList().remove(SVNVersionListSVNVersion);
                    oldRevisionOfSVNVersionListSVNVersion = em.merge(oldRevisionOfSVNVersionListSVNVersion);
                }
            }
            for (Dir dirListDir : revision.getDirList()) {
                Revision oldRevisionOfDirListDir = dirListDir.getRevision();
                dirListDir.setRevision(revision);
                dirListDir = em.merge(dirListDir);
                if (oldRevisionOfDirListDir != null) {
                    oldRevisionOfDirListDir.getDirList().remove(dirListDir);
                    oldRevisionOfDirListDir = em.merge(oldRevisionOfDirListDir);
                }
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRevision(revision.getRevisionID()) != null) {
                throw new PreexistingEntityException("Revision " + revision + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void edit(Revision revision) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            Revision persistentRevision = em.find(Revision.class, revision.getRevisionID());
            SVNRepo SVNRepoOld = persistentRevision.getSVNRepo();
            SVNRepo SVNRepoNew = revision.getSVNRepo();
            List<SVNVersion> SVNVersionListOld = persistentRevision.getSVNVersionList();
            List<SVNVersion> SVNVersionListNew = revision.getSVNVersionList();
            List<Dir> dirListOld = persistentRevision.getDirList();
            List<Dir> dirListNew = revision.getDirList();
            List<String> illegalOrphanMessages = null;
            for (SVNVersion SVNVersionListOldSVNVersion : SVNVersionListOld) {
                if (!SVNVersionListNew.contains(SVNVersionListOldSVNVersion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SVNVersion " + SVNVersionListOldSVNVersion + " since its revision field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (SVNRepoNew != null) {
                SVNRepoNew = em.getReference(SVNRepoNew.getClass(), SVNRepoNew.getSvnid());
                revision.setSVNRepo(SVNRepoNew);
            }
            List<SVNVersion> attachedSVNVersionListNew = new ArrayList<SVNVersion>();
            for (SVNVersion SVNVersionListNewSVNVersionToAttach : SVNVersionListNew) {
                SVNVersionListNewSVNVersionToAttach = em.getReference(SVNVersionListNewSVNVersionToAttach.getClass(), SVNVersionListNewSVNVersionToAttach.getSVNVersionPK());
                attachedSVNVersionListNew.add(SVNVersionListNewSVNVersionToAttach);
            }
            SVNVersionListNew = attachedSVNVersionListNew;
            revision.setSVNVersionList(SVNVersionListNew);
            List<Dir> attachedDirListNew = new ArrayList<Dir>();
            for (Dir dirListNewDirToAttach : dirListNew) {
                dirListNewDirToAttach = em.getReference(dirListNewDirToAttach.getClass(), dirListNewDirToAttach.getDirID());
                attachedDirListNew.add(dirListNewDirToAttach);
            }
            dirListNew = attachedDirListNew;
            revision.setDirList(dirListNew);
            revision = em.merge(revision);
            if (SVNRepoOld != null && !SVNRepoOld.equals(SVNRepoNew)) {
                SVNRepoOld.getRevisionList().remove(revision);
                SVNRepoOld = em.merge(SVNRepoOld);
            }
            if (SVNRepoNew != null && !SVNRepoNew.equals(SVNRepoOld)) {
                SVNRepoNew.getRevisionList().add(revision);
                SVNRepoNew = em.merge(SVNRepoNew);
            }
            for (SVNVersion SVNVersionListNewSVNVersion : SVNVersionListNew) {
                if (!SVNVersionListOld.contains(SVNVersionListNewSVNVersion)) {
                    Revision oldRevisionOfSVNVersionListNewSVNVersion = SVNVersionListNewSVNVersion.getRevision();
                    SVNVersionListNewSVNVersion.setRevision(revision);
                    SVNVersionListNewSVNVersion = em.merge(SVNVersionListNewSVNVersion);
                    if (oldRevisionOfSVNVersionListNewSVNVersion != null && !oldRevisionOfSVNVersionListNewSVNVersion.equals(revision)) {
                        oldRevisionOfSVNVersionListNewSVNVersion.getSVNVersionList().remove(SVNVersionListNewSVNVersion);
                        oldRevisionOfSVNVersionListNewSVNVersion = em.merge(oldRevisionOfSVNVersionListNewSVNVersion);
                    }
                }
            }
            for (Dir dirListOldDir : dirListOld) {
                if (!dirListNew.contains(dirListOldDir)) {
                    dirListOldDir.setRevision(null);
                    dirListOldDir = em.merge(dirListOldDir);
                }
            }
            for (Dir dirListNewDir : dirListNew) {
                if (!dirListOld.contains(dirListNewDir)) {
                    Revision oldRevisionOfDirListNewDir = dirListNewDir.getRevision();
                    dirListNewDir.setRevision(revision);
                    dirListNewDir = em.merge(dirListNewDir);
                    if (oldRevisionOfDirListNewDir != null && !oldRevisionOfDirListNewDir.equals(revision)) {
                        oldRevisionOfDirListNewDir.getDirList().remove(dirListNewDir);
                        oldRevisionOfDirListNewDir = em.merge(oldRevisionOfDirListNewDir);
                    }
                }
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = revision.getRevisionID();
                if (findRevision(id) == null) {
                    throw new NonexistentEntityException("The revision with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            Revision revision;
            try {
                revision = em.getReference(Revision.class, id);
                revision.getRevisionID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The revision with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SVNVersion> SVNVersionListOrphanCheck = revision.getSVNVersionList();
            for (SVNVersion SVNVersionListOrphanCheckSVNVersion : SVNVersionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Revision (" + revision + ") cannot be destroyed since the SVNVersion " + SVNVersionListOrphanCheckSVNVersion + " in its SVNVersionList field has a non-nullable revision field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SVNRepo SVNRepo = revision.getSVNRepo();
            if (SVNRepo != null) {
                SVNRepo.getRevisionList().remove(revision);
                SVNRepo = em.merge(SVNRepo);
            }
            List<Dir> dirList = revision.getDirList();
            for (Dir dirListDir : dirList) {
                dirListDir.setRevision(null);
                dirListDir = em.merge(dirListDir);
            }
            em.remove(revision);
            //em.getTransaction().commit();
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public List<Revision> findRevisionEntities() {
        return findRevisionEntities(true, -1, -1);
    }

    public List<Revision> findRevisionEntities(int maxResults, int firstResult) {
        return findRevisionEntities(false, maxResults, firstResult);
    }

    private List<Revision> findRevisionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Revision.class));
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

    public Revision findRevision(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Revision.class, id);
        } finally {
            //em.close();
        }
    }

    public int getRevisionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Revision> rt = cq.from(Revision.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            //em.close();
        }
    }
    
    public long getNextId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Revision.getMaxId");           
            return ((Long) q.getSingleResult()) + 1;          
        }
        catch(Exception ex)
        {
            return 1;
        }
        finally {
            //em.close();
        }
    }

}
