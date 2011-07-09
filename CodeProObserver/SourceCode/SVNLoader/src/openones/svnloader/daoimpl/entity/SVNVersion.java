/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openones.svnloader.daoimpl.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "SVNVersion")
@NamedQueries({
    @NamedQuery(name = "SVNVersion.findAll", query = "SELECT s FROM SVNVersion s"),
    @NamedQuery(name = "SVNVersion.findByDirID", query = "SELECT s FROM SVNVersion s WHERE s.sVNVersionPK.dirID = :dirID"),
    @NamedQuery(name = "SVNVersion.findByFileName", query = "SELECT s FROM SVNVersion s WHERE s.sVNVersionPK.fileName = :fileName"),
    @NamedQuery(name = "SVNVersion.findBySVNAction", query = "SELECT s FROM SVNVersion s WHERE s.sVNAction = :sVNAction"),
    @NamedQuery(name = "SVNVersion.findByNMComment", query = "SELECT s FROM SVNVersion s WHERE s.nMComment = :nMComment"),
    @NamedQuery(name = "SVNVersion.findByRevisionID", query = "SELECT s FROM SVNVersion s WHERE s.sVNVersionPK.revisionID = :revisionID"),
    @NamedQuery(name = "SVNVersion.findByCopyFromPath", query = "SELECT s FROM SVNVersion s WHERE s.copyFromPath = :copyFromPath"),
    @NamedQuery(name = "SVNVersion.findByCopyRevision", query = "SELECT s FROM SVNVersion s WHERE s.copyRevision = :copyRevision"),
    @NamedQuery(name = "SVNVersion.findByNmStaticBug", query = "SELECT s FROM SVNVersion s WHERE s.nmStaticBug = :nmStaticBug"),
    @NamedQuery(name = "SVNVersion.findByNmUTBug", query = "SELECT s FROM SVNVersion s WHERE s.nmUTBug = :nmUTBug"),
    @NamedQuery(name = "SVNVersion.findByEffort", query = "SELECT s FROM SVNVersion s WHERE s.effort = :effort")})
public class SVNVersion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SVNVersionPK sVNVersionPK;
    @Basic(optional = false)
    @Column(name = "SVNAction")
    private char sVNAction;
    
    @Column(name = "Size")
    private BigInteger size;
    
    @Column(name = "Unit")
    private String unit;
    
    @Column(name = "Size1")
    private BigInteger size1;
    
    @Column(name = "Unit1")
    private String unit1;
    
    @Column(name = "Size2")
    private BigInteger size2;
    
    @Column(name = "Unit2")
    private String unit2;
    
    @Column(name = "NMComment")
    private BigInteger nMComment;
    @Column(name = "CopyFromPath")
    private String copyFromPath;
    @Column(name = "CopyRevision")
    private BigInteger copyRevision;
    @Column(name = "NMStaticBug")
    private BigInteger nmStaticBug;

    @Column(name = "NMUTBug")
    private BigInteger nmUTBug;

    @Column(name = "Effort")
    private Double effort;
    @JoinColumn(name = "RevisionID", referencedColumnName = "RevisionID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Revision revision;
    @JoinColumn(name = "DirID", referencedColumnName = "DirID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Dir dir;

    public SVNVersion() {
    }

    public SVNVersion(SVNVersionPK sVNVersionPK) {
        this.sVNVersionPK = sVNVersionPK;
    }

    public SVNVersion(SVNVersionPK sVNVersionPK, char sVNAction) {
        this.sVNVersionPK = sVNVersionPK;
        this.sVNAction = sVNAction;
    }

    public SVNVersion(int dirID, String fileName, long revisionID) {
        this.sVNVersionPK = new SVNVersionPK(dirID, fileName, revisionID);
    }

    public SVNVersionPK getSVNVersionPK() {
        return sVNVersionPK;
    }

    public void setSVNVersionPK(SVNVersionPK sVNVersionPK) {
        this.sVNVersionPK = sVNVersionPK;
    }

    public char getSVNAction() {
        return sVNAction;
    }

    public void setSVNAction(char sVNAction) {
        this.sVNAction = sVNAction;
    }

    public BigInteger getNMComment() {
        return nMComment;
    }

    public void setNMComment(BigInteger nMComment) {
        this.nMComment = nMComment;
    }
    
    public void setNMComment(int nMComment) {
        this.nMComment = BigInteger.valueOf(nMComment);
    }

    public String getCopyFromPath() {
        return copyFromPath;
    }

    public void setCopyFromPath(String copyFromPath) {
        this.copyFromPath = copyFromPath;
    }

    public BigInteger getCopyRevision() {
        return copyRevision;
    }

    public void setCopyRevision(BigInteger copyRevision) {
        this.copyRevision = copyRevision;
    }

    public Revision getRevision() {
        return revision;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public BigInteger getnMComment() {
        return nMComment;
    }

    public void setnMComment(BigInteger nMComment) {
        this.nMComment = nMComment;
    }

    public BigInteger getNmStaticBug() {
        return nmStaticBug;
    }

    public void setNmStaticBug(BigInteger nmStaticBug) {
        this.nmStaticBug = nmStaticBug;
    }
    
    public void setNmStaticBug(int nmStaticBug) {
        this.nmStaticBug = BigInteger.valueOf(nmStaticBug);
    }
    
    public BigInteger getNmUTBug() {
        return nmUTBug;
    }

    public void setNmUTBug(BigInteger nmUTBug) {
        this.nmUTBug = nmUTBug;
    }
    
    public void setNmUTBug(int nmUTBug) {
        this.nmUTBug = BigInteger.valueOf(nmUTBug);
    }
    
    public Double getEffort() {
        return effort;
    }

    public void setEffort(Double effort) {
        this.effort = effort;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public BigInteger getSize1() {
        return size1;
    }

    public void setSize1(BigInteger size1) {
        this.size1 = size1;
    }

    public String getUnit1() {
        return unit1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public BigInteger getSize2() {
        return size2;
    }

    public void setSize2(BigInteger size2) {
        this.size2 = size2;
    }

    public String getUnit2() {
        return unit2;
    }

    public void setUnit2(String unit2) {
        this.unit2 = unit2;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sVNVersionPK != null ? sVNVersionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SVNVersion)) {
            return false;
        }
        SVNVersion other = (SVNVersion) object;
        if ((this.sVNVersionPK == null && other.sVNVersionPK != null) || (this.sVNVersionPK != null && !this.sVNVersionPK.equals(other.sVNVersionPK))) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return this.getClass().getName() + "[sVNVersionPK=" + sVNVersionPK + "]";
    }

}
