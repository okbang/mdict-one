/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import openones.svnloader.dao.entity.IRevision;
import openones.svnloader.dao.entity.ISVNRepo;

/**
 *
 */
@Entity
@Table(name = "Revision")
@NamedQueries({
    @NamedQuery(name = "Revision.getMaxId", query = "SELECT max(r.revisionID) FROM Revision r"),
    @NamedQuery(name = "Revision.findAll", query = "SELECT r FROM Revision r"),
    @NamedQuery(name = "Revision.findByRevisionID", query = "SELECT r FROM Revision r WHERE r.revisionID = :revisionID"),
    @NamedQuery(name = "Revision.findByRevisionNum", query = "SELECT r FROM Revision r WHERE r.revisionNum = :revisionNum"),
    @NamedQuery(name = "Revision.findByAuthor", query = "SELECT r FROM Revision r WHERE r.author = :author"),
    @NamedQuery(name = "Revision.findByDateLog", query = "SELECT r FROM Revision r WHERE r.dateLog = :dateLog"),
    @NamedQuery(name = "Revision.findByComment", query = "SELECT r FROM Revision r WHERE r.comment = :comment")})
public class Revision implements IRevision, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RevisionID")
    private Long revisionID;
    @Basic(optional = false)
    @Column(name = "RevisionNum")
    private long revisionNum;
    @Column(name = "Author")
    private String author;
    @Column(name = "DateLog")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLog;
    @Column(name = "Comment")
    private String comment;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "revision")
    private List<SVNVersion> sVNVersionList;
    @OneToMany(mappedBy = "revision")
    private List<Dir> dirList;
    @JoinColumn(name = "SVNID", referencedColumnName = "SVNID")
    @ManyToOne(optional = false)
    private SVNRepo sVNRepo;

    public Revision() {
    }

    public Revision(Long revisionID) {
        this.revisionID = revisionID;
    }

    public Revision(Long revisionID, long revisionNum) {
        this.revisionID = revisionID;
        this.revisionNum = revisionNum;
    }

    public Long getRevisionID() {
        return revisionID;
    }

    public void setRevisionID(Long revisionID) {
        this.revisionID = revisionID;
    }

    public long getRevisionNum() {
        return revisionNum;
    }

    public void setRevisionNum(long revisionNum) {
        this.revisionNum = revisionNum;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDateLog() {
        return dateLog;
    }

    @Override
    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SVNVersion> getSVNVersionList() {
        return sVNVersionList;
    }

    public void setSVNVersionList(List<SVNVersion> sVNVersionList) {
        this.sVNVersionList = sVNVersionList;
    }

    public List<Dir> getDirList() {
        return dirList;
    }

    public void setDirList(List<Dir> dirList) {
        this.dirList = dirList;
    }

    public SVNRepo getSVNRepo() {
        return sVNRepo;
    }

    @Override
    public void setSVNRepo(ISVNRepo sVNRepo) {
        this.sVNRepo = (SVNRepo) sVNRepo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (revisionID != null ? revisionID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Revision)) {
            return false;
        }
        Revision other = (Revision) object;
        if ((this.revisionID == null && other.revisionID != null) || (this.revisionID != null && !this.revisionID.equals(other.revisionID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[revisionID=" + revisionID + "]";
    }

}
