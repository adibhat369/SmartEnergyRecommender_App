/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartERWebServices;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Adithya bhat
 */
@Entity
@Table(name = "ELECTRICITYUSAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Electricityusage.findAll", query = "SELECT e FROM Electricityusage e"),
    @NamedQuery(name = "Electricityusage.findByUsageid", query = "SELECT e FROM Electricityusage e WHERE e.usageid = :usageid"),
    @NamedQuery(name = "Electricityusage.findByUsagedate", query = "SELECT e FROM Electricityusage e WHERE e.usagedate = :usagedate"),
    @NamedQuery(name = "Electricityusage.findByUsagehour", query = "SELECT e FROM Electricityusage e WHERE e.usagehour = :usagehour"),
    @NamedQuery(name = "Electricityusage.findByFridgeusage", query = "SELECT e FROM Electricityusage e WHERE e.fridgeusage = :fridgeusage"),
    @NamedQuery(name = "Electricityusage.findByAcusage", query = "SELECT e FROM Electricityusage e WHERE e.acusage = :acusage"),
    @NamedQuery(name = "Electricityusage.findByWashingmachineusage", query = "SELECT e FROM Electricityusage e WHERE e.washingmachineusage = :washingmachineusage"),
    @NamedQuery(name = "Electricityusage.findByTemperature", query = "SELECT e FROM Electricityusage e WHERE e.temperature = :temperature"),
    @NamedQuery(name = "Electricityusage.findByResid", query = "SELECT e FROM Electricityusage e WHERE e.resid.resid = :resid"),
    @NamedQuery(name = "Electricityusage.findByfirstnameandACusageanddate", query = "Select e from Electricityusage e where e.resid.firstname = :firstname and e.acusage = :acusage and e.usagedate = :usagedate")})
public class Electricityusage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USAGEID")
    private Integer usageid;
    @Column(name = "USAGEDATE")
    @Temporal(TemporalType.DATE)
    private Date usagedate;
    @Column(name = "USAGEHOUR")
    private Integer usagehour;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FRIDGEUSAGE")
    private BigDecimal fridgeusage;
    @Column(name = "ACUSAGE")
    private BigDecimal acusage;
    @Column(name = "WASHINGMACHINEUSAGE")
    private BigDecimal washingmachineusage;
    @Column(name = "TEMPERATURE")
    private Integer temperature;
    @JoinColumn(name = "RESID", referencedColumnName = "RESID")
    @ManyToOne
    private Resident resid;

    public Electricityusage() {
    }

    public Electricityusage(Integer usageid) {
        this.usageid = usageid;
    }

    public Integer getUsageid() {
        return usageid;
    }

    public void setUsageid(Integer usageid) {
        this.usageid = usageid;
    }

    public Date getUsagedate() {
        return usagedate;
    }

    public void setUsagedate(Date usagedate) {
        this.usagedate = usagedate;
    }

    public Integer getUsagehour() {
        return usagehour;
    }

    public void setUsagehour(Integer usagehour) {
        this.usagehour = usagehour;
    }

    public BigDecimal getFridgeusage() {
        return fridgeusage;
    }

    public void setFridgeusage(BigDecimal fridgeusage) {
        this.fridgeusage = fridgeusage;
    }

    public BigDecimal getAcusage() {
        return acusage;
    }

    public void setAcusage(BigDecimal acusage) {
        this.acusage = acusage;
    }

    public BigDecimal getWashingmachineusage() {
        return washingmachineusage;
    }

    public void setWashingmachineusage(BigDecimal washingmachineusage) {
        this.washingmachineusage = washingmachineusage;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Resident getResid() {
        return resid;
    }

    public void setResid(Resident resid) {
        this.resid = resid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usageid != null ? usageid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Electricityusage)) {
            return false;
        }
        Electricityusage other = (Electricityusage) object;
        if ((this.usageid == null && other.usageid != null) || (this.usageid != null && !this.usageid.equals(other.usageid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SmartERWebServices.Electricityusage[ usageid=" + usageid + " ]";
    }
    
}
