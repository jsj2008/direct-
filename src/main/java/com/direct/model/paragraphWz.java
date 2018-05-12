package com.direct.model;

public class paragraphWz {
    private Integer id;

    private String currentpar;

    private String par;

    private String type;

    private Integer price;

    private Integer trainer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrentpar() {
        return currentpar;
    }

    public void setCurrentpar(String currentpar) {
        this.currentpar = currentpar == null ? null : currentpar.trim();
    }

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par == null ? null : par.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTrainer() {
        return trainer;
    }

    public void setTrainer(Integer trainer) {
        this.trainer = trainer;
    }
}