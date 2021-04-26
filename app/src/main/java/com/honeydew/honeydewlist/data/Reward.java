package com.honeydew.honeydewlist.data;

import java.util.List;

public class Reward extends Item {
    private Long points;
    private Long quantity; // TODO: Add quantity restriction, for now set quantity to 1
    private Boolean redeemed;
    private String redeemerUUID;
    private String redeemer;

    public Reward() {
        super();
    }

    public Reward(String name, String description, String owner, String uuid, Long points, Boolean redeemed, String redeemerUUID, String redeemer, String itemID) {
        super(name, description, owner, uuid, itemID);
        this.redeemed = redeemed;
        this.points = points;
        this.redeemer = redeemer;
        this.redeemerUUID = redeemerUUID;
    }

    public Long getPoints() {
        return points;
    }

    public void setCost(Long points) {
        this.points = points;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Boolean getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    public String getRedeemerUUID() {
        return redeemerUUID;
    }

    public void setRedeemerUUID(String redeemerUUID) {
        this.redeemerUUID = redeemerUUID;
    }

    public String getRedeemer() {
        return redeemer;
    }

    public void setRedeemer(String redeemer) {
        this.redeemer = redeemer;
    }
}
