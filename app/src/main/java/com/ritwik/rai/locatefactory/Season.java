package com.ritwik.rai.locatefactory;

import java.io.Serializable;

public class Season implements Serializable {

    private String seasonID = "";

    private String seasonPmsID = "";

    private String seasonName = "";


    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getSeasonPmsID() {
        return seasonPmsID;
    }

    public void setSeasonPmsID(String seasonPmsID) {
        this.seasonPmsID = seasonPmsID;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
