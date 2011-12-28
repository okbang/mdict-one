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
import openones.svnloader.daoimpl.store.exceptions.IllegalOrphanException;
import openones.svnloader.daoimpl.store.exceptions.NonexistentEntityException;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;


/**
 *
 */
public class SVNRepoJpaController {

    public EntityManager getEntityManager() {
        return PersistentManager.getEntityManager();
    }

    public void create(SVNRepo SVNRepo) throws PreexistingEntityException, Exception {
        if (SVNRepo.getDirList() == null) {
            SVNRepo.setDirList(new ArrayList<Dir>());
        }
        if (SVNRepo.getRevisionList() == null) {
            SVNRepo.setRevisionList(new ArrayList<Revision>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            List<Dir> attachedDirList = new ArrayList<Dir>();
            for (Dir dirListDirToAttach : SVNRepo.getDirList()) {
                dirListDirToAttach = em.getReference(dirListDirToAttach.getClass(), dirListDirToAttach.getDirID());
                attachedDirList.add(dirListDirToAttach);
            }
            SVNRepo.setDirList(attachedDirList);
            List<Revision> attachedRevisionList = new ArrayList<Revision>();
            for (Revision revisionListRevisionToAttach : SVNRepo.getRevisionList()) {
                revisionListRevisionToAttach = em.getReference(revisionListRevisionToAttach.getClass(), revisionListRevisionToAttach.getRevisionID());
                attachedRevisionList.add(revisionListRevisionToAttach);
            }
            SVNRepo.setRevisionList(attachedRevisionList);
            em.persist(SVNRepo);
            for (Dir dirListDir : SVNRepo.getDirList()) {
                SVNRepo oldSVNRepoOfDirListDir = dirListDir.getSVNRepo();
                dirListDir.setSVNRepo(SVNRepo);
                dirListDir = em.merge(dirListDir);
                if (oldSVNRepoOfDirListDir != null) {
                    oldSVNRepoOfDirListDir.getDirList().remove(dirListDir);
                    oldSVNRepoOfDirListDir = em.merge(oldSVNRepoOfDirListDir);
                }
            }
            for (Revision revisionListRevision : SVNRepo.getRevisionList()) {
                SVNRepo oldSVNRepoOfRevisionListRevision = revisionListRevision.getSVNRepo();
                revisionListRevision.setSVNRepo(SVNRepo);
                revisionListRevision = em.merge(revisionListRevision);
                if (oldSVNRepoOfRevisionListRevision != null) {
                    oldSVNRepoOfRevisionListRevision.getRevisionList().remove(revisionListRevision);
                    oldSVNRepoOfRevisionListRevision = em.merge(oldSVNRepoOfRevisionListRevision);
                }
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSVNRepo(SVNRepo.getSvnid()) != null) {
                throw new PreexistingEntityException("SVNRepo " + SVNRepo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void edit(SVNRepo SVNRepo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNRepo persistentSVNRepo = em.find(SVNRepo.class, SVNRepo.getSvnid());
            List<Dir> dirListOld = persistentSVNRepo.getDirList();
            List<Dir> dirListNew = SVNRepo.getDirList();
            List<Revision> revisionListOld = persistentSVNRepo.getRevisionList();
            List<Revision> revisionListNew = SVNRepo.getRevisionList();
            List<String> illegalOrphanMessages = null;
            /*for (Dir dirListOldDir : dirListOld) {
                if (!dirListNew.contains(dirListOldDir)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dir " + dirListOldDir + " since its SVNRepo field is not nullable.");
                }
            }
            for (Revision revisionListOldRevision : revisionListOld) {
                if (!revisionListNew.contains(revisionListOldRevision)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Revision " + revisionListOldRevision + " since its SVNRepo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }*/
            List<Dir> attachedDirListNew = new ArrayList<Dir>();
            for (Dir dirListNewDirToAttach : dirListNew) {
                dirListNewDirToAttach = em.getReference(dirListNewDirToAttach.getClass(), dirListNewDirToAttach.getDirID());
                attachedDirListNew.add(dirListNewDirToAttach);
            }
            dirListNew = attachedDirListNew;
            SVNRepo.setDirList(dirListNew);
            List<Revision> attachedRevisionListNew = new ArrayList<Revision>();
            for (Revision revisionListNewRevisionToAttach : revisionListNew) {
                revisionListNewRevisionToAttach = em.getReference(revisionListNewRevisionToAttach.getClass(), revisionListNewRevisionToAttach.getRevisionID());
                attachedRevisionListNew.add(revisionListNewRevisionToAttach);
            }
            revisionListNew = attachedRevisionListNew;
            SVNRepo.setRevisionList(revisionListNew);
            SVNRepo = em.merge(SVNRepo);
            for (Dir dirListNewDir : dirListNew) {
                if (!dirListOld.contains(dirListNewDir)) {
                    SVNRepo oldSVNRepoOfDirListNewDir = dirListNewDir.getSVNRepo();
                    dirListNewDir.setSVNRepo(SVNRepo);
                    dirListNewDir = em.merge(dirListNewDir);
                    if (oldSVNRepoOfDirListNewDir != null && !oldSVNRepoOfDirListNewDir.equals(SVNRepo)) {
                        oldSVNRepoOfDirListNewDir.getDirList().remove(dirListNewDir);
                        oldSVNRepoOfDirListNewDir = em.merge(oldSVNRepoOfDirListNewDir);
                    }
                }
            }
            for (Revision revisionListNewRevision : revisionListNew) {
                if (!revisionListOld.contains(revisionListNewRevision)) {
                    SVNRepo oldSVNRepoOfRevisionListNewRevision = revisionListNewRevision.getSVNRepo();
                    revisionListNewRevision.setSVNRepo(SVNRepo);
                    revisionListNewRevision = em.merge(revisionListNewRevision);
                    if (oldSVNRepoOfRevisionListNewRevision != null && !oldSVNRepoOfRevisionListNewRevision.equals(SVNRepo)) {
                        oldSVNRepoOfRevisionListNewRevision.getRevisionList().remove(revisionListNewRevision);
                        oldSVNRepoOfRevisionListNewRevision = em.merge(oldSVNRepoOfRevisionListNewRevision);
                    }
                }
            }
            //em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = SVNRepo.getSvnid();
                if (findSVNRepo(id) == null) {
                    throw new NonexistentEntityException("The sVNRepo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            SVNRepo SVNRepo;
            try {
                SVNRepo = em.getReference(SVNRepo.class, id);
                SVNRepo.getSvnid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The SVNRepo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Dir> dirListOrphanCheck = SVNRepo.getDirList();
            for (Dir dirListOrphanCheckDir : dirListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SVNRepo (" + SVNRepo + ") cannot be destroyed since the Dir " + dirListOrphanCheckDir + " in its dirList field has a non-nullable SVNRepo field.");
            }
            List<Revision> revisionListOrphanCheck = SVNRepo.getRevisionList();
            for (Revision revisionListOrphanCheckRevision : revisionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SVNRepo (" + SVNRepo + ") cannot be destroyed since the Revision " + revisionListOrphanCheckRevision + " in its revisionList field has a non-nullable SVNRepo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(SVNRepo);
           /// em.getTransaction().commit();
        } finally {
            if (em != null) {
                //em.close();
            }
        }
    }

    public List<SVNRepo> findSVNRepoEntities() {
        return findSVNRepoEntities(true, -1, -1);
    }

    public List<SVNRepo> findSVNRepoEntities(int maxResults, int firstResult) {
        return findSVNRepoEntities(false, maxResults, firstResult);
    }

    private List<SVNRepo> findSVNRepoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SVNRepo.class));
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

    public SVNRepo findSVNRepo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SVNRepo.class, id);
        } finally {
            //em.close();
        }
    }

    public int getSVNRepoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SVNRepo> rt = cq.from(SVNRepo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            //em.close();
        }
    }
    
    public int getNextId() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("SVNRepo.getMaxId");           
            return ((Integer) q.getSingleResult()) + 1;          
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
