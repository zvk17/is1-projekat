/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user2
 */
@Entity
@Table(name = "alarms")
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a"),
    @NamedQuery(name = "Alarm.findByIdAlarm", query = "SELECT a FROM Alarm a WHERE a.idAlarm = :idAlarm"),
    @NamedQuery(name = "Alarm.findByDatetimeMoment", query = "SELECT a FROM Alarm a WHERE a.datetimeMoment = :datetimeMoment"),
    @NamedQuery(name = "Alarm.findByIntervalSeconds", query = "SELECT a FROM Alarm a WHERE a.intervalSeconds = :intervalSeconds")})
public class Alarm implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "datetimeMoment")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeMoment;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAlarm")
    private Integer idAlarm;
    @Column(name = "intervalSeconds")
    private Integer intervalSeconds;
    @ManyToMany(mappedBy = "alarmList")
    private List<Activity> activityList;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private User idUser;

    public Alarm() {
    }

    public Alarm(Integer idAlarm) {
        this.idAlarm = idAlarm;
    }

    public Alarm(Integer idAlarm, Date datetimeMoment) {
        this.idAlarm = idAlarm;
        this.datetimeMoment = datetimeMoment;
    }

    public Integer getIdAlarm() {
        return idAlarm;
    }

    public void setIdAlarm(Integer idAlarm) {
        this.idAlarm = idAlarm;
    }


    public Integer getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(Integer intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
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
        hash += (idAlarm != null ? idAlarm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.idAlarm == null && other.idAlarm != null) || (this.idAlarm != null && !this.idAlarm.equals(other.idAlarm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Alarm[ idAlarm=" + idAlarm + " ]";
    }

    public Date getDatetimeMoment() {
        return datetimeMoment;
    }

    public void setDatetimeMoment(Date datetimeMoment) {
        this.datetimeMoment = datetimeMoment;
    }
    
}
