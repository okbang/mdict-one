/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "SVNRepo")
@NamedQueries({
    @NamedQuery(name = "SVNRepo.getMaxId", query = "SELECT max(s.svnid) FROM SVNRepo s"),
    @NamedQuery(name = "SVNRepo.findAll", query = "SELECT s FROM SVNRepo s"),
    @NamedQuery(name = "SVNRepo.findBySvnid", query = "SELECT s FROM SVNRepo s WHERE s.svnid = :svnid"),
    @NamedQuery(name = "SVNRepo.findByUrl", query = "SELECT s FROM SVNRepo s WHERE s.url = :url"),
    @NamedQuery(name = "SVNRepo.findByLastestRevisionID", query = "SELECT s FROM SVNRepo s WHERE s.lastestRevisionID = :lastestRevisionID"),
    @NamedQuery(name = "SVNRepo.findByProjectCode", query = "SELECT s FROM SVNRepo s WHERE s.projectCode = :projectCode")})
public class SVNRepo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SVNID")
    private Integer svnid;
    @Basic(optional = false)
    @Column(name = "URL")
    private String url;
    @Column(name = "LastestRevisionID")
    private BigInteger lastestRevisionID;
    @Basic(optional = false)
    @Column(name = "ProjectCode")
    private String projectCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sVNRepo")
    private List<Dir> dirList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sVNRepo")
    private List<Revision> revisionList;

    public SVNRepo() {
    }

    public SVNRepo(Integer svnid) {
        this.svnid = svnid;
    }

    public SVNRepo(Integer svnid, String url, String projectCode) {
        this.svnid = svnid;
        this.url = url;
        this.projectCode = projectCode;
    }

    public Integer getSvnid() {
        return svnid;
    }

    public void setSvnid(Integer svnid) {
        this.svnid = svnid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigInteger getLastestRevisionID() {
        return lastestRevisionID;
    }

    public void setLastestRevisionID(BigInteger lastestRevisionID) {
        this.lastestRevisionID = lastestRevisionID;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public List<Dir> getDirList() {
        return dirList;
    }

    public void setDirList(List<Dir> dirList) {
        this.dirList = dirList;
    }

    public List<Revision> getRevisionList() {
        return revisionList;
    }

    public void setRevisionList(List<Revision> revisionList) {
        this.revisionList = revisionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (svnid != null ? svnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SVNRepo)) {
            return false;
        }
        SVNRepo other = (SVNRepo) object;
        if ((this.svnid == null && other.svnid != null) || (this.svnid != null && !this.svnid.equals(other.svnid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[svnid=" + svnid + "]";
    }

}
