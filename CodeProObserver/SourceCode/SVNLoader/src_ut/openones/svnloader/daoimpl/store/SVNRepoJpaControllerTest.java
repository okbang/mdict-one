package openones.svnloader.daoimpl.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.store.SVNRepoJpaController;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;

import org.junit.Test;


public class SVNRepoJpaControllerTest {

    @Test
    public void testSVNRepoJpaController() {
        SVNRepoJpaController jpa = new SVNRepoJpaController();
        assertNotNull(jpa);
    }

    @Test
    public void testGetEntityManager() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreate() {
        SVNRepoJpaController jpa = new SVNRepoJpaController();
        SVNRepo svnRepo = new SVNRepo(0, "file:///D:/repository test", "");
        try {
            jpa.create(svnRepo);
        } catch (PreexistingEntityException e) {
           
            e.printStackTrace();
            fail();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void textGetMaxID()
    {
        SVNRepoJpaController jpa = new SVNRepoJpaController();
        
        try {
            int item = jpa.getNextId();
            assertEquals(1, item);       
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    
    }

    @Test
    public void testEdit() {
        fail("Not yet implemented");
    }

    @Test
    public void testDestroy() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindSVNRepoEntities() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindSVNRepoEntitiesIntInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindSVNRepo() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetSVNRepoCount() {
        fail("Not yet implemented");
    }

}
