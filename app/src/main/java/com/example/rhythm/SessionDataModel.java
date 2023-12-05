package com.example.rhythm;

import java.io.Serializable;

public class SessionDataModel implements Serializable {
    String session_id;
    String total_analyzed_beat;
    String start_time;
    String duration;
    String normal_beat_count;
    String median_bpm_count;
    Integer max_bpm_count;
    Integer min_bpm_count;
    Integer ap_beat_count;
    Integer svf_beat_count;
    Integer int_normal_beat_count;


    public SessionDataModel(String session_id, String total_analyzed_beat, String start_time, String duration, String normal_beat_count, String median_bpm_count, Integer ap_beat_count, Integer svf_beat_count) {
        this.session_id = session_id;
        this.total_analyzed_beat = total_analyzed_beat;
        this.start_time = start_time;
        this.duration = duration;
        this.normal_beat_count = normal_beat_count;
        this.median_bpm_count = median_bpm_count;
        this.ap_beat_count = ap_beat_count;
        this.svf_beat_count = svf_beat_count;
    }

    public SessionDataModel() {

    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getTotal_analyzed_beat() {
        return total_analyzed_beat;
    }

    public void setTotal_analyzed_beat(String total_analyzed_beat) {
        this.total_analyzed_beat = total_analyzed_beat;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNormal_beat_count() {
        return normal_beat_count;
    }

    public void setNormal_beat_count(String normal_beat_count) {
        this.normal_beat_count = normal_beat_count;
    }

    public String getMedian_bpm_count() {
        return median_bpm_count;
    }

    public void setMedian_bpm_count(String median_bpm_count) {
        this.median_bpm_count = median_bpm_count;
    }

    public Integer getAp_beat_count() {
        return ap_beat_count;
    }

    public void setAp_beat_count(Integer ap_beat_count) {
        this.ap_beat_count = ap_beat_count;
    }

    public Integer getSvf_beat_count() {
        return svf_beat_count;
    }

    public void setSvf_beat_count(Integer svf_beat_count) {
        this.svf_beat_count = svf_beat_count;
    }

    public Integer getInt_normal_beat_count() {
        return int_normal_beat_count;
    }

    public void setInt_normal_beat_count(Integer int_normal_beat_count) {
        this.int_normal_beat_count = int_normal_beat_count;
    }

    public Integer getMax_bpm_count() {
        return max_bpm_count;
    }

    public void setMax_bpm_count(Integer max_bpm_count) {
        this.max_bpm_count = max_bpm_count;
    }

    public Integer getMin_bpm_count() {
        return min_bpm_count;
    }

    public void setMin_bpm_count(Integer min_bpm_count) {
        this.min_bpm_count = min_bpm_count;
    }
}

