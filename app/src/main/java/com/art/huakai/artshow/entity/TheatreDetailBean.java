package com.art.huakai.artshow.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 2017/10/28.
 */
public class TheatreDetailBean {


    /**
     * id : 8a999cce5f4ed221015f4f0e7b640003
     * logo : http://139.224.47.213/image/8a999cce5f4ed220015f4f0e7b200004.png
     * name : 鸣人剧场
     * roomName : 鸣人厅
     * seating : 800
     * regionId : 130300
     * address : 望京地区
     * coordinate : null
     * linkman : 鸣人
     * linkTel : 17610005667
     * expense : 8000
     * description : 舞动青春不知道的
     * stageHeight : 0
     * stageWidth : 0
     * stageDepth : 0
     * curtainHeight : 0
     * curtainWidth : 0
     * dressingRoomNum : 0
     * rehearsalRoomNum : 0
     * propRoomNum : 0
     * costumeRoomNum : 18
     * stageLights : 0
     * stereoEquipment : 1
     * broadcastSystem : 0
     * steeve : 0
     * musicStage : 0
     * chorusPlatform : 1
     * orchestraPit : 0
     * acousticShroud : 0
     * bandPlatform : 0
     * curtainSystem : 0
     * specialEquipment : 0
     * projector : 0
     * priceDiagram : http://139.224.47.213/image/8a999cce5f621167015f62ebdf6b000e.png
     * detailedIntroduce : 舞动青春里走过你身边有没有这样做可以不看书学习成绩优秀
     * userId : 8a999cce5f497b4e015f49860e700005
     * status : 1
     * createTime : 1509194016000
     * updateTime : 1509197501100
     * regionName : 秦皇岛市
     * pictures : [{"masterUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f92036000f@thumb.png","largeUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f92036000f@thumb.png","middleUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f92036000f@thumb.png","smallUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f92036000f@thumb.png","height":911,"width":1366,"theaterId":null,"createTime":null},{"masterUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f924370010@thumb.png","largeUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f924370010@thumb.png","middleUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f924370010@thumb.png","smallUrl":"http://139.224.47.213/image/8a999cce5f621167015f62f924370010@thumb.png","height":911,"width":1366,"theaterId":null,"createTime":null}]
     * disabledDates : [{"theaterId":null,"date":1509292800000}]
     * disabledMonths : null
     * viewTimes : 60
     */

    private String id;
    private String logo;
    private String name;
    private String roomName;
    private int seating;
    private int regionId;
    private String address;
    private Object coordinate;
    private String linkman;
    private String linkTel;
    private int expense;
    private String description;
    private int stageHeight;
    private int stageWidth;
    private int stageDepth;
    private int curtainHeight;
    private int curtainWidth;
    private int dressingRoomNum;
    private int rehearsalRoomNum;
    private int propRoomNum;
    private int costumeRoomNum;
    private int stageLights;
    private int stereoEquipment;
    private int broadcastSystem;
    private int steeve;
    private int musicStage;
    private int chorusPlatform;
    private int orchestraPit;
    private int acousticShroud;
    private int bandPlatform;
    private int curtainSystem;
    private int specialEquipment;
    private int projector;
    private String priceDiagram;
    private String detailedIntroduce;
    private String userId;
    private int status;
    private long createTime;
    private long updateTime;
    private String regionName;
    private Object disabledMonths;
    private int viewTimes;
    private ArrayList<PicturesBean> pictures;
    private List<DisabledDatesBean> disabledDates;

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

    public int getSeating() {
        return seating;
    }

    public void setSeating(int seating) {
        this.seating = seating;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Object coordinate) {
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

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStageHeight() {
        return stageHeight;
    }

    public void setStageHeight(int stageHeight) {
        this.stageHeight = stageHeight;
    }

    public int getStageWidth() {
        return stageWidth;
    }

    public void setStageWidth(int stageWidth) {
        this.stageWidth = stageWidth;
    }

    public int getStageDepth() {
        return stageDepth;
    }

    public void setStageDepth(int stageDepth) {
        this.stageDepth = stageDepth;
    }

    public int getCurtainHeight() {
        return curtainHeight;
    }

    public void setCurtainHeight(int curtainHeight) {
        this.curtainHeight = curtainHeight;
    }

    public int getCurtainWidth() {
        return curtainWidth;
    }

    public void setCurtainWidth(int curtainWidth) {
        this.curtainWidth = curtainWidth;
    }

    public int getDressingRoomNum() {
        return dressingRoomNum;
    }

    public void setDressingRoomNum(int dressingRoomNum) {
        this.dressingRoomNum = dressingRoomNum;
    }

    public int getRehearsalRoomNum() {
        return rehearsalRoomNum;
    }

    public void setRehearsalRoomNum(int rehearsalRoomNum) {
        this.rehearsalRoomNum = rehearsalRoomNum;
    }

    public int getPropRoomNum() {
        return propRoomNum;
    }

    public void setPropRoomNum(int propRoomNum) {
        this.propRoomNum = propRoomNum;
    }

    public int getCostumeRoomNum() {
        return costumeRoomNum;
    }

    public void setCostumeRoomNum(int costumeRoomNum) {
        this.costumeRoomNum = costumeRoomNum;
    }

    public int getStageLights() {
        return stageLights;
    }

    public void setStageLights(int stageLights) {
        this.stageLights = stageLights;
    }

    public int getStereoEquipment() {
        return stereoEquipment;
    }

    public void setStereoEquipment(int stereoEquipment) {
        this.stereoEquipment = stereoEquipment;
    }

    public int getBroadcastSystem() {
        return broadcastSystem;
    }

    public void setBroadcastSystem(int broadcastSystem) {
        this.broadcastSystem = broadcastSystem;
    }

    public int getSteeve() {
        return steeve;
    }

    public void setSteeve(int steeve) {
        this.steeve = steeve;
    }

    public int getMusicStage() {
        return musicStage;
    }

    public void setMusicStage(int musicStage) {
        this.musicStage = musicStage;
    }

    public int getChorusPlatform() {
        return chorusPlatform;
    }

    public void setChorusPlatform(int chorusPlatform) {
        this.chorusPlatform = chorusPlatform;
    }

    public int getOrchestraPit() {
        return orchestraPit;
    }

    public void setOrchestraPit(int orchestraPit) {
        this.orchestraPit = orchestraPit;
    }

    public int getAcousticShroud() {
        return acousticShroud;
    }

    public void setAcousticShroud(int acousticShroud) {
        this.acousticShroud = acousticShroud;
    }

    public int getBandPlatform() {
        return bandPlatform;
    }

    public void setBandPlatform(int bandPlatform) {
        this.bandPlatform = bandPlatform;
    }

    public int getCurtainSystem() {
        return curtainSystem;
    }

    public void setCurtainSystem(int curtainSystem) {
        this.curtainSystem = curtainSystem;
    }

    public int getSpecialEquipment() {
        return specialEquipment;
    }

    public void setSpecialEquipment(int specialEquipment) {
        this.specialEquipment = specialEquipment;
    }

    public int getProjector() {
        return projector;
    }

    public void setProjector(int projector) {
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Object getDisabledMonths() {
        return disabledMonths;
    }

    public void setDisabledMonths(Object disabledMonths) {
        this.disabledMonths = disabledMonths;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public ArrayList<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<PicturesBean> pictures) {
        this.pictures = pictures;
    }

    public List<DisabledDatesBean> getDisabledDates() {
        return disabledDates;
    }

    public void setDisabledDates(List<DisabledDatesBean> disabledDates) {
        this.disabledDates = disabledDates;
    }



    public static class DisabledDatesBean {
        /**
         * theaterId : null
         * date : 1509292800000
         */

        private Object theaterId;
        private long date;

        public Object getTheaterId() {
            return theaterId;
        }

        public void setTheaterId(Object theaterId) {
            this.theaterId = theaterId;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }
}
