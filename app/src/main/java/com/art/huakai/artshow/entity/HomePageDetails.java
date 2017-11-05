package com.art.huakai.artshow.entity;

import java.util.List;

/**
 * Created by lining on 2017/10/18.
 */
public class HomePageDetails {


    /**
     * theaters : [{"id":"402894115f2f3d8f015f2f3e123a0181","logo":"https://www.showonline.com.cn/image/2017/09/13/323b9fb21d6e4fde92a33fc3a75f2dd5@thumb.JPG","name":"北外剧场","roomName":"","expense":48000,"seating":1014,"regionName":null,"linkman":"华艺互联","linkTel":"13811615602","status":1,"createTime":null},{"id":"402894115f2f3d8f015f2f3e12cf0183","logo":"https://www.showonline.com.cn/image/2017/09/28/4b021a5c13b84c289c333001bce64e04.jpg","name":"进·剧场","roomName":"","expense":8000,"seating":266,"regionName":null,"linkman":"华艺互联","linkTel":"13962970707","status":1,"createTime":null},{"id":"402894115f2f3d8f015f2f3e134a0187","logo":"https://www.showonline.com.cn/image/2017/07/12/58275870efb048d6a86541bcf0ddca5c@thumb.jpg","name":"北京9剧场-行动剧场","roomName":"","expense":10000,"seating":514,"regionName":null,"linkman":"华艺互联","linkTel":"18612317109","status":1,"createTime":null}]
     * enrolls : [{"id":"402894115f2f4249015f2f4252570004","logo":null,"title":"演圈周报 | 初秋九月的每一天都是蓝色的","description":"测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述","authName":"演出中国","endTime":1539273600000,"createTime":1508326396503},{"id":"402894115f2f4249015f2f42515a0003","logo":null,"title":"2017北京市体制外优秀青年戏剧推荐展演","description":"测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述","authName":"演出中国","endTime":1518537600000,"createTime":1508326396250},{"id":"402894115f2f4249015f2f424fd30002","logo":null,"title":"2017青戏节发布会 | 从\u201c一\u201d马当先到\u201c十\u201d马奔腾 ","description":"测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述","authName":"演出中国","endTime":1539273600000,"createTime":1508326395859},{"id":"402894115f2f4249015f2f424e230001","logo":null,"title":"从\u201c女性主义\u201d视角看第五届乌镇戏剧节 ","description":"测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述测试描述","authName":"演出中国","endTime":1507564800000,"createTime":1508326395427}]
     * advert : {"id":"8a999cce5e473445015e4c3cd6940654","logo":"https://www.showonline.com.cn/image/2017/09/04/f38335157d8b45f5b9aa179694d8e014.jpg","target":"news://8a999cce5e473445015e4c3cd6940127","status":1,"createTime":null}
     * repertorys : [{"id":"8a999cce5d3237f0015d327125cf0001","logo":"https://www.showonline.com.cn/image/2017/07/12/ddbd5716035840629afd7e75786e8a89@thumb.JPG","title":"夜·班","agency":"北京龙百合艺术传媒有限公司","expense":30000,"peopleNum":8,"premiereTime":1508256000000,"classifyId":14,"classifyName":"实验当代","rounds":35,"status":1,"createTime":1508336509499},{"id":"8a999cce5d257ebb015d308da3e5000e","logo":"https://www.showonline.com.cn/image/2017/08/30/b173692e5eb7447d8dc067d12990b646@thumb.jpg","title":"芳心之罪（全男版）","agency":"北京奕朵文化有限公司","expense":70000,"peopleNum":12,"premiereTime":1508256000000,"classifyId":2,"classifyName":"话剧","rounds":10,"status":1,"createTime":1508335837086},{"id":"8a999cce5d599d5c015d607612820036","logo":"https://www.showonline.com.cn/image/2017/07/20/3f32fd55e94a42ec8bdd488882d9b291@thumb.JPG","title":"小红帽","agency":"北京欢乐童年木偶剧团","expense":30000,"peopleNum":16,"premiereTime":1508256000000,"classifyId":3,"classifyName":"儿童剧","rounds":100,"status":1,"createTime":1508335608457},{"id":"8a999cce5d599d5c015d60707bdc0035","logo":"https://www.showonline.com.cn/image/2017/07/20/e9eec4a59b52468a97c0e19ab78ebbdc@thumb.JPG","title":"公主与天鹅","agency":"北京欢乐童年木偶剧团","expense":30000,"peopleNum":16,"premiereTime":1508256000000,"classifyId":3,"classifyName":"儿童剧","rounds":300,"status":1,"createTime":1508335583775}]
     * banners : [{"id":"40288aba5f2865fc015f286602fe0006","logo":"http://image2.135editor.com/cache/remote/aHR0cHM6Ly9tbWJpei5xcGljLmNuL21tYml6X3BuZy9UQ0Qzb1pwcUtpYWFXQm11ZmxPWjhjcm1DTWcyeERaYlBTRkdrcnVFMjNYS1Y0azQwTDBRRmljQUhQa2liUDFnTjhZRXA3cnZ4U2hWUmNjQWlhcUloOWNPV2cvMD93eF9mbXQ9cG5n","target":"enroll://40288aba5f2865fc015f286602fe0002","status":1,"createTime":null},{"id":"40288aba5f2865fc015f286602fe0011","logo":"http://image2.135editor.com/cache/remote/aHR0cHM6Ly9tbWJpei5xcGljLmNuL21tYml6X2pwZy9UQ0Qzb1pwcUtpYVpwaDBCdnhnUjhkdExLV0twREpab1RyOVNZZDRKNWE4VjZtVWh3RVJnbVN6NmY1NE9OWGpnWU01aWNuU3ZZUVptN0tnbW84emhOTWFBLzA/d3hfZm10PWpwZWc=","target":"http://139.224.47.213:8080/showonline_website/news/detail?id=8a999cce5e56a22b015e56a89a520001","status":1,"createTime":null},{"id":"402894115f2f3d8f015f2f3e14680188","logo":"https://www.showonline.com.cn/image/2017/07/12/d9ba972e1e2c4958837816ed6ebdd110@thumb.jpg","target":"theater://402894115f2f3d8f015f2f3e14680192","status":1,"createTime":null},{"id":"402894115f2f3d8f015f2f3e2c8801a9","logo":"https://www.showonline.com.cn/image/2017/07/26/0aca5b302b5b4fd48d24086c3732223a@thumb.image","target":"talent://402894115f2f3d8f015f2f3e2c8801e7","status":1,"createTime":null},{"id":"8a999cce5da6b987015da79d981c4433","logo":"http://139.224.47.213/image/2017/08/03/7983e0b3e08846979c5b0446b4852a03.jpeg","target":"repertory://8a999cce5da6b987015da79d981c0021","status":1,"createTime":null},{"id":"8a999cce5e473445015e4c3cd6940654","logo":"https://www.showonline.com.cn/image/2017/09/04/f38335157d8b45f5b9aa179694d8e014.jpg","target":"news://8a999cce5e473445015e4c3cd6940127","status":1,"createTime":null}]
     * newses : [{"id":"8a999cce5e14535b015e18bb74e5001a","logo":"","title":"2017青戏节发布会 | 从\u201c一\u201d马当先到\u201c十\u201d马奔腾 ","description":"2017青戏节发布会 | 从\u201c一\u201d马当先到\u201c十\u201d马奔腾 ","authName":"演出中国","createTime":1507268617000},{"id":"8a999cce5e473445015e4c3cd6940127","logo":"https://www.showonline.com.cn/image/2017/09/04/f38335157d8b45f5b9aa179694d8e014.jpg","title":"演圈周报 | 初秋九月的每一天都是蓝色的","description":"演圈周报 | 初秋九月的每一天都是蓝色的","authName":"演出中国","createTime":1507268617000},{"id":"8a999cce5e2f092a015e327aa33d00e6","logo":"https://www.showonline.com.cn/image/2017/08/30/b452d8834548414e8daa10980d9e1ef9.jpg","title":"2017北京市体制外优秀青年戏剧推荐展演","description":"2017北京市体制外优秀青年戏剧推荐展演","authName":"演出中国","createTime":1507268617000},{"id":"8a999cce5e56a22b015e56a89a520001","logo":"https://www.showonline.com.cn/image/2017/09/06/0fe91d104afd482fa9d8db18472f24eb@thumb.jpg","title":"演圈事件 | 青戏节十年了，你是哪一年入了戏剧的\u201c坑\u201d？","description":"演圈事件 | 青戏节十年了，你是哪一年入了戏剧的\u201c坑\u201d？","authName":"演出中国","createTime":1507268617000}]
     * talents : [{"id":"402894115f2f3d8f015f2f3e239301e2","logo":"https://www.showonline.com.cn/image/2017/07/21/e8cd6ae0c18d4544a85010524949b2e8@thumb.jpg","name":"秦枫","school":"中央戏剧学院","age":0,"birthday":1508256000000,"classifyNames":["话剧演员","编剧","导演"],"classifyNamesStr":"话剧演员 编剧 导演 ","regionName":"","status":1,"createTime":1508326122000,"userId":"8a999cce5d599d5c015d62bf21ac0038","authentication":1,"agency":"北京现代音乐学院","linkTel":"15101116378"},{"id":"402894115f2f3d8f015f2f3e205201e1","logo":"https://www.showonline.com.cn/image/2017/07/19/9990c379e0a749138ea6e342874b9130.image","name":"丁瓦","school":"其他院校","age":0,"birthday":1508256000000,"classifyNames":["编剧","导演"],"classifyNamesStr":"编剧 导演 ","regionName":"朝阳区","status":1,"createTime":1508326121000,"userId":"8a999cce5d599d5c015d5aa6b779001c","authentication":1,"agency":"自有职业","linkTel":"18688604099"}]
     */

    private AdvertBean advert;
    private List<TheatersBean> theaters;
    private List<EnrollInfo> enrolls;
    private List<RepertorysBean> repertorys;
    private List<AdvertBean> banners;
    private List<NewsesBean> newses;
    private List<TalentsBean> talents;

    public AdvertBean getAdvert() {
        return advert;
    }

    public void setAdvert(AdvertBean advert) {
        this.advert = advert;
    }

    public List<TheatersBean> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<TheatersBean> theaters) {
        this.theaters = theaters;
    }

    public List<EnrollInfo> getEnrolls() {
        return enrolls;
    }

    public void setEnrolls(List<EnrollInfo> enrolls) {
        this.enrolls = enrolls;
    }

    public List<RepertorysBean> getRepertorys() {
        return repertorys;
    }

    public void setRepertorys(List<RepertorysBean> repertorys) {
        this.repertorys = repertorys;
    }

    public List<AdvertBean> getBanners() {
        return banners;
    }

    public void setBanners(List<AdvertBean> banners) {
        this.banners = banners;
    }

    public List<NewsesBean> getNewses() {
        return newses;
    }

    public void setNewses(List<NewsesBean> newses) {
        this.newses = newses;
    }

    public List<TalentsBean> getTalents() {
        return talents;
    }

    public void setTalents(List<TalentsBean> talents) {
        this.talents = talents;
    }

}
