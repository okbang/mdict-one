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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "Dir")
@NamedQueries({
    @NamedQuery(name = "Dir.getMaxId", query = "SELECT max(d.dirID) FROM Dir d"),
    @NamedQuery(name = "Dir.findAll", query = "SELECT d FROM Dir d"),
    @NamedQuery(name = "Dir.findByDirID", query = "SELECT d FROM Dir d WHERE d.dirID = :dirID"),
    @NamedQuery(name = "Dir.findByDirName", query = "SELECT d FROM Dir d WHERE d.dirName = :dirName"),
    @NamedQuery(name = "Dir.findByParentDirID", query = "SELECT d FROM Dir d WHERE d.parentDirID = :parentDirID"),
    @NamedQuery(name = "Dir.findByParentPath", query = "SELECT d FROM Dir d WHERE d.parentPath = :parentPath"),
    @NamedQuery(name = "Dir.findByStatus", query = "SELECT d FROM Dir d WHERE d.status = :status"),
    @NamedQuery(name = "Dir.findByCopyFormPath", query = "SELECT d FROM Dir d WHERE d.copyFormPath = :copyFormPath"),
    @NamedQuery(name = "Dir.findByCopyRevision", query = "SELECT d FROM Dir d WHERE d.copyRevision = :copyRevision"),
    @NamedQuery(name = "Dir.findByParentPathAndName", query = "SELECT d FROM Dir d WHERE d.dirName = :dirName AND d.parentPath = :parentPath AND d.deletedRevisionID IS NULL "),
    @NamedQuery(name = "Dir.findByDeletedRevisionID", query = "SELECT d FROM Dir d WHERE d.deletedRevisionID = :deletedRevisionID")})
public class Dir implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DirID")
    private Integer dirID;
    @Column(name = "DirName")
    private String dirName;
    @Column(name = "ParentDirID")
    private Integer parentDirID;
    @Column(name = "ParentPath")
    private String parentPath;
    @Column(name = "Status")
    private Integer status;
    @Column(name = "CopyFormPath")
    private String copyFormPath;
    @Column(name = "CopyRevision")
    private BigInteger copyRevision;
    @Column(name = "DeletedRevisionID")
    private BigInteger deletedRevisionID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dir")
    private List<SVNVersion> sVNVersionList;
    @JoinColumn(name = "SVNID", referencedColumnName = "SVNID")
    @ManyToOne(optional = false)
    private SVNRepo sVNRepo;
    @JoinColumn(name = "RevisionID", referencedColumnName = "RevisionID")
    @ManyToOne
    private Revision revision;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dir")
    private List<SVNFile> sVNFileList;

    public Dir() {
    }

    public Dir(Integer dirID) {
        this.dirID = dirID;
    }

    public Integer getDirID() {
        return dirID;
    }

    public void setDirID(Integer dirID) {
        this.dirID = dirID;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Integer getParentDirID() {
        return parentDirID;
    }

    public void setParentDirID(Integer parentDirID) {
        this.parentDirID = parentDirID;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCopyFormPath() {
        return copyFormPath;
    }

    public void setCopyFormPath(String copyFormPath) {
        this.copyFormPath = copyFormPath;
    }

    public BigInteger getCopyRevision() {
        return copyRevision;
    }

    public void setCopyRevision(BigInteger copyRevision) {
        this.copyRevision = copyRevision;
    }

    public BigInteger getDeletedRevisionID() {
        return deletedRevisionID;
    }

    public void setDeletedRevisionID(BigInteger deletedRevisionID) {
        this.deletedRevisionID = deletedRevisionID;
    }

    public List<SVNVersion> getSVNVersionList() {
        return sVNVersionList;
    }

    public void setSVNVersionList(List<SVNVersion> sVNVersionList) {
        this.sVNVersionList = sVNVersionList;
    }

    public SVNRepo getSVNRepo() {
        return sVNRepo;
    }

    public void setSVNRepo(SVNRepo sVNRepo) {
        this.sVNRepo = sVNRepo;
    }

    public Revision getRevision() {
        return revision;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public List<SVNFile> getSVNFileList() {
        return sVNFileList;
    }

    public void setSVNFileList(List<SVNFile> sVNFileList) {
        this.sVNFileList = sVNFileList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dirID != null ? dirID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dir)) {
            return false;
        }
        Dir other = (Dir) object;
        if ((this.dirID == null && other.dirID != null) || (this.dirID != null && !this.dirID.equals(other.dirID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[dirID=" + dirID + "]";
    }

}
