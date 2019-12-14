package com.monash.user.smarter;

import java.util.Date;

public class Credentials {
    String username;
    String passwordhash;
    Date registrationdate;
    Resident resid;

    public Resident getResident() {
        return resid;
    }

    public void setResident(Resident resident) {
        this.resid = resident;
    }

    public Credentials(String username, String passwordhash, Date registrationdate, Resident resident) {
        this.username = username;
        this.passwordhash = passwordhash;
        this.registrationdate = registrationdate;
        this.resid = resident;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getPasswordhash() {

        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public Date getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(Date registrationdate) {
        this.registrationdate = registrationdate;
    }





}
