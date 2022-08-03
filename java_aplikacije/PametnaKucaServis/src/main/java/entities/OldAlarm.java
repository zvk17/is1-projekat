/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user2
 */
@Entity
@Table(name = "old_alarms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OldAlarm.findAll", query = "SELECT o FROM OldAlarm o"),
    @NamedQuery(name = "OldAlarm.findByIdOldAlarm", query = "SELECT o FROM OldAlarm o WHERE o.idOldAlarm = :idOldAlarm"),
    @NamedQuery(name = "OldAlarm.findByDatetimeMoment", query = "SELECT o FROM OldAlarm o WHERE o.datetimeMoment = :datetimeMoment")})
public class OldAlarm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idOldAlarm")
    private Integer idOldAlarm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datetimeMoment")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeMoment;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User idUser;

    public OldAlarm() {
    }

    public OldAlarm(Integer idOldAlarm) {
        this.idOldAlarm = idOldAlarm;
    }

    public OldAlarm(Integer idOldAlarm, Date datetimeMoment) {
        this.idOldAlarm = idOldAlarm;
        this.datetimeMoment = datetimeMoment;
    }

    public Integer getIdOldAlarm() {
        return idOldAlarm;
    }

    public void setIdOldAlarm(Integer idOldAlarm) {
        this.idOldAlarm = idOldAlarm;
    }

    public Date getDatetimeMoment() {
        return datetimeMoment;
    }

    public void setDatetimeMoment(Date datetimeMoment) {
        this.datetimeMoment = datetimeMoment;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOldAlarm != null ? idOldAlarm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OldAlarm)) {
            return false;
        }
        OldAlarm other = (OldAlarm) object;
        if ((this.idOldAlarm == null && other.idOldAlarm != null) || (this.idOldAlarm != null && !this.idOldAlarm.equals(other.idOldAlarm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.OldAlarm[ idOldAlarm=" + idOldAlarm + " ]";
    }
    
}
