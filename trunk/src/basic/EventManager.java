package basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mange the event, containing the information about how many event in total
 * and how many distinct event in total.
 * @author Yexi Jiang <http://sites.google.com/site/yexijiang/>
 *
 */
public class EventManager {
	private List<Event> eventSequence;	//	event sequence
	
	private Set<Event> eventSet;
	
	public EventManager(){
		eventSequence = new ArrayList<Event>();
		eventSet = new HashSet<Event>();
	}
	
	public void read(String filepath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line;
		while((line = reader.readLine()) != null){
			if(line.length() == 0){
				continue;
			}
			String[] tokens = line.split("\\t");
			String[] date = tokens[1].split("-");
			String[] time = tokens[2].split(":");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]),
						Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
			int eventNo = Integer.parseInt(tokens[5]);
			Event event = new Event(tokens[0], calendar, tokens[3], tokens[4], eventNo, tokens[6], tokens[7]);
			addEvent(event);
		}
	}
	
	public void addEvent(Event event){
		eventSequence.add(event);
		eventSet.add(event);
	}
	
	public int distinctEventSize(){
		return eventSet.size();
	}
	
	public int length(){
		return eventSequence.size();
	}
	
	public List<Event> getSequence(){
		return eventSequence;
	}
	
	public Set<Event> getEventSet(){
		return eventSet;
	}
	
}
