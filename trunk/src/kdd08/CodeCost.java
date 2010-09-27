package kdd08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import basic.Event;
import basic.EventManager;
import basic.Pair;

/**
 * The MDL code cost for each part.
 * @author Yexi Jiang <http://sites.google.com/site/yexijiang/>
 *
 */
public class CodeCost {
	private Set<Event> eventSet;
	private List<Event> eventSequence;
	private int m;
	
	private double[][] costTLMatrix;
	private double[][] costLLMatrix;
	
	public CodeCost(EventManager manager){
		eventSequence = manager.getSequence();
		eventSet = manager.getEventSet();
		m = eventSet.size();
		costTLMatrix = new double[eventSequence.size()][eventSequence.size()];
		costLLMatrix = new double[eventSequence.size()][eventSequence.size()];
		for(int i = 0; i < eventSequence.size(); ++i){
			Arrays.fill(costTLMatrix[i], -1.0);
			Arrays.fill(costLLMatrix[i], -1.0);
		}
	}
	
	public double optimalTL(){
		double cost = 0.0;
		cost = optimalTLHelper(0, eventSequence.size() - 1);
		
		System.out.println("Final TLMatrix:");
		for(int idx = 0; idx < eventSequence.size(); ++idx){
			for(int jdx = 0; jdx < eventSequence.size(); ++jdx){
				System.out.print(costTLMatrix[idx][jdx] + "\t");
			}
			System.out.println();
		}
		return cost;
	}
	
	private double optimalTLHelper(int start, int end){
		if(start == end){
			return 0.0;
		}
		double cost = 0.0;
		for(int j = start; j < end; ++j){
			System.out.println("Event:" + start + ":" + end);
			double curcost = 0.0;
			if(costTLMatrix[start][j] < 0.0){
				double subcost = optimalTLHelper(start, j);
				costTLMatrix[start][j] = subcost;
				
				System.out.println("TLMatrix:");
				for(int idx = 0; idx < eventSequence.size(); ++idx){
					for(int jdx = 0; jdx < eventSequence.size(); ++jdx){
						System.out.print(costTLMatrix[idx][jdx] + "\t");
					}
					System.out.println();
				}
			}
			cost += costTLMatrix[start][j];
			if(costLLMatrix[j + 1][end] < 0.0){
				double subcost = optimalLL(j + 1, end);
				costLLMatrix[j + 1][end] = subcost;
			}
			cost += costLLMatrix[j + 1][end];
			if(cost < 0.0 || curcost < cost){
				cost = curcost;
			}
		}
		
		return cost;
	}
	
	private double optimalLL(int start, int end){
		if(start == end){
			return 0.0;
		}
		double cost = 0.0;
		if(costLLMatrix[start][end]  < 0.0){
			double subcost = optimalLLHelper(start, end);
			costLLMatrix[start][end] = subcost;
		}
		cost = costLLMatrix[start][end];
		return cost;
	}
	
	private double optimalLLHelper(int start, int end){
		if(start == end){
			return 0.0;
		}
		System.out.println("LLMatrix:");
		for(int idx = 0; idx < eventSequence.size(); ++idx){
			for(int jdx = 0; jdx < eventSequence.size(); ++jdx){
				System.out.print(costLLMatrix[idx][jdx] + "\t");
			}
			System.out.println();
		}
		double cost = 0.0;
		for(int l = start; l < end; ++l){
			double curcost = (m + 2) * Math.log(m) / Math.log(2);
			if(costLLMatrix[start][l] < 0.0){
				double subcost = optimalLLHelper(start, l);
				costLLMatrix[start][l] = subcost;
				curcost += subcost;
			}
			cost += costU(l + 1, end);
			if(cost == 0.0 || curcost < cost){
				cost = curcost;
			}
		}

		return cost;
	}
	
	private double costU(int start, int end){
		if(start == end){
			return 0.0;
		}
		List<Event> eventList = getSubsetEventOrder(start, end);
		double cost = 0.0;
		for(Event event : eventList){
//			System.out.println("start:" + start + ":" + end + ",Size:" + eventList.size());
			int n = nEvent(start, end, event);
			double p = 0.0;
			for(Event ev : eventList){
				double nEvent = nEvent(start, end, ev);
				p += nEvent;
			}
			
			p /= (end - start);
			if(p != 0.0){
				cost -= n * Math.log(p) / Math.log(2);
				cost -= ((end - start) - n) * Math.log(p) / Math.log(2);
			}
			if(cost < 0.0){
				System.out.println("start:" + start + ", end:" + end + ", cost:" + cost + ", p:" + p);
				System.exit(1);
			}
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
	
	/**
	 * Get the ordered list according to Ii
	 * @param eventStart
	 * @param end
	 * @return
	 */
	private List<Event> getSubsetEventOrder(int eventStart, int eventEnd){
		List<Event> orderList = new ArrayList<Event>();
		for(int i = eventStart; i <= eventEnd; ++i){
			Event event = eventSequence.get(i);
			boolean found = false;
			for(int j = 0; j < orderList.size(); ++j){
				if(orderList.get(j).equals(event)){
					orderList.get(j).increaseCount();
					found = true;
					break;
				}
			}
			if(found == false){
				orderList.add(event);
			}
		}
		Collections.sort(orderList);
		Collections.reverse(orderList);
		
		return orderList;
	}
	
	
	
	
	
	
	public double greedyTL(){
		//	init boundary
		double cost = 0.0;
		List<Pair<Integer, Double>> listBoundary = new ArrayList<Pair<Integer, Double>>();
		for(int i = 1; i < eventSequence.size(); ++i){
			Pair<Integer, Double> pair = new Pair<Integer, Double>();
			pair.setFirst(i);
			double g = optimalLL(i - 1, i + 1 - 1) + Math.log(eventSequence.size()) / Math.log(2)
						- optimalLL(i - 1, i - 1) - Math.log(eventSequence.size()) / Math.log(2)
						- optimalLL(i, i + 1 - 1) - Math.log(eventSequence.size()) / Math.log(2);
			pair.setSecond(g);
		}
		
		
		
		return cost;
	}
}
