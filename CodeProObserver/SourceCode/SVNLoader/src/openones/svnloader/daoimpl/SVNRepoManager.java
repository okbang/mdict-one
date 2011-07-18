package openones.svnloader.daoimpl;

import java.math.BigInteger;
import java.util.List;

import openones.svnloader.dao.ISVNRepoManager;
import openones.svnloader.dao.entity.ISVNRepo;
import openones.svnloader.daoimpl.entity.SVNRepo;
import openones.svnloader.daoimpl.store.SVNRepoJpaController;


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
    public ISVNRepo createSVNRepo(String url, long revisionID) throws Exception {
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
    public ISVNRepo createSVNRepo(String url) throws Exception {
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
    public ISVNRepo createSVNRepo(String url, String projectCode) throws Exception {
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
    public void updateSVNRepo(Object repo) throws Exception {
        jpaController.edit((SVNRepo) repo);
    }

    /**
     * [Explain the description for this method here].
     * @param URL
     * @return
     * @see openones.svnloader.engine.manager.ISVNRepoManager#findRepoByURL(java.lang.String)
     */
    @Override
    public ISVNRepo findRepoByURL(String URL) {
        ISVNRepo target = null;
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
