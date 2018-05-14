package payme.notification.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AosMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> data;
	
	//private final Set<String> tabSet;
	
	private final String lang;
	
	private final List<String> userIds;
	
	public AosMessage(String lang) {
		this.data = new LinkedHashMap<String, String>();
		//this.tabSet = new HashSet<String>();
		this.userIds = new ArrayList<String>();
		this.lang = lang;
	}

	public void addMessage(String message){
		data.put("message", message);
	}
	
	public void addSound(String sound){
		data.put("sound", sound);
	}
	
	public void addBadge(String badge){
		data.put("badge", badge);
	}
	
	public void addParameter(String key, String value){
		data.put(key, value);
	}
	
	/*public void addTabSet(String tabString){
		tabSet.add(tabString);
	}
	
	public Set<String> getTabSet() {
		return tabSet;
	}*/

	public void addUserId(String userId){
		userIds.add(userId);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{\"");
		if (!data.isEmpty()) {
			builder.append("data\":{");
			for (Map.Entry<String, String> entry : data.entrySet()) {
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
	
	/*public String getTabSetToString(){
		StringBuilder builder = new StringBuilder("");
		if (!tabSet.isEmpty()) {
			builder.append("{");
			Iterator<String> iterator = tabSet.iterator();
			while(iterator.hasNext()){
				String tab = iterator.next();
				builder.append(""+tab+",");
			}
			builder.delete(builder.length() - 1, builder.length());
			builder.append("}");
		}
		return builder.toString();
	}*/
	
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
