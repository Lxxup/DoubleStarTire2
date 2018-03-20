package com.ds.tire.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

public class Constant
{
//	public static final String WEB_SITE = "http://211.64.154.160:8080/tire/"; //produce测试 
	
	public static final String WEB_SITE = "http://27.223.71.70:8080/tire/"; 
	 public static final String TIRE_URL  = "http://27.223.71.70:8080/tire/images/tirepicture/";
	 public static final String TIRE_URL2  = "http://27.223.71.70:8080/tire/images/tirepicture/";
	 public static final String Sign_URL  = "http://27.223.71.70:8080/tire/images/register/";
//	 public static final String Sign_URL  = "http://211.64.154.160:8080/tire/images/SignPhoto/";
//	 public static final String Sign_URL  = "http://211.64.154.159:8080/tire/images/SignPhoto/";
//    	public static final String WEB_SITE = "http://27.223.71.70:8080/tire/";    
//    	public static final String WEB_SITE = "http://211.64.154.159:8080/tire/";    
	//public static final String BROKER_URL             = "tcp://211.64.154.170:1883";

    //public static final String BROKER_URL = "tcp://115.28.135.178:1883";//阿里云服务器推送
       
//     public static final String WEB_SITE = "http://119.167.96.106:8080/tire/";
//     public static final String BROKER_URL = "tcp://115.28.135.178 1883";
    
	
//	public static final String WEB_SITE               = "http://shuangxing.seiouc.net/tire";//域名
	
    //外网
//    public static final String WEB_SITE               = "Http://219.147.15.227:18080/tire/";
    public static final String BROKER_URL = "tcp://27.223.71.70:1883";//服务器推送
    
    public static final String VERSION                = "version";
    public static final String TYPE                   = "1";
    public static final String ROLE                   = "1";
    public static final String UID                   = "uid";
	public static final String CID                    = "cid";
	public static final String OID                    = "oid";

    public static final String DBNAME                 = "cartest.db";
    public static final int    DBVERSION              = 1;
    public static String       ADD_OIL_DB             = "addoil";
    
    public static final String ACTION_MQTT_START      = "action_mqtt_start";
    public static final String ACTION_MQTT_STOP       = "action_mqtt_stop";
    public static final String KEY_PUSH_ID            = "key_push_id";
    // public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_RESCUE_LAT         = "rescue_lat";
    public static final String KEY_RESCUE_LON         = "rescue_lon";
    public static final String KEY_RESCUE_NUM         = "rescue_num";
    public static final String KEY_RESCUE_PHONE       = "rescue_phone";
    public static final String KEY_RESCUE_ID          = "rescue_id";
    public static final String KEY_FINISH_RESCUE      = "finish_rescue";
    public static final String KEY_JSON               = "key_json";
    public static final String ACTION_RESCUE_LOCATION = "action_rescue_location";
    public static final String ACTION_RESCUE_DETAILS  = "action_rescue_details";
    public static final String ACTION_RESCUE_NOCAR  = "action_rescue_nocar";
//    public static final String WENDUTOP= null;
//    public static final String WENDUBOT= null;
//    public static final String YAQIANGTOP= null;
//    public static final String YAQIANGBOT= null;
   // public static boolean IF_FIRST_IN=true;
	public static final Intent ACTION_BLUETOOTH_SERVICE = null;
   
    //安装的设备的序列号数组
    public static List<String> getTitle()
    {
        List<String> title = new ArrayList<String>();
        title.add("定期检查轮胎");
        title.add("胎压");
        title.add("冬季轮胎");
        title.add("正确的轮胎气压");
        title.add("平衡和调正");
        title.add("定期轮换");
        title.add("目视检查");
        title.add("修理");
        title.add("轮胎购买");
        return title;
    }
    
    public static List<String> getContent()
    {
        List<String> content = new ArrayList<String>();
        content.add("当检查轮胎时，要求包括备胎在内都要检查，并检查轮胎气压。发现有否鼓包，裂缝，割伤，扎钉和不正常的胎 面磨损等现象。使用损伤的轮胎可能会导致轮胎毁坏。在有坑洞和岩石或其它物体的道路上驾驶车辆可能会损坏轮胎并使车辆定位不良，因此在这样的道路上行驶 时，需要慢慢地、小心地行驶，通过这些路段后检查轮胎是否有任何损伤，例如：割伤，鼓包，扎钉，胎面不正常磨损等等。");
        content.add("对于轮胎性能和寿命来说，正确地充气至关重要，对于车辆的行驶体验和安全而言也同样重要。轮胎承载着车辆的全部重量，当充气不足或者充气过量时，它们就无法正常工作。在充气不足的情况下使用轮胎，也会导致轮胎的损毁。");
        content.add("进入冬季，您就应该给爱车换上冬季胎，以确保全年的道路安全");
        content.add("持正确的气压是驾驶员能够为轮胎做的最重要的事情。大概每隔一个月，轮胎就会损失 4.5Kb/cm2的气压。定期检查气压非常重要，这样可确保轮胎永远不会过压和欠压。");
        content.add("让轮胎保持平衡并正确调正非常重要，这不仅可延长轮胎的使用寿命，也有助于保证驾驶员安全和充分发挥汽车性能。");
        content.add("汽车的重量没有均匀分配到所有四个轮胎上。因此，定期轮换对于保持轮胎的均匀磨损和最大限度延长轮胎寿命是不可缺少的。");
        content.add("通常，只需用目视检查就能发现轮胎存在的潜在问题。在下次检查气压时，请查看胎面上是否有可能划破轮胎的尖利物体。即便划口不深，没有立即让轮胎漏气，小裂口引起的破裂和凹陷最终也会变得更糟，并导致汽车在行驶时出现问题。");
        content.add("了解应该修理轮胎还是应该更换轮胎非常重要。如果轮胎漏气，则必须将其从车辆上拆下，进行彻底的内部检查，确保它没有受到损伤。既使行驶非常短的距离也会漏气的轮胎经常是已损坏和无法修理的轮胎。");
        content.add("尽量选择在专卖店或者口碑好的地方购买;注意轮胎的生产日期;挑选时注意轮胎的细节;选择适合自己车的轮胎");
        return content;
    }
  //安装的设备的序列号数组
    public static List<String> getPianfang()
    {
        List<String> ptitle = new ArrayList<String>();
        ptitle.add("开长途车注意事项");
        ptitle.add("如何养“神”才能容颜美呢？");
        ptitle.add("味精可止牙痛");
        ptitle.add("温水浸泡双手可治酒后头痛");
        ptitle.add("蚊虫叮咬后止痒方法");
        ptitle.add("无花果叶治脚气");
        ptitle.add("西瓜皮美容");
        ptitle.add("仙人掌能治无名肿痛与腮腺炎");
        ptitle.add("消除脚汗臭");
        ptitle.add("醒酒秘方");
        return ptitle;
    }
    
    public static List<String> getPcontent()
    {
        List<String> pcontent = new ArrayList<String>();
        pcontent.add("注意 疲劳驾驶 酒后驾车 防盗抢  车的保养 天热的时候要注意轮胎温度 气压 经常检查刹车 先在基本都超载 放大坡的时候 不要用空挡 一定用档憋着 注意路基是否松软 不要太靠右边  雨天注意弯道提前减速");
        pcontent.add("首先，要拥有良好的气质。良好的气质培养需要具有很高的思想觉悟和文化修养，塑造美好的心灵，可通过欣赏音乐、艺术、诗歌、园艺、大自然，而获得喜悦心情，“喜则志和气达，荣卫通利”，则容光焕发、容颜秀美。在这一过程中，不仅人的精神得到调节，为容颜美“加分”，而且能起到提高修养、开阔心胸的作用。其次，培养良好的情绪，至少应省思节怒。中医认为，思虑过度，会耗伤心神，损伤脾胃，令人饮食减少，气血不足，进而容貌憔悴，所以对容颜美的伤害很大，欲美容者对此避忌为好。总之，养颜当先养神。人们不仅要追求外在美，更要追求心灵美，只有两者有机地结合在一起，“神貌和美”，人才能更健康、更美。。");
        pcontent.add("牙痛时, 应急的办法之一是用筷子蘸上一点味精, 放到疼痛的牙齿上, 疼痛会很快消失.");
        pcontent.add("喝白酒或葡萄酒过量引起头痛时, 可取一只脸盆, 倒入温水, 水温适中, 不宜过烫, 然后将双手和腕关节完全浸泡在水中20-30分钟, 即可使头痛很快消失或减轻。");
        pcontent.add("1，用切成片的大蒜在被蚊虫叮咬处反复偿还分钟, 有明显的 止痛去痒消炎作用, 即使被叮咬处已成大包或发炎溃烂, 均可用大蒜擦, 一般12小时后即可消炎去肿, 溃烂的伤口24小时后可痊愈. 皮肤过敏者应慎用.2，脚上被蚊虫叮了几个红点. 倒半盆滚烫的开水, 找一块干净的方毛巾, 把毛巾的一角放入水中, 然后轻轻地烫痒处(注意只烫痒处, 要防止开水下流引起烫伤), 反复几次, 痒感片刻即消. 3，人体被蚊子叮咬后不仅红肿起包且刺痒难忍, 可用清水冲洗被咬处, 不要全擦干, 然后用一个湿手指头蘸一点洗衣粉涂于被咬处, 可立即止痒且红肿很快消失, 待红肿消失后可用 清水将洗衣粉冲掉. 4，用湿手指蘸点盐搓擦患处也去痛痒. 5，用湿肥皂涂患处即刻止痒, 红肿渐消. 6，明矾蘸唾液擦痒处两三次即好. 7，被蚊虫叮咬后, 可立即涂搽1至2滴氯霉素眼药水, 即可止痛止痒. 由于氯霉素眼药水有消炎作用, 蚊虫叮咬后已被抠破有轻度感染发炎者, 涂搽后还可消炎. 肤被蚊子叮后, 可用肥皂(或香皂)蘸水涂擦, 稍等片刻, 即可止痒.");
        pcontent.add("取无花果叶数片, 加水煮10分钟左右, 待水温合适时泡洗患足10分钟, 每日1-2次, 一般三～五天即愈.");
        pcontent.add("将西瓜皮切成条束状(以有残存红瓤为佳), 直接在脸部反复揉搓5分钟, 然后用清水洗脸, 每周两次, 可保持皮肤细嫩洁白。");
        pcontent.add("脚腕忽然红肿, 火烧火燎. 仙人掌能治:将盆栽的仙人掌剪下一块, 放到容器里拔掉刺捣碎, 糊在红肿部位, 连糊两次,便消肿不疼了。患腮腺炎者, 用仙人掌, 第一次敷了三天, 第二次敷了两天, 便消肿不疼.");
        pcontent.add("在洗脚水中加入25克茶叶和少量食盐, 把双脚浸泡水中, 并反复搓洗5～10分钟, 脚汗臭可以消除.");
        pcontent.add("1，吃些豆腐或其他豆制品, 不仅对身体有很大好处, 而且是解酒佳品,2，鲜柑皮煮水, 再加少许细盐, 可起醒酒作用.3，茶中含有多酚类、咖啡碱、氨基酸等物质, 能兴奋中枢神经, 利于血液循环, 从而提高肝脏代谢能力，对酒精起缓解作用.");
        return pcontent;
    }
    
    
}
