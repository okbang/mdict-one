package openones.svnloader.daoimpl.store;

import static org.junit.Assert.*;

import java.util.List;

import openones.svnloader.daoimpl.entity.Dir;
import openones.svnloader.daoimpl.store.DirJpaController;

import org.junit.Test;

public class DirJpaControllerTest {

    @Test
    public void testDirJpaController() {
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
    public void testFindDirEntities() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindDirEntitiesIntInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindDir() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetDirCount() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetNextId() {
        DirJpaController jpa = new DirJpaController();
        try {
            int nextId = jpa.getNextId();
            assertEquals(1, nextId);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void testFindByParentPathAndName() {
        DirJpaController jpa = new DirJpaController();
        try {
            List<Dir> target = jpa.findByParentPathAndName("$", null);
            System.out.println("List of dir:");
            for (Dir dir : target) {
                System.out.println(dir.getDirID());
            }
            assertNotNull(target);
        } catch (Exception ex) {
            fail();
        }
    }
    
    @Test
    public void testFindByParentPathAndName02() {
        DirJpaController jpa = new DirJpaController();
        try {
            List<Dir> target = jpa.findByParentPathAndName("Project01", "$");
            System.out.println("List of dir:");
            for (Dir dir : target) {
                System.out.println(dir.getDirID() + "," + dir.getDirName());
            }
            assertNotNull(target);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void testFindDirEntities1() {
        DirJpaController jpa = new DirJpaController();
        try {
            List<Dir> target = jpa.findDirEntities(1);
            System.out.println("List of dir:");
            for (Dir dir : target) {
                System.out.println(dir.getDirID() + " Path: " + dir.getParentPath() + "/" + dir.getDirName()
                        + " -SVNID: " + dir.getSVNRepo().getSvnid());
            }
            assertNotNull(target);
        } catch (Exception ex) {
            fail();
        }
    }

}
