package openones.svnloader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import openones.svnloader.daoimpl.entity.Dir;
import openones.svnloader.daoimpl.entity.Revision;
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.store.DirJpaController;
import openones.svnloader.daoimpl.store.PersistentManager;
import openones.svnloader.daoimpl.store.RevisionJpaController;
import openones.svnloader.daoimpl.store.SVNRepoJpaController;

import org.junit.Test;

public class TestJTATransaction {
    
    @Test
    public void testJTA1()
    {
        //UserTransaction transaction = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
        //transaction.begin();
        EntityManager em = PersistentManager.getEntityManager();
        em.getTransaction().begin();
        try {      
            
            SVNRepoJpaController jpa = new SVNRepoJpaController();
            SVNRepo svnRepo = new SVNRepo(0, "file:///D:/repository test", "");
            SVNRepo svnRepo2 = new SVNRepo(1, "file:///D:/repository test", "");
            jpa.create(svnRepo);
            jpa.create(svnRepo2);           
            em.getTransaction().commit();
         
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            em.getTransaction().rollback();
           
            fail();
        }
        finally{
         em.close();
        }
      
    }
    @Test
    public void testJTACreate01()
    {
        EntityManager em = PersistentManager.getEntityManager();
        SVNRepoJpaController jpa = null;
        String repoPath = "file:///D:/repository test";
        try {      
            em.getTransaction().begin();
            jpa = new SVNRepoJpaController();
            SVNRepo svnRepo = new SVNRepo(10, repoPath, "");
            jpa.create(svnRepo);
            em.getTransaction().commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           em.getTransaction().rollback();
           
            fail();
        }
        
        finally{
         em.close();
        }
      
        SVNRepo createdRepo = jpa.findSVNRepo(10);
        assertEquals(10,createdRepo.getSvnid().intValue());
        assertEquals(repoPath, createdRepo.getUrl());
        
    }
    
    @Test
    public void testJTADelete()
    {
        EntityManager em = PersistentManager.getEntityManager();
        try {      
            em.getTransaction().begin();
            SVNRepoJpaController jpa = new SVNRepoJpaController();
            jpa.destroy(10);
            em.getTransaction().commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            fail();
        }
        finally{
         em.close();
        }
      
    }
    
    @Test
    public void testJTA2()
    {
        //UserTransaction transaction = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
        //transaction.begin();
        EntityManager em = PersistentManager.getEntityManager();
        em.getTransaction().begin();
        try {      
            
            SVNRepoJpaController  svnRepoJpaController = new SVNRepoJpaController();
            DirJpaController dirJpaController  = new DirJpaController();
            RevisionJpaController revisionJpaController = new RevisionJpaController();
            SVNRepo svnRepo = new SVNRepo(2, "file:///D:/repository test", " ");
            
            Revision revision = new Revision(Long.valueOf("2"), Long.valueOf("233"));
            revision.setAuthor("OpenOnesAdm");
            revision.setComment("Code test");
            revision.setSVNRepo(svnRepo);
            
            //construct Dir
            Dir dir = new Dir(2);
            dir.setDirName("ProgNews");
            dir.setRevision(revision);
            dir.setStatus(0);
            dir.setSVNRepo(svnRepo);
            
            svnRepoJpaController.create(svnRepo);
            revisionJpaController.create(revision);
            dirJpaController.create(dir);
             
            em.getTransaction().commit();
         
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            em.getTransaction().rollback();
           
            fail();
        }
        finally{
         em.close();
        }
      
    }
}
