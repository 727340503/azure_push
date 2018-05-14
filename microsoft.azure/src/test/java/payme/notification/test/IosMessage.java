package payme.notification.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IosMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> aps;
	
	private final Map<String, String> parameter;
	
	private final String lang;
	
	private final List<String> userIds;
	
	public IosMessage(String lang) {
		this.aps = new LinkedHashMap<String, String>();
		this.parameter = new LinkedHashMap<String, String>();
		this.userIds = new ArrayList<String>();
		this.lang = lang;
	}

	public void addMessage(String message){
		aps.put("alert", message);
	}
	
	public void addSound(String sound){
		aps.put("sound", sound);
	}
	
	public void addBadge(String badge){
		aps.put("badge", badge);
	}
	
	public void addParameter(String key, String value){
		parameter.put(key, value);
	}
	
	public void addUserId(String userId){
		userIds.add(userId);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{\"");
		if (!aps.isEmpty()) {
			builder.append("aps\":{");
			for (Map.Entry<String, String> entry : aps.entrySet()) {
				if(entry.getKey() == "badge"){
					builder.append("\"" + entry.getKey() + "\"").append(":").append("" + entry.getValue()).append(",");
				}else{
					builder.append("\"" + entry.getKey() + "\"").append(":").append("\"" + entry.getValue()).append("\",");
				}
			}
			builder.delete(builder.length() - 1, builder.length());
			builder.append("}");
		}
		if (builder.charAt(builder.length() - 1) == ' ') {
			builder.delete(builder.length() - 2, builder.length());
		}
		
		if(!parameter.isEmpty()){
			for (Map.Entry<String, String> entry : parameter.entrySet()) {
				builder.append(",\"" + entry.getKey() + "\"").append(":").append("\"" + entry.getValue()).append("\"");
			}
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
}
