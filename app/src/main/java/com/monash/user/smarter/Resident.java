package com.monash.user.smarter;

import java.util.Date;

public class Resident {
    Integer resid;
    String firstname;
    String surname;
    Date dob;
    String address;
    String postcode;
    String email;
    String mobilenumber;
    Integer numberofresidents;
    String energyprovider;

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public Integer getNumberofresidents() {
        return numberofresidents;
    }

    public Resident(Integer resid, String firstname, String surname, Date dob, String address, String postcode, String email, String mobilenumber, Integer numberofresidents, String energyprovider) {
        this.resid = resid;
        this.firstname = firstname;
        this.surname = surname;
        this.dob = dob;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.mobilenumber = mobilenumber;
        this.numberofresidents = numberofresidents;
        this.energyprovider = energyprovider;
    }

    public void setNumberofresidents(Integer numberofresidents) {

        this.numberofresidents = numberofresidents;
    }

    public String getEnergyprovider() {
        return energyprovider;
    }

    public void setEnergyprovider(String energyprovider) {
        this.energyprovider = energyprovider;
    }



    public Integer getResid() {
        return resid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }





    public void setResid(Integer resid) {
        this.resid = resid;
    }
}
