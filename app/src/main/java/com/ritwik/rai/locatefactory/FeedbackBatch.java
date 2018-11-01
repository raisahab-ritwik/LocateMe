package com.ritwik.rai.locatefactory;

import java.io.Serializable;

public class FeedbackBatch implements Serializable {

    private String batchID = "";

    private String batchNumber = "";

    private String batchDate = "";

    private String batchEtDate = "";

    private String sku = "";

    private String skuId = "";

    private String sumQty = "";

    private String fabricatorID="";

    private Fabricator fabricator;

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    public String getBatchEtDate() {
        return batchEtDate;
    }

    public void setBatchEtDate(String batchEtDate) {
        this.batchEtDate = batchEtDate;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSumQty() {
        return sumQty;
    }

    public void setSumQty(String sumQty) {
        this.sumQty = sumQty;
    }

    public String getFabricatorID() {
        return fabricatorID;
    }

    public void setFabricatorID(String fabricatorID) {
        this.fabricatorID = fabricatorID;
    }

    public Fabricator getFabricator() {
        return fabricator;
    }

    public void setFabricator(Fabricator fabricator) {
        this.fabricator = fabricator;
    }
}
