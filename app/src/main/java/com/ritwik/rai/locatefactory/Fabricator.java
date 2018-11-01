package com.ritwik.rai.locatefactory;

import java.io.Serializable;

public class Fabricator implements Serializable {

    private String fabricatorID = "";

    private String groupName = "";

    private String supervisorName = "";

    private String unitId = "";

    public String getFabricatorID() {
        return fabricatorID;
    }

    public void setFabricatorID(String fabricatorID) {
        this.fabricatorID = fabricatorID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
