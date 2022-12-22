/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.rest;

import javax.xml.bind.annotation.*;
import java.sql.*;

/**
 *
 * @author gui
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ad")
public class  Ad implements java.io.Serializable {
    @XmlElement(required = true)
    protected String advertiser;
    @XmlElement(required = true)
    protected String typeAd;
    @XmlElement(required = true)
    protected String stateAd;
    @XmlElement(required = true)
    protected int price;
    @XmlElement(required = true)
    protected String gender;
    @XmlElement(required = true)
    protected String localAd;
    @XmlElement(required = true)
    protected String typology;
    @XmlElement(required = false)
    protected Date date;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = false)
    protected int aid;

    public Ad() {
        super();
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public void setType(String type) {
        this.typeAd = type;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLocal(String local) {
        this.localAd = local;
    }


    public void setTypology(String typology) {
        this.typology = typology;
    }

    public void setState(String state) {
        this.stateAd = state;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) { this.description = description; }

    // get's

    public String getAdvertiser() {
        return advertiser;
    }

    public String getType() {
        return typeAd;
    }

    public String getState() {
        return stateAd;
    }

    public int getPrice() {
        return price;
    }

    public String getGender() {
        return gender;
    }

    public String getLocal() {
        return localAd;
    }

    public String getTypology() {
        return typology;
    }

    public Date getDate() {
        return date;
    }

    public int getAid() { return aid; }

    public String getDescription() { return description; }
}
