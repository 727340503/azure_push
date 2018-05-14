package payme.notification.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.windowsazure.messaging.AppleRegistration;
import com.windowsazure.messaging.BaiduRegistration;
import com.windowsazure.messaging.CollectionResult;
import com.windowsazure.messaging.GcmRegistration;
import com.windowsazure.messaging.Notification;
import com.windowsazure.messaging.NotificationHub;
import com.windowsazure.messaging.NotificationHubsException;
import com.windowsazure.messaging.NotificationOutcome;
import com.windowsazure.messaging.NotificationTelemetry;
import com.windowsazure.messaging.Registration;

import junit.framework.TestCase;

public class AzureNotificationTest extends TestCase {
	
	//dev
	NotificationHub hub = new NotificationHub(
			"Endpoint=sb://push-ns-prod-hk-qtsa.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=WN70RzHvs1f7L2RqHFXe9mBOMBhsU554yLQADDXAir8=",
			"push-prod-hk-qtsa-shared");
	
	private String aosToken_1 = "dH6pcE8PmVA:APA91bGq5IhHkUUVYOtj1IITFamNPwq9WvAnFYd869bzE4K_QPH9MiIXnmuLwJtjkB79AMtrqYoQh-y6RnO2AhEmuj17zzGTaSBY3mvZ56c5av4BNo_1okZnL7t0DZ7ireCKkqyFGZy";
	//aos LG
	private String aosToken_2 = "xxxxxxxxx";
	
	private String iosToken_1 = "85a3289b09dd46cd51f9130eb6f3afedc7c58f2e5e4717810965a6b41cb366c1";
	//Simon i6
	private String iosToken_2 = "xxxxxxxxx";
	//Simon i4s
	private String iosToken_3 = "xxxxxxxxx";
	
	//Simon i6
	private String baidu_Token_1 = "4570470623583933894";
	//Simon i4s
	private String baiduToken_2 = "xxxxxxxxx";
	                             
	private int deviceType_ios = 1;
	private int deviceType_aos = 2;
	private int deviceType_baidu = 3;
	
	private String en_US = "en_US";
	private String zh_CN = "zh_CN";
	private String zh_HK = "zh_HK";
	
	private String emoji = "🚫 🔞 🚯 🚱 🚳 🚷";
	
	
	public void test_getRegistrationsByChannel()throws Exception{
		String deviceToken = iosToken_3;
		CollectionResult deviceTokenResult = hub.getRegistrationsByChannel(deviceToken);
		if(deviceTokenResult.getRegistrations() != null && deviceTokenResult.getRegistrations().size() > 0){
			for(Registration r : deviceTokenResult.getRegistrations()){
				//删除旧注册记录
				//hub.deleteRegistration(r.getRegistrationId());
				System.out.println("r.getRegistrationId():"+r.getRegistrationId());
				System.out.println("r.getTags():"+r.getTags());
			}
		}
	}
	
	public void test_deleteRegistration()throws Exception{
		String registrationId = "1701241497248795739-3179917161360323013-3";
		// 删除旧注册记录
		hub.deleteRegistration(registrationId);

	}
	
	private void registration_DeviceToken(int deviceType, String deviceToken, String userId, String lang)throws Exception{
		//check exist deviceToken
		CollectionResult deviceTokenResult = hub.getRegistrationsByChannel(deviceToken);
		if(deviceTokenResult.getRegistrations() != null && deviceTokenResult.getRegistrations().size() > 0){
			for(Registration r : deviceTokenResult.getRegistrations()){
				//删除旧注册记录
				hub.deleteRegistration(r.getRegistrationId());
			}
		}
		
		//check exist tab "userId"
		if(userId != null && !userId.isEmpty()){
			CollectionResult tabUserIdResult = hub.getRegistrationsByTag(userId);
			if(tabUserIdResult.getRegistrations() != null && tabUserIdResult.getRegistrations().size() > 0){
				for(Registration r : tabUserIdResult.getRegistrations()){
					r.getTags().remove(userId);
					//去除旧绑定的userId
					hub.updateRegistration(r);
				}
			}
		}
		
		//创建新注册deviceToken & userId 绑定
		Registration reg = null;
		if(deviceType==1){
			reg = new AppleRegistration(deviceToken);
			reg.getTags().add("qtsa_ios");
		}else if(deviceType==2){
			reg = new GcmRegistration(deviceToken);
			reg.getTags().add("qtsa_aos");
		}
		
		if(reg != null){
			if(userId != null && !userId.isEmpty()){
				reg.getTags().add(userId);
			}
			reg.getTags().add(lang);
			hub.createRegistration(reg);
		}
	}
	
	private void unbindByUserSessionIds(String userId)throws Exception{
		//check exist tab "userId"
		if(userId != null && !userId.isEmpty()){
			CollectionResult tabUserIdResult = hub.getRegistrationsByTag(userId);
			if(tabUserIdResult.getRegistrations() != null && tabUserIdResult.getRegistrations().size() > 0){
				for(Registration r : tabUserIdResult.getRegistrations()){
					r.getTags().remove(userId);
					//去除旧绑定的userId
					hub.updateRegistration(r);
				}
			}
		}
	}
	
	private void getRegistrationsByTag() throws Exception {
		
		CollectionResult result;
		List<Registration> list;
		
		//根据Tab查找
		//result = hub.getRegistrationsByTag("payme_aos");
		result = hub.getRegistrations();
				
		//根据deviceToken查找
		//result =hub.getRegistrationsByChannel("devicetoken");
		
		list = result.getRegistrations();
		System.out.println("---");
		for(Registration r:list){
//			hub.deleteRegistration(r.getRegistrationId());
//			System.out.println(r.getRegistrationId());
			System.out.println(r.getXml());
		}
	}
	
	public void test_registration_deviceToken() throws Exception {
		String user1 = "1662";
		registration_DeviceToken(deviceType_ios,iosToken_1,user1,en_US);
	}
	
//	public void test_baidu_registration_deviceToken() throws Exception {
//		String user1 = "1662";
//		String baiduUserId = "1083683284072334636";
//		registration_DeviceToken(deviceType_baidu,baidu_Token_1,baiduUserId,user1,en_US);
//	}
	
	public void test_getRegistrationsByTag() throws Exception {
		getRegistrationsByTag();
	}
	
	private AosMessage buildAosMessage(){
		//AOS format
		//String message = "{\"data\":{\"message\":\"Hello from Java!\"}}";
		
		//zh_CN
		//en_US
		AosMessage aosMessage = new AosMessage(en_US);
		aosMessage.addMessage("Hello payme aos azure push!");
		aosMessage.addBadge("0");
		//aosMessage.addSound("a.sound");
		//aosMessage.addParameter("aa", "a value");
		
		//根据tab发送
		//aosMessage.addUserId("1663");
		//aosMessage.addUserId("1665");
		aosMessage.addUserId("1662");
		
		return aosMessage;
	}
	
	public void test_send_aos_push()throws Exception{
		AosMessage aosMessage = buildAosMessage();
		System.out.println("aos message -> "+aosMessage.toString());
		System.out.println("aos tab set -> "+aosMessage.getTabString());
		Notification n = Notification.createGcmNotifiation(aosMessage.toString());

		NotificationOutcome outCome = hub.sendNotification(n,aosMessage.getTabString());
		System.out.println("TrackingId:"+outCome.getTrackingId());
		System.out.println("NotificationId:"+outCome.getNotificationId());
	}
	
	//not use
	public void test_createTemplateNotification()throws Exception{
		Map<String, String> prop =  new HashMap<String, String>();
		prop.put("prop1", "v1");
		prop.put("prop2", "v2");
		Notification n = Notification.createTemplateNotification(prop);
		hub.sendNotification(n);
	}
	
	private IosMessage buildIosMessage(){
		//IOS format
		//String message = "{\"aps\":{\"alert\":\""+str+"\"}}";
		
		//zh_CN
		//en_US
		IosMessage iosMessage = new IosMessage(en_US);
		iosMessage.addMessage("Hello payme ios azure push!");
		//iosMessage.addMessage("您已向我们的客户服务团队提交查询。");
		iosMessage.addBadge("0");
		//iosMessage.addSound("aaa");
		iosMessage.addParameter("msgtitle", "PayMe from HSBC");
		
		//根据tab发送
		iosMessage.addUserId("1665");
		iosMessage.addUserId("1662");
		iosMessage.addUserId("1666");
		
		return iosMessage;
	}
	
	public void test_send_ios_push() throws Exception {
		IosMessage iosMessage = buildIosMessage();
		System.out.println("ios message -> "+iosMessage.toString());
		System.out.println("ios tab set -> "+iosMessage.getTabString());
		Notification n = Notification.createAppleNotifiation(iosMessage.toString());
		
		NotificationOutcome outCome = hub.sendNotification(n, iosMessage.getTabString());
		System.out.println("TrackingId:"+outCome.getTrackingId());
		System.out.println("NotificationId:"+outCome.getNotificationId());
	}
	
	private void getNotificationTelemetry(String notificationId)throws Exception{
		NotificationTelemetry nt = hub.getNotificationTelemetry(notificationId);
		Map<String, Integer> map = nt.getApnsOutcomeCounts();
		
		if(map == null){
			return;
		}
		
		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Integer integer = map.get(key);
			System.out.println("Key="+key+", value="+integer.intValue());
		}
	}
	
	public void test_send_ios_push_callback() throws Exception {
		IosMessage iosMessage = buildIosMessage();
		System.out.println("ios message -> "+iosMessage.toString());
		System.out.println("ios tab set -> "+iosMessage.getTabString());
		Notification n = Notification.createAppleNotifiation(iosMessage.toString());
		
		SendPushFutureCallback callback = new SendPushFutureCallback();
		hub.sendNotificationAsync(n, iosMessage.getTabString(), callback);
		while(!callback.isPass()){
		}
		String notificationId = callback.getNotificationId();
		String trackingId = callback.getTrackingId();
		NotificationTelemetry nt = hub.getNotificationTelemetry(notificationId);
		Map<String, Integer> map = nt.getApnsOutcomeCounts();
		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Integer integer = map.get(key);
			System.out.println("Key="+key+", value="+integer.intValue());
		}
	}
	
	public void test_getNotificationTelemetry()throws Exception{
		String notificationId = "abb7023c826c467cb0b916d8a0a405fb_20180110083822_2006824373_13_QNHQueue212fae2d780846ca80747d64df605843-S17169973579350087";
		getNotificationTelemetry(notificationId);
	}
	
	public void test_scheduleNotification() throws Exception {
		IosMessage iosMessage = buildIosMessage();
		Notification n = Notification.createAppleNotifiation(iosMessage.toString());
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, 60);
		Date srcDate = c.getTime();
		
		NotificationOutcome outCome = hub.scheduleNotification(n,iosMessage.getTabString(), srcDate);
		System.out.println("TrackingId:"+outCome.getTrackingId());
		System.out.println("NotificationId:"+outCome.getNotificationId());
	}

	public void test_cancelScheduledNotification()throws Exception{
		String notificationId = "3bc0e3209b04420fb93c83216c51a05d_20180110085226_2006824373_11_QNHQueueed0cfeef4af94d1c9993e12400b8cd8e-S61643019900718633";
		hub.cancelScheduledNotification(notificationId);
	}
	
}
