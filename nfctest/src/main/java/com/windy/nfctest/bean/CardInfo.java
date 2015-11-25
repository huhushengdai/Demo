package com.windy.nfctest.bean;

/**
 * author: wang
 * time: 2015/9/28
 * description:
 *     卡片信息
 */
public class CardInfo {
    private long cardNum;//卡号
    //---4块
    private String cardType;//卡类型
    private String carType;//车类型
    private String startEffectTime;//有效期开始时间
    private String endEffectTime;//有效期结束时间
    //---5块或6块信息
    private String enterTime;//入场时间
    private byte enterState;//0为入场，1为出场
    private String centerChargeTime;//中央收费--临时卡已经交费时间，车辆入场时清零
    private String centerPayment;//中央收费--临时卡已交金额（累加），车辆入场时清零
    private byte chargeFlag;//0是未交费，1是已经交费
    private byte discountFlag;//0是无折扣，1是有折扣

    public CardInfo() {
    }

    public long getCardNum() {
        return cardNum;
    }

    public void setCardNum(long cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getStartEffectTime() {
        return startEffectTime;
    }

    public void setStartEffectTime(String startEffectTime) {
        this.startEffectTime = startEffectTime;
    }

    public String getEndEffectTime() {
        return endEffectTime;
    }

    public void setEndEffectTime(String endEffectTime) {
        this.endEffectTime = endEffectTime;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public byte getEnterState() {
        return enterState;
    }

    public void setEnterState(byte enterState) {
        this.enterState = enterState;
    }

    public String getCenterChargeTime() {
        return centerChargeTime;
    }

    public void setCenterChargeTime(String centerChargeTime) {
        this.centerChargeTime = centerChargeTime;
    }

    public String getCenterPayment() {
        return centerPayment;
    }

    public void setCenterPayment(String centerPayment) {
        this.centerPayment = centerPayment;
    }

    public byte getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(byte chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public byte getDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(byte discountFlag) {
        this.discountFlag = discountFlag;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardNum=" + cardNum +
                ", cardType='" + cardType + '\'' +
                ", carType='" + carType + '\'' +
                ", startEffectTime='" + startEffectTime + '\'' +
                ", endEffectTime='" + endEffectTime + '\'' +
                ", enterTime='" + enterTime + '\'' +
                ", enterState=" + enterState +
                ", centerChargeTime='" + centerChargeTime + '\'' +
                ", centerPayment='" + centerPayment + '\'' +
                ", chargeFlag=" + chargeFlag +
                ", discountFlag=" + discountFlag +
                '}';
    }
}
