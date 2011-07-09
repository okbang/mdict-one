package openones.svnloader.daoimpl;

import java.math.BigInteger;
import java.util.List;

import openones.svnloader.dao.ISVNRepoManager;
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.store.SVNRepoJpaController;
import openones.svnloader.daoimpl.store.exceptions.PreexistingEntityException;


public class SVNRepoManager implements ISVNRepoManager {
    private SVNRepoJpaController jpaController;

    public SVNRepoManager() {
        jpaController = new SVNRepoJpaController();
    }

    /**
     * [Explain the description for this method here].
     * @param url
     * @param revisionID
     * @return
     * @throws Exception
     * @see openones.svnloader.engine.manager.ISVNRepoManager#createSVNRepo(java.lang.String, long)
     */
    @Override
    public SVNRepo createSVNRepo(String url, long revisionID) throws Exception {
        int id = jpaController.getNextId();
        SVNRepo repo;
        try {
            repo = new SVNRepo(id, url, "");
            repo.setLastestRevisionID(BigInteger.valueOf(revisionID));
            jpaController.create(repo);
        } catch (Exception ex) {
            throw ex;
        }
        return repo;

    }
    
    /**
     * [Explain the description for this method here].
     * @param url
     * @return
     * @throws Exception
     * @see openones.svnloader.engine.manager.ISVNRepoManager#createSVNRepo(java.lang.String)
     */
    @Override
    public SVNRepo createSVNRepo(String url) throws Exception {
        int id = jpaController.getNextId();
        SVNRepo repo;
        try {
            repo = new SVNRepo(id, url, "");
            repo.setLastestRevisionID(BigInteger.valueOf(-1));
            jpaController.create(repo);
        } catch (Exception ex) {
            throw ex;
        }
        return repo;
    }

    /**
     * [Explain the description for this method here].
     * @param url
     * @param projectCode
     * @return
     * @throws Exception
     * @see openones.svnloader.engine.manager.ISVNRepoManager#createSVNRepo(java.lang.String, java.lang.String)
     */
    @Override
    public SVNRepo createSVNRepo(String url, String projectCode) throws Exception {
        int id = jpaController.getNextId();
        SVNRepo repo;
        try {
            repo = new SVNRepo(id, url, "");
            repo.setLastestRevisionID(BigInteger.valueOf(-1));
            repo.setProjectCode(projectCode);
            jpaController.create(repo);
        } catch (Exception ex) {
            throw ex;
        }
        return repo;
    }
    @Override
    public void updateSVNRepo(SVNRepo repo) throws PreexistingEntityException, Exception {
        jpaController.edit(repo);
    }

    /**
     * [Explain the description for this method here].
     * @param URL
     * @return
     * @see openones.svnloader.engine.manager.ISVNRepoManager#findRepoByURL(java.lang.String)
     */
    @Override
    public SVNRepo findRepoByURL(String URL) {
        SVNRepo target = null;
        List<SVNRepo> listRepo = jpaController.findSVNRepoEntities();
        for (SVNRepo svnRepo : listRepo) {
            if (svnRepo.getUrl().equals(URL)) {
                target = svnRepo;
                break;
            }
        }
        return target;
    }

}
