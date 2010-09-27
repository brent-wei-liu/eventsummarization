package kdd08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import basic.Event;
import basic.EventManager;

/**
 * The MDL code cost for each part.
 * @author Yexi Jiang <http://sites.google.com/site/yexijiang/>
 *
 */
public class CodeCost {
	private List<Event> eventSet;
	private List<Event> eventSequence;
	private int m;
	
	public CodeCost(EventManager manager){
		eventSequence = manager.getSequence();
		eventSet = manager.getEventSet();
		Collections.sort(eventSet);
		Collections.reverse(eventSet);
		m = eventSet.size();
	}
	
	public double optimalTL(){
		double cost = 0.0;
		cost = optimalTLHelper(1, eventSequence.size());
		return cost;
	}
	
	private double optimalTLHelper(int start, int end){
		double cost = 0.0;
		for(int j = start; j < end; ++j){
			double curcost = optimalTLHelper(start, j) + optimalLL(j + 1, end);
			if(cost == 0.0 || curcost < cost){
				cost = curcost;
			}
		}
		return cost;
	}
	
	private double optimalLL(int start, int end){
		double cost = 0.0;
		for(int l = start; l < end; ++l){
			double curcost = optimalLL(start, l) + costU(start, l + 1, end) + m * Math.log(m) / Math.log(2) + 2 * Math.log(m) / Math.log(2);
			if(cost == 0.0 || curcost < cost){
				cost = curcost;
			}
		}
		return cost;
	}
	
	private double costU(int start, int l, int end){
		double cost = 0.0;
		for(int i = l + 1; i < end - 1; ++i){
			Event event = eventSet.get(i);
			int n = nEvent(start, end, event);
			double p = 0.0;
			for(int j = l + 1; j <= end; ++j){
				p += nEvent(start, end, event);
			}
			p /= (end - start);
			cost -= n * Math.log(p) / Math.log(2);
			cost -= ((end - start) - n) * Math.log(1 - p) / Math.log(2);
		}
		return cost;
	}
	
	private int nEvent(int start, int end, Event event){
		int count = 0;
		for(int i = start; i < end - 1; ++i){
			if(eventSequence.get(i).equals(event)){
				++count;
			}
		}
		return count;
	}
}
