/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Adithya bhat
 */
@XmlRootElement
public class Resident_electricity_usage {
    Integer resid;
    String address;
    String postcode;
    Float total_hourly_power_usage;

    public Integer getResid() {
        return resid;
    }

    public void setResid(Integer resid) {
        this.resid = resid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Resident_electricity_usage() {
    }

    public Resident_electricity_usage(Integer resid, String address, String postcode, Float total_hourly_power_usage) {
        this.resid = resid;
        this.address = address;
        this.postcode = postcode;
        this.total_hourly_power_usage = total_hourly_power_usage;
    }

    public Float getTotal_hourly_power_usage() {
        return total_hourly_power_usage;
    }

    public void setTotal_hourly_power_usage(Float total_hourly_power_usage) {
        this.total_hourly_power_usage = total_hourly_power_usage;
    }
       
}
