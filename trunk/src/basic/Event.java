package basic;

import java.util.Calendar;

/**
 * 
 * @author Yexi Jiang <http://sites.google.com/site/yexijiang/>
 *type	date	time	source	category	event	user	computer
 */
public class Event implements Comparable<Event>{
	private String type;
	private Calendar date;
	private String source;
	private String category;
	private int event;
	private String user;
	private String computer;
	
	private int count;
	
	private static int i = 0;
	
	public Event(String type, Calendar date, String source, String category,
			int event, String user, String computer) {
		super();
		this.type = type;
		this.date = date;
		this.source = source;
		this.category = category;
		this.event = event;
		this.user = user;
		this.computer = computer;
		this.count = 0;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getEvent() {
		return event;
	}
	
	public void setEvent(int event) {
		this.event = event;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getComputer() {
		return computer;
	}
	
	public void setComputer(String computer) {
		this.computer = computer;
	}
	
	public int getCount(){
		return count;
	}
	
	public void increaseCount(){
		count++;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Event){
			Event event = (Event)obj;
			if(event.getEvent() == this.event){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		return event;
	}
	
	public int compareTo(Event other){
		if(this.count < other.getCount()){
			return -1;
		}
		else if(this.count == other.getCount()){
			return 0;
		}
		else{
			return 1;
		}
	}
	
}
