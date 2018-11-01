package com.ritwik.rai.locatefactory;

import java.io.Serializable;

public class ProductionStage implements Serializable {

    private String stageId = "";

    private String stageNo = "";

    private String stageName = "";


    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageNo() {
        return stageNo;
    }

    public void setStageNo(String stageNo) {
        this.stageNo = stageNo;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }
}
