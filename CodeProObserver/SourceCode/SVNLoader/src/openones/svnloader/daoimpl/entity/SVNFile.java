/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "SVNFile")
@NamedQueries({
    @NamedQuery(name = "SVNFile.findAll", query = "SELECT s FROM SVNFile s"),
    @NamedQuery(name = "SVNFile.findByDirID", query = "SELECT s FROM SVNFile s WHERE s.sVNFilePK.dirID = :dirID"),
    @NamedQuery(name = "SVNFile.findByFileName", query = "SELECT s FROM SVNFile s WHERE s.sVNFilePK.fileName = :fileName"),
    @NamedQuery(name = "SVNFile.findByCreateRevision", query = "SELECT s FROM SVNFile s WHERE s.sVNFilePK.createRevision = :createRevision")})
public class SVNFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SVNFilePK sVNFilePK;
    @JoinColumn(name = "DirID", referencedColumnName = "DirID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Dir dir;

    public SVNFile() {
    }

    public SVNFile(SVNFilePK sVNFilePK) {
        this.sVNFilePK = sVNFilePK;
    }

    public SVNFile(int dirID, String fileName, long createRevision) {
        this.sVNFilePK = new SVNFilePK(dirID, fileName, createRevision);
    }

    public SVNFilePK getSVNFilePK() {
        return sVNFilePK;
    }

    public void setSVNFilePK(SVNFilePK sVNFilePK) {
        this.sVNFilePK = sVNFilePK;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sVNFilePK != null ? sVNFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SVNFile)) {
            return false;
        }
        SVNFile other = (SVNFile) object;
        if ((this.sVNFilePK == null && other.sVNFilePK != null) || (this.sVNFilePK != null && !this.sVNFilePK.equals(other.sVNFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[sVNFilePK=" + sVNFilePK + "]";
    }

}
