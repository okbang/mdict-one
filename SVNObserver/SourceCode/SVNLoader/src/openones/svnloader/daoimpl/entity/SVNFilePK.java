/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import openones.svnloader.dao.entity.ISVNFilePK;

/**
 *
 */
@Embeddable
public class SVNFilePK implements ISVNFilePK, Serializable {
    @Basic(optional = false)
    @Column(name = "DirID")
    private int dirID;
    @Basic(optional = false)
    @Column(name = "FileName")
    private String fileName;
    @Basic(optional = false)
    @Column(name = "CreateRevision")
    private long createRevision;

    public SVNFilePK() {
    }

    public SVNFilePK(int dirID, String fileName, long createRevision) {
        this.dirID = dirID;
        this.fileName = fileName;
        this.createRevision = createRevision;
    }

    public int getDirID() {
        return dirID;
    }

    public void setDirID(int dirID) {
        this.dirID = dirID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getCreateRevision() {
        return createRevision;
    }

    public void setCreateRevision(long createRevision) {
        this.createRevision = createRevision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) dirID;
        hash += (fileName != null ? fileName.hashCode() : 0);
        hash += (int) createRevision;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SVNFilePK)) {
            return false;
        }
        SVNFilePK other = (SVNFilePK) object;
        if (this.dirID != other.dirID) {
            return false;
        }
        if ((this.fileName == null && other.fileName != null) || (this.fileName != null && !this.fileName.equals(other.fileName))) {
            return false;
        }
        if (this.createRevision != other.createRevision) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[dirID=" + dirID + ", fileName=" + fileName + ", createRevision=" + createRevision + "]";
    }

}
