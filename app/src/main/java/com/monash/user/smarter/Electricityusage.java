package com.monash.user.smarter;

import java.math.BigDecimal;
import java.util.Date;

public class Electricityusage {

    Integer usageid;
    Date usagedate;
    Integer usagehour;
    Float fridgeusage;
    Float acusage;
    Float washingmachineusage;
    Integer temperature;
    Resident resid;

    public Electricityusage(Integer usageid, Date usagedate, Integer usagehour, Float fridgeusage, Float acusage, Float washingmachineusage, Integer temperature, Resident resid) {
        this.usageid = usageid;
        this.usagedate = usagedate;
        this.usagehour = usagehour;
        this.fridgeusage = fridgeusage;
        this.acusage = acusage;
        this.washingmachineusage = washingmachineusage;
        this.temperature = temperature;
        this.resid = resid;
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

    public Float getFridgeusage() {
        return fridgeusage;
    }

    public void setFridgeusage(Float fridgeusage) {
        this.fridgeusage = fridgeusage;
    }

    public Float getAcusage() {
        return acusage;
    }

    public void setAcusage(Float acusage) {
        this.acusage = acusage;
    }

    public Float getWashingmachineusage() {
        return washingmachineusage;
    }

    public void setWashingmachineusage(Float washingmachineusage) {
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
}
