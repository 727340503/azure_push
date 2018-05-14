package payme.notification.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaiduMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> data;
	
	private final Map<String, String> customContent;
	
	private final String lang;
	
	private final List<String> userIds;
	
	public BaiduMessage(String lang) {
		this.data = new LinkedHashMap<String, String>();
		this.customContent = new LinkedHashMap<String, String>();
		this.userIds = new ArrayList<String>();
		this.lang = lang;
	}

	public void addMessage(String message){
		data.put("description", message);
	}
	
	public void addTitle(String title) {
		data.put("title", title);
	}
	
	public void addUrl(String url){
		data.put("url", url);
	}
	
	public void addOpenType(String openType) {
		data.put("open_type", openType);
	}
	
	public void addParameter(String key, String value){
		customContent.put(key, value);
	}
	
	public void addUserId(String userId){
		userIds.add(userId);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		if (!data.isEmpty()) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				if(entry.getKey().equals("open_type")) {
					builder.append("\"" + entry.getKey() + "\"").append(":").append("" + entry.getValue()).append(",");
				}else {
					builder.append("\"" + entry.getKey() + "\"").append(":").append("\"" + entry.getValue()).append("\",");
				}
			}
			builder.delete(builder.length() - 1, builder.length());
		}
		if (builder.charAt(builder.length() - 1) == ' ') {
			builder.delete(builder.length() - 2, builder.length());
		}
		if(!customContent.isEmpty()) {
			builder.append(",\"custom_content\":{");
			for (Map.Entry<String, String> entry : customContent.entrySet()) {
				builder.append("\"" + entry.getKey() + "\"").append(":").append("\"" + entry.getValue()).append("\",");
			}
			builder.delete(builder.length() - 1, builder.length());
			builder.append("}");
		}
		
		if (builder.charAt(builder.length() - 1) == ' ') {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append("}");
		return builder.toString();
	}
	
	public String getTabString(){
		StringBuilder builder = new StringBuilder("");
		if(!userIds.isEmpty()){
			builder.append("(");
			for(String userId : userIds){
				builder.append(userId+" || ");
			}
			
			builder.delete(builder.length() - 4, builder.length());
			builder.append(")");
		}
		if(builder.length() > 0){
			builder.append(" && "+ lang);
		}else{
			builder.append(lang);
		}
		
		return builder.toString();
	}
	
	public static void main(String[] args) {
		BaiduMessage msg = new BaiduMessage("en_US");
		msg.addMessage("test");
		msg.addTitle("title");
		msg.addParameter("type", "1");
		
		System.out.println(msg.toString());
	}
}
