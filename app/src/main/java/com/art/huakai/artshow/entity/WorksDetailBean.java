package com.art.huakai.artshow.entity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 2017/10/28.
 */
public class WorksDetailBean implements Serializable {
    /**
     * id : 8a999cce5e4ccfa8015e5691aeeb009b
     * logo : https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg
     * title : 我是我爷爷
     * classifyId : 4
     * linkman : 青麦演艺经纪（北京）有限公司
     * linkTel : 13581533281
     * peopleNum : 30
     * expense : 100000
     * regionId : 36
     * seatingRequir : 500
     * premiereTime : 1508256000000
     * rounds : 0
     * showLast : 90
     * description :     关注社会问题，取材自当代家庭生活聚焦社会父子关系，通过多重魔幻相互建构的戏剧手段来解构超高速的现实科技生活与失落的亲情，从困境到囧境再到佳境的亲情治愈过程，最终帮助父子实现“心灵的成长与丰富”。
     * 此剧融观赏性、教育性、娱乐性为一体,让小朋友在娱乐中受到人生启发，启迪童年快乐成长！业内前辈指出，这会是一部大人看着不瞌睡、欢笑中让你内心更加柔软的治愈性儿童音乐剧。
     * 首创性地将编创内容与未来教学相结合，将演员的针对性训练和幕间内容转化为单元课件，为教学备书，切实实现舞台实践与艺能培养的“双丰收”，这对于参演演员来说，也是一次不可多得的成长机会。
     * <p>
     * plot :     赖特宁和赖罗庚原本就是一对关系紧张的父子。每当两人争吵时家里的牙刷、pad、狗狗、日记本就都成为了两人战争的无辜受害者，终于pad打算用神奇闹钟带着小伙伴们群起反抗！
     * <p>
     * 这一次故事发生在闪电妈妈出差后，父子两人因为各种生活矛盾起了争执，赖特宁梦想参加梦想之歌大赛遭到赖罗庚极力反对，伤心又气愤的赖特宁对pad发出了希望爸爸消失的愿望!在pad的帮忙召唤下时间精灵真的将赖罗庚送回了过去。
     * <p>
     * 正当小伙伴们和赖特宁开心狂欢时，班主任打来的电话敲碎了大家的美梦，此次梦想之歌大赛是家庭赛事，赖罗庚必须参加，否则赖特宁就没有参赛资格。赖特宁由于狂欢过度身体不适和参赛需要爸爸，意识到爸爸的重要性，想让伙伴们帮助自己回到过去找到赖罗庚。小伙伴们受够了赖罗庚的折磨不想让他回来，于是pad不情愿的召唤了时间精灵送走了赖特宁。在赖特宁走后，小伙伴们对赖特宁的爱与担心最终超越了对赖罗庚的厌恶，一起回到了过去。
     * <p>
     * 穿越回过去的赖特宁奇妙的变成了自己的爷爷赖卫国，上演了一场和赖罗庚的父子反转。赖罗庚重新经历了小学时光，并在此与同学冲突，被老师请来了变成赖卫国的赖特宁。父子反转后的见面，两人矛盾百出，关系依然恶劣。小伙伴们为了帮助父子俩真正和解，让赖特宁恢复了之前的记忆发现了爸爸的登台梦想，并慢慢的开始理解爸爸。最后借助时间精灵魔法使父子俩穿越到了梦想之歌的舞台。虽然最后父子俩依然没有赶上比赛，完成自己的梦想，但是两人却因这次奇妙旅程收获了比比赛更加宝贵的爱和理解。<img class='plot_img' src='https://www.showonline.com.cn/image/2017/09/06/fa159b46fe4c4c619cd1877c28956af9.jpg'></img>
     * awardsDescpt : <p></p>
     * requirements : <p><table class='requirements_tb'></table></p>
     * status : 1
     * userId : 8a999cce5e4ccfa8015e55014baa006c
     * createTime : 1508347961000
     * updateTime : null
     * performanceBeginDate : 1507651200000
     * performanceEndDate : null
     * regionName : null
     * classifyName : 音乐剧
     * pictures : [{"masterUrl":"https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg","largeUrl":"https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg","middleUrl":"https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg","smallUrl":"https://www.showonline.com.cn/image/2017/09/06/84826db9521144628d10b0095b765cad@thumb.jpeg","repertoryId":null,"height":512,"width":362,"createTime":null}]
     * staffs : [{"id":"402894115f2f3d8f015f2f3e11540175","photo":"https://www.showonline.com.cn/image/2017/09/06/61225808c4f24984aab545aa9ce42d46.jpg","name":"刘能一","roleName":"总导演","descpt":"毕业于中央戏剧学院（戏剧·影视）导演专业，师从著名教授钮心慈，哈萨克民族音乐剧《可爱的一朵玫瑰花》总导演，荣获国家精品剧目；江西省艺术节·玉茗花戏剧艺术节赣腔歌舞剧《月缺江湖》总导演；国家艺术基金重点项目音乐剧《黑眼睛》编剧、总导演；天津卫视明星竞演秀《国色天香》艺术总监，魔幻现实主义童话剧《七色花》总编剧。 ","contentId":null,"contentType":null,"createTime":null},{"id":"402894115f2f3d8f015f2f3e11540176","photo":"https://www.showonline.com.cn/image/2017/09/06/0e78a8d411ce42fd9e4d7c71920d2a6a@thumb.jpg","name":"张小柯","roleName":"作曲","descpt":"音乐制作人，中国音乐家协会流行音乐协会会员，其舞台剧作品曾多次获得\u201c五个一工程奖\u201d、\u201c国家艺术基金\u201d等国家奖项。 北京广播电台、北京电视台联合举办大型晚会《北京榜样》的音乐总监；话剧作品《你好！打劫》、《你好！疯子》等；张纪中导演《西游记》配乐；尤小刚导演《西施秘史》配乐。 音乐剧、儿童剧作品：《雪孩子》、《北京遇上西雅图》、《托马斯&朋友》、《团仔圆妞》、《小羊肖恩》、《植物大战僵尸》、《疯狂小糖》、《月亮姐姐与嘟噜嘀嘟农场》、《巴拉拉小魔仙之星梦派对》等。","contentId":null,"contentType":null,"createTime":null},{"id":"402894115f2f3d8f015f2f3e11540177","photo":"https://www.showonline.com.cn/image/2017/09/06/b9fe1c45f6574a99ae8858bc05a7aed1@thumb.jpeg","name":"王雨姝","roleName":"饰 李想","descpt":"毕业于四川音乐学院，音乐剧表演。 参演作品： 周杰伦音乐剧《不能说的秘密》 饰演：杨娜/孙晴依（女二号） 雷松音乐剧《啊，鼓岭》 饰演：伊丽莎白（女主角） 魔幻新媒体剧《从前有座山》 饰演：盛装女 原创音乐剧 小柯剧场《百万约定》饰演：王金花 原创音乐剧 小柯剧场《稳稳的幸福》饰演：小童谣","contentId":null,"contentType":null,"createTime":null}]
     * agency : 青麦演艺经纪（北京）有限公司
     * viewTimes : 96
     */

    private String id;
    private String logo;
    private String title;
    private int classifyId;
    private String linkman;
    private String linkTel;
    private int peopleNum;
    private int expense;
    private int regionId;
    private int seatingRequir;
    private long premiereTime;
    private int rounds;
    private int showLast;
    private String description;
    private String plot;
    private String awardsDescpt;
    private String requirements;
    private int status;
    private String userId;
    private long createTime;
    private Object updateTime;
    private long performanceBeginDate;
    private Object performanceEndDate;
    private Object regionName;
    private String classifyName;
    private String agency;
    private int viewTimes;
    private ArrayList<PicturesBean> pictures;
    private List<StaffsBean> staffs;
    private String shareLink;

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
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

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getSeatingRequir() {
        return seatingRequir;
    }

    public void setSeatingRequir(int seatingRequir) {
        this.seatingRequir = seatingRequir;
    }

    public long getPremiereTime() {
        return premiereTime;
    }

    public void setPremiereTime(long premiereTime) {
        this.premiereTime = premiereTime;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getShowLast() {
        return showLast;
    }

    public void setShowLast(int showLast) {
        this.showLast = showLast;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getAwardsDescpt() {
        return awardsDescpt;
    }

    public void setAwardsDescpt(String awardsDescpt) {
        this.awardsDescpt = awardsDescpt;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public long getPerformanceBeginDate() {
        return performanceBeginDate;
    }

    public void setPerformanceBeginDate(long performanceBeginDate) {
        this.performanceBeginDate = performanceBeginDate;
    }

    public Object getPerformanceEndDate() {
        return performanceEndDate;
    }

    public void setPerformanceEndDate(Object performanceEndDate) {
        this.performanceEndDate = performanceEndDate;
    }

    public Object getRegionName() {
        return regionName;
    }

    public void setRegionName(Object regionName) {
        this.regionName = regionName;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
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

    public List<StaffsBean> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<StaffsBean> staffs) {
        this.staffs = staffs;
    }


    public static class StaffsBean implements Serializable{
        /**
         * id : 402894115f2f3d8f015f2f3e11540175
         * photo : https://www.showonline.com.cn/image/2017/09/06/61225808c4f24984aab545aa9ce42d46.jpg
         * name : 刘能一
         * roleName : 总导演
         * descpt : 毕业于中央戏剧学院（戏剧·影视）导演专业，师从著名教授钮心慈，哈萨克民族音乐剧《可爱的一朵玫瑰花》总导演，荣获国家精品剧目；江西省艺术节·玉茗花戏剧艺术节赣腔歌舞剧《月缺江湖》总导演；国家艺术基金重点项目音乐剧《黑眼睛》编剧、总导演；天津卫视明星竞演秀《国色天香》艺术总监，魔幻现实主义童话剧《七色花》总编剧。
         * contentId : null
         * contentType : null
         * createTime : null
         */

        private String id;
        private String photo;
        private String name;
        private String roleName;
        private String descpt;
        private Object contentId;
        private Object contentType;
        private Object createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getDescpt() {
            return descpt;
        }

        public void setDescpt(String descpt) {
            this.descpt = descpt;
        }

        public Object getContentId() {
            return contentId;
        }

        public void setContentId(Object contentId) {
            this.contentId = contentId;
        }

        public Object getContentType() {
            return contentType;
        }

        public void setContentType(Object contentType) {
            this.contentType = contentType;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }
    }
}
