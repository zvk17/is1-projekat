/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pametnakucaservis.resources;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author user2
 */
@XmlRootElement(name = "moment")
public class XmlMoment {
    
    private Date dateTime;
    private Integer id;
    public XmlMoment() {
    }
    
    public XmlMoment(Date date, int id) {
        this.dateTime = date;
        this.id = id;
    }
    @XmlElement (name = "date-time")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    @XmlElement
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
