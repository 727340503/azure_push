package payme.notification.test;

import org.apache.http.concurrent.FutureCallback;

import com.windowsazure.messaging.NotificationOutcome;

public class SendPushFutureCallback implements FutureCallback<NotificationOutcome> {

	private byte[] LOCK_PASS = new byte[0];
	private boolean pass = false;
	private String trackingId;
	private String notificationId;
	//private NotificationOutcome outcome;
	
	public SendPushFutureCallback() {
		// TODO Auto-generated constructor stub
	}

	//@Override
	public void completed(NotificationOutcome result) {
		System.out.println("--->completed");
		System.out.println("TrackingId:"+result.getTrackingId());
		trackingId = result.getTrackingId();
		notificationId = result.getNotificationId();
		setPass(true);
	}

	//@Override
	public void failed(Exception ex) {
		// TODO Auto-generated method stub
		System.out.println("--->failed");
	}

	//@Override
	public void cancelled() {
		// TODO Auto-generated method stub
		System.out.println("--->cancelled");
	}

	public boolean isPass() {
		synchronized(LOCK_PASS) {
			return pass;
		}
	}

	public void setPass(boolean pass) {
		synchronized(LOCK_PASS) {
			this.pass = pass;
		}
	}

	public String getTrackingId() {
		return trackingId;
	}

	public String getNotificationId() {
		return notificationId;
	}

}
