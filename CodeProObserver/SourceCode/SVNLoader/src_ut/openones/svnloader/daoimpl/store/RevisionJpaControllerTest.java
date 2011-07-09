package openones.svnloader.daoimpl.store;

import static org.junit.Assert.*;

import openones.svnloader.daoimpl.store.RevisionJpaController;

import org.junit.Test;

public class RevisionJpaControllerTest {

    @Test
    public void testRevisionJpaController() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetEntityManager() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreate() {
        fail("Not yet implemented");
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
    public void testFindRevisionEntities() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindRevisionEntitiesIntInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindRevision() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetRevisionCount() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetNextId() {
        RevisionJpaController jpa = new RevisionJpaController();
        try
        {
            long nextId=jpa.getNextId();
            assertEquals(1, nextId);
        }
        catch(Exception ex)
        {
            fail();
        }
    }

}
