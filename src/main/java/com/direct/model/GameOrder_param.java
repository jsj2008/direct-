package com.direct.model;

import java.util.Date;

public class GameOrder_param {
    private Integer id;

    private String openid;

    private String orderid;

    private String paytype;

    private Integer totalFee;

    private String state;

    private Date createdtime;

    private Date updatatime;

    private String phone;

    private Integer trainertime;

    private String contype1;

    private String ordertype;

    private String boostertype;

    private String accompanytype;

    private Integer currentpar;

    private Integer targetpar;

    private Integer inscriptiongrade;

    private Integer positioningmatch1;

    private Integer positioningmatch2;

    private Integer currentgrade;

    private Integer targetgrade;

    private String displacementtype;

    private String issupplement;

    private String gametype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public Date getUpdatatime() {
        return updatatime;
    }

    public void setUpdatatime(Date updatatime) {
        this.updatatime = updatatime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTrainertime() {
        return trainertime;
    }

        public void setTrainertime(Integer trainertime) {
        this.trainertime = trainertime;
    }

    public String getContype1() {
        return contype1;
    }

    public void setContype1(String contype1) {
        this.contype1 = contype1 == null ? null : contype1.trim();
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype == null ? null : ordertype.trim();
    }

    public String getBoostertype() {
        return boostertype;
    }

    public void setBoostertype(String boostertype) {
        this.boostertype = boostertype == null ? null : boostertype.trim();
    }

    public String getAccompanytype() {
        return accompanytype;
    }

    public void setAccompanytype(String accompanytype) {
        this.accompanytype = accompanytype == null ? null : accompanytype.trim();
    }

    public Integer getCurrentpar() {
        return currentpar;
    }

    public void setCurrentpar(Integer currentpar) {
        this.currentpar = currentpar;
    }

    public Integer getTargetpar() {
        return targetpar;
    }

    public void setTargetpar(Integer targetpar) {
        this.targetpar = targetpar;
    }

    public Integer getInscriptiongrade() {
        return inscriptiongrade;
    }

    public void setInscriptiongrade(Integer inscriptiongrade) {
        this.inscriptiongrade = inscriptiongrade;
    }

    public Integer getPositioningmatch1() {
        return positioningmatch1;
    }

    public void setPositioningmatch1(Integer positioningmatch1) {
        this.positioningmatch1 = positioningmatch1;
    }

    public Integer getPositioningmatch2() {
        return positioningmatch2;
    }

    public void setPositioningmatch2(Integer positioningmatch2) {
        this.positioningmatch2 = positioningmatch2;
    }

    public Integer getCurrentgrade() {
        return currentgrade;
    }

    public void setCurrentgrade(Integer currentgrade) {
        this.currentgrade = currentgrade;
    }

    public Integer getTargetgrade() {
        return targetgrade;
    }

    public void setTargetgrade(Integer targetgrade) {
        this.targetgrade = targetgrade;
    }

    public String getDisplacementtype() {
        return displacementtype;
    }

    public void setDisplacementtype(String displacementtype) {
        this.displacementtype = displacementtype == null ? null : displacementtype.trim();
    }

    public String getIssupplement() {
        return issupplement;
    }

    public void setIssupplement(String issupplement) {
        this.issupplement = issupplement == null ? null : issupplement.trim();
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }
}