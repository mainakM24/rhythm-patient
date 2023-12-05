package com.example.rhythm;

import java.io.Serializable;

public class UserDataModel implements Serializable {

           String id;
           String patient_id;
           String p_name;
           String p_dob;
           String p_sex;
           String p_street_name;
           String p_mobile;
           String p_notes;
           String p_start_date;
           String p_status;
           String p_email;
           String p_house_no;
           String p_city;
           String p_state;
           String p_country;
           String p_pin_code;

    public UserDataModel(String id, String patient_id, String p_name, String p_dob, String p_sex, String p_street_name, String p_mobile, String p_notes, String p_start_date, String p_status, String p_email, String p_house_no, String p_city, String p_state, String p_country, String p_pin_code) {
        this.id = id;
        this.patient_id = patient_id;
        this.p_name = p_name;
        this.p_dob = p_dob;
        this.p_sex = p_sex;
        this.p_street_name = p_street_name;
        this.p_mobile = p_mobile;
        this.p_notes = p_notes;
        this.p_start_date = p_start_date;
        this.p_status = p_status;
        this.p_email = p_email;
        this.p_house_no = p_house_no;
        this.p_city = p_city;
        this.p_state = p_state;
        this.p_country = p_country;
        this.p_pin_code = p_pin_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_dob() {
        return p_dob;
    }

    public void setP_dob(String p_dob) {
        this.p_dob = p_dob;
    }

    public String getP_sex() {
        return p_sex;
    }

    public void setP_sex(String p_sex) {
        this.p_sex = p_sex;
    }

    public String getP_street_name() {
        return p_street_name;
    }

    public void setP_street_name(String p_street_name) {
        this.p_street_name = p_street_name;
    }

    public String getP_mobile() {
        return p_mobile;
    }

    public void setP_mobile(String p_mobile) {
        this.p_mobile = p_mobile;
    }

    public String getP_notes() {
        return p_notes;
    }

    public void setP_notes(String p_notes) {
        this.p_notes = p_notes;
    }

    public String getP_start_date() {
        return p_start_date;
    }

    public void setP_start_date(String p_start_date) {
        this.p_start_date = p_start_date;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_house_no() {
        return p_house_no;
    }

    public void setP_house_no(String p_house_no) {
        this.p_house_no = p_house_no;
    }

    public String getP_city() {
        return p_city;
    }

    public void setP_city(String p_city) {
        this.p_city = p_city;
    }

    public String getP_state() {
        return p_state;
    }

    public void setP_state(String p_state) {
        this.p_state = p_state;
    }

    public String getP_country() {
        return p_country;
    }

    public void setP_country(String p_country) {
        this.p_country = p_country;
    }

    public String getP_pin_code() {
        return p_pin_code;
    }

    public void setP_pin_code(String p_pin_code) {
        this.p_pin_code = p_pin_code;
    }
}
