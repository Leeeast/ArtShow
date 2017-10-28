package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lidonglaing on 2017/10/25.
 */

public class TheatreDetailInfo {

    private TheatreDetailInfo() {
    }

    private static TheatreDetailInfo theatreDetailInfo;

    public synchronized static TheatreDetailInfo getInstance() {
        if (theatreDetailInfo == null) {
            theatreDetailInfo = new TheatreDetailInfo();
        }
        return theatreDetailInfo;
    }

    private String id;
    private String logo;
    private String name;
    private String roomName;
    private String seating;
    private String regionId;
    private String address;
    private String coordinate;
    private String linkman;
    private String linkTel;
    private String expense;
    private String description;
    private String stageHeight;
    private String stageWidth;
    private String stageDepth;
    private String curtainHeight;
    private String curtainWidth;
    private String dressingRoomNum;
    private String rehearsalRoomNum;
    private String propRoomNum;
    private String costumeRoomNum;
    private String stageLights;
    private String stereoEquipment;
    private String broadcastSystem;
    private String steeve;
    private String musicStage;
    private String chorusPlatform;
    private String orchestraPit;
    private String acousticShroud;
    private String bandPlatform;
    private String curtainSystem;
    private String specialEquipment;
    private String projector;
    private String priceDiagram;
    private String detailedIntroduce;
    private String userId;
    private int status;
    private long createTime;
    private String updateTime;
    private String regionName;
    private List<PicturesBean> pictures;
    private List<String> disabledDates;
    private String disabledMonths;
    private int viewTimes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSeating() {
        return seating;
    }

    public void setSeating(String seating) {
        this.seating = seating;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStageHeight() {
        return stageHeight;
    }

    public void setStageHeight(String stageHeight) {
        this.stageHeight = stageHeight;
    }

    public String getStageWidth() {
        return stageWidth;
    }

    public void setStageWidth(String stageWidth) {
        this.stageWidth = stageWidth;
    }

    public String getStageDepth() {
        return stageDepth;
    }

    public void setStageDepth(String stageDepth) {
        this.stageDepth = stageDepth;
    }

    public String getCurtainHeight() {
        return curtainHeight;
    }

    public void setCurtainHeight(String curtainHeight) {
        this.curtainHeight = curtainHeight;
    }

    public String getCurtainWidth() {
        return curtainWidth;
    }

    public void setCurtainWidth(String curtainWidth) {
        this.curtainWidth = curtainWidth;
    }

    public String getDressingRoomNum() {
        return dressingRoomNum;
    }

    public void setDressingRoomNum(String dressingRoomNum) {
        this.dressingRoomNum = dressingRoomNum;
    }

    public String getRehearsalRoomNum() {
        return rehearsalRoomNum;
    }

    public void setRehearsalRoomNum(String rehearsalRoomNum) {
        this.rehearsalRoomNum = rehearsalRoomNum;
    }

    public String getPropRoomNum() {
        return propRoomNum;
    }

    public void setPropRoomNum(String propRoomNum) {
        this.propRoomNum = propRoomNum;
    }

    public String getCostumeRoomNum() {
        return costumeRoomNum;
    }

    public void setCostumeRoomNum(String costumeRoomNum) {
        this.costumeRoomNum = costumeRoomNum;
    }

    public String getStageLights() {
        return stageLights;
    }

    public void setStageLights(String stageLights) {
        this.stageLights = stageLights;
    }

    public String getStereoEquipment() {
        return stereoEquipment;
    }

    public void setStereoEquipment(String stereoEquipment) {
        this.stereoEquipment = stereoEquipment;
    }

    public String getBroadcastSystem() {
        return broadcastSystem;
    }

    public void setBroadcastSystem(String broadcastSystem) {
        this.broadcastSystem = broadcastSystem;
    }

    public String getSteeve() {
        return steeve;
    }

    public void setSteeve(String steeve) {
        this.steeve = steeve;
    }

    public String getMusicStage() {
        return musicStage;
    }

    public void setMusicStage(String musicStage) {
        this.musicStage = musicStage;
    }

    public String getChorusPlatform() {
        return chorusPlatform;
    }

    public void setChorusPlatform(String chorusPlatform) {
        this.chorusPlatform = chorusPlatform;
    }

    public String getOrchestraPit() {
        return orchestraPit;
    }

    public void setOrchestraPit(String orchestraPit) {
        this.orchestraPit = orchestraPit;
    }

    public String getAcousticShroud() {
        return acousticShroud;
    }

    public void setAcousticShroud(String acousticShroud) {
        this.acousticShroud = acousticShroud;
    }

    public String getBandPlatform() {
        return bandPlatform;
    }

    public void setBandPlatform(String bandPlatform) {
        this.bandPlatform = bandPlatform;
    }

    public String getCurtainSystem() {
        return curtainSystem;
    }

    public void setCurtainSystem(String curtainSystem) {
        this.curtainSystem = curtainSystem;
    }

    public String getSpecialEquipment() {
        return specialEquipment;
    }

    public void setSpecialEquipment(String specialEquipment) {
        this.specialEquipment = specialEquipment;
    }

    public String getProjector() {
        return projector;
    }

    public void setProjector(String projector) {
        this.projector = projector;
    }

    public String getPriceDiagram() {
        return priceDiagram;
    }

    public void setPriceDiagram(String priceDiagram) {
        this.priceDiagram = priceDiagram;
    }

    public String getDetailedIntroduce() {
        return detailedIntroduce;
    }

    public void setDetailedIntroduce(String detailedIntroduce) {
        this.detailedIntroduce = detailedIntroduce;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesBean> pictures) {
        this.pictures = pictures;
    }

    public List<String> getDisabledDates() {
        return disabledDates;
    }

    public void setDisabledDates(List<String> disabledDates) {
        this.disabledDates = disabledDates;
    }

    public String getDisabledMonths() {
        return disabledMonths;
    }

    public void setDisabledMonths(String disabledMonths) {
        this.disabledMonths = disabledMonths;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }
}
