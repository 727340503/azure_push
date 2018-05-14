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
import com.windowsazure.messaging.BaiduCredential;
import com.windowsazure.messaging.BaiduRegistration;
import com.windowsazure.messaging.CollectionResult;
import com.windowsazure.messaging.GcmRegistration;
import com.windowsazure.messaging.Notification;
import com.windowsazure.messaging.NotificationHub;
import com.windowsazure.messaging.NotificationHubJob;
import com.windowsazure.messaging.NotificationHubsException;
import com.windowsazure.messaging.NotificationOutcome;
import com.windowsazure.messaging.NotificationTelemetry;
import com.windowsazure.messaging.Registration;

import junit.framework.TestCase;

public class AzureNotificationBaiduTest extends TestCase {
	
	//dev
	NotificationHub hub = new NotificationHub(
			"Endpoint=sb://push-ns-prod-hk-qtsa.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=WN70RzHvs1f7L2RqHFXe9mBOMBhsU554yLQADDXAir8=",
			"push-prod-hk-qtsa-shared");
	
	//Simon i6
	private String baidu_Token_1 = "4519915673561229544";
	//Simon i4s
	private String baiduToken_2 = "4570470623583933894";
	                             
	private int deviceType_baidu = 3;
	
	private String en_US = "en_US";
	private String zh_CN = "zh_CN";
	private String zh_HK = "zh_HK";
	
	private String emoji = "🚫 🔞 🚯 🚱 🚳 🚷";
	
	
	public void test_deleteRegistration()throws Exception{
		String registrationId = "1701241497248795739-3179917161360323013-3";
		// 删除旧注册记录
		hub.deleteRegistration(registrationId);

	}
	
	private void registration_DeviceToken(int deviceType, String baiduChannelId,String baiduUserId, String userId, String lang)throws Exception{
		//check exist deviceToken
		CollectionResult deviceTokenResult = hub.getRegistrationsByChannel(baiduChannelId);
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
		Registration reg = new BaiduRegistration(baiduUserId, baiduChannelId);
		
		if(reg != null){
			if(userId != null && !userId.isEmpty()){
				reg.getTags().add(userId);
			}
			reg.getTags().add(lang);
			hub.createRegistration(reg);
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
	
	public void test_baidu_registration_deviceToken() throws Exception {
		String user1 = "1667";
		String baiduUserId = "1083683284072334636";
		registration_DeviceToken(deviceType_baidu,baiduToken_2,baiduUserId,user1,en_US);
	}
	
	public void test_getRegistrationsByTag() throws Exception {
		getRegistrationsByTag();
	}
	
	public void test_getRegistrantion() throws NotificationHubsException {
		String baiduUserId = "1005010835336899560";
		Registration r = new BaiduRegistration(baiduUserId, baidu_Token_1);
		r.getTags().add(baidu_Token_1+baiduUserId);
		hub.createRegistration(r);
		
		CollectionResult result =  hub.getRegistrationsByTag(baidu_Token_1+baiduUserId);
		List<Registration> list = result.getRegistrations();
		for(Registration rl:list){
			System.out.println(rl.getXml());
		}
		
	}
	
	public void test_send_baidu_push()throws Exception{
		BaiduMessage msg = buildBaiduMessage();
		System.out.println("baidu message -> "+msg.toString());
		System.out.println("baidu tab set -> "+msg.getTabString());
		//"{\"title\":\"((Notification title))\",\"description\":\"Hello from Azure\"}"
		Notification n = Notification.createBaiduNotifiation(msg.toString());

		NotificationOutcome outCome = hub.sendNotification(n,msg.getTabString());
		System.out.println("TrackingId:"+outCome.getTrackingId());
		System.out.println("NotificationId:"+outCome.getNotificationId());
	}
	
	public void test_query_notification() throws NotificationHubsException {
		List<NotificationHubJob> jobs = hub.getAllNotificationHubJobs();
		System.out.println(jobs.size());
		
		for(NotificationHubJob job : jobs) {
			System.out.println(job.getXml());
		}
	}
	
	private BaiduMessage buildBaiduMessage(){
		//BAIDU format
		//String message = "{\"data\":{\"key1\":\"$(value1)\"}}";
		
		//zh_CN
		//en_US
		BaiduMessage msg = new BaiduMessage(en_US);
		msg.addMessage("Hello qtsa baidu azure push!");
		msg.addTitle("优惠通知");
		msg.addOpenType("0");
		msg.addParameter("type", "2");
		
		//aosMessage.addSound("a.sound");
		//aosMessage.addParameter("aa", "a value");
		
		//根据tab发送
		msg.addUserId("1663");
		msg.addUserId("1665");
		msg.addUserId("1662");
		msg.addUserId("1667");
		
		return msg;
	}
	
}
