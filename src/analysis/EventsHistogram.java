package analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import basic.ComparablePair;
import basic.Event;
import basic.EventManager;
import basic.Pair;

/**
 * Get the histogram of each event type from the dataset.
 * @author yjian004
 *
 */
public class EventsHistogram {
	private String datasetFile;
	private EventManager eventManager;
	private List<Event> eventSequence;
	private Set<Event> eventSet;
	private HistogramDataset histogram;
	private List<Pair<Event, HistogramDataset>> listHistogram;
	
	public EventsHistogram(String filepath){
		datasetFile = filepath;
		eventManager = new EventManager();
		eventSet = null;
		eventSequence = null;
		histogram = new HistogramDataset();
		listHistogram = new ArrayList<Pair<Event, HistogramDataset>>();
	}
	
	public HistogramDataset generateHistogramByEvent(Event event) throws IOException{
		boolean firstAppear = true;
		int previousLocation = 0;
		List<Double> intervalList = new ArrayList<Double>();
		for(int i = 0; i < eventSequence.size(); ++i){
			if(event.equals(eventSequence.get(i))){
				if(firstAppear == true){
					previousLocation = i;
					firstAppear = false;
				}
				else{
					double interval = i - previousLocation;
					intervalList.add(interval);
				}
			}
		}
		HistogramDataset dataset = new HistogramDataset();
		Double[] values = new Double[1];
		intervalList.toArray(values);
		double[] dvalues = new double[values.length];
		for(int i = 0; i < dvalues.length; ++i){
			dvalues[i] = values[i];
		}
		if(dvalues.length != 0){
			dataset.addSeries(event.getEvent(), dvalues, eventSequence.size());
			listHistogram.add(new Pair<Event, HistogramDataset>(event, dataset));
		}
		return dataset;
	}
	
	public void generateHistograms() throws IOException{
		//	initialization
		eventManager.read(datasetFile);
		eventSet = eventManager.getEventSet();
		int distinctEventNumber = eventSet.size();
		eventSequence = eventManager.getSequence();
		//	first of pair denotes the interval, second of pair denotes the frequency
		List<Map<Integer, Integer>> eventFrequency = new ArrayList();
		
		Iterator<Event> itr = eventSet.iterator();
		while(itr.hasNext()){
			Event targetEvent = itr.next();
			Map<Integer, Integer> freqMap = new HashMap<Integer, Integer>();
			int previousLocation = 0;
			boolean firstAppreance = true;
			for(int i = 0; i < eventSequence.size(); ++i){
				Event event = eventSequence.get(i);
				if(event.equals(targetEvent)){
					if(firstAppreance == true){
						previousLocation = i;
						firstAppreance = false;
						continue;
					}
					else{
						int interval = i - previousLocation;
						Integer freq = freqMap.get(interval);
						if(freq == null){
							freqMap.put(interval, 1);
						}
						else{
							freqMap.put(interval, freq + 1);
						}
						previousLocation = i;
					}
				}
			}
			eventFrequency.add(freqMap);
		}
		
		int nonEmptyEvent = 0;
		for(Map<Integer, Integer> map : eventFrequency){
			if(map.isEmpty() != true){
				++nonEmptyEvent;
			}
		}
		System.out.println("Non empty event:" + nonEmptyEvent);
		
		itr = eventSet.iterator();
		
		//	put result into histogramDataset
		int count = 0;
		for(Map<Integer, Integer> freqMap : eventFrequency){
			++count;
			List<ComparablePair<Integer, Integer>> listIntervalFreq = new ArrayList<ComparablePair<Integer, Integer>>();
			for(Map.Entry<Integer, Integer> entry : freqMap.entrySet()){
				listIntervalFreq.add(new ComparablePair<Integer, Integer>(entry));
				if(count == 1)
					System.out.println(entry.getKey() + ":" + entry.getValue());
			}
			
			Event event = itr.next();
			if(listIntervalFreq.isEmpty() == false){
				ComparablePair<Integer, Integer> maxPair = Collections.max(listIntervalFreq);
				int maxInterval = maxPair.getFirst();
				if(count == 1)
					System.out.println(maxInterval);
				
				double[] values = new double[maxInterval + 2];	//	0 with frequency 0
				Arrays.fill(values, 0);
				
				for(ComparablePair<Integer, Integer> pair : listIntervalFreq){
					values[pair.getFirst()] = pair.getSecond();
				}
				if(count == 1)
				for(int i = 0; i < values.length; ++i){
					System.out.println("values[" + i + "]=" + values[i]);
				}
				
				histogram.addSeries(new String("Event " + event.getEvent()), values, listIntervalFreq.size());
				HistogramDataset h = new HistogramDataset();
				h.addSeries(new String("Event " + event.getEvent()), values, values.length);
				listHistogram.add(new Pair<Event, HistogramDataset>(event, h));
			}
		}
		
	}
	
	public HistogramDataset getHistogramDataset(){
		return histogram;
	}
	
	public HistogramDataset getHistogramDatasetByIndex(int index){
		return listHistogram.get(index).getSecond();
	}
	
	public Set<Event> getEventSet(){
		return eventManager.getEventSet();
	}
	
	public static void main(String[] args) throws IOException{
		EventsHistogram histogram = new EventsHistogram("c:/dataset/event/short.txt");
		Set<Event> set = histogram.getEventSet();
		Iterator<Event> itr = set.iterator();
		System.out.println(set.size());
		Event event = null;
		for(int i = 0; i < 2; ++i){
			event = itr.next();
		}
		HistogramDataset hh = histogram.generateHistogramByEvent(event);
		
		HistogramDataset h = new HistogramDataset();
		h.addSeries("Event A", new double[]{2.00, 3.00, 4.00, 3.00}, 4);
		
		JFreeChart chart = ChartFactory.createHistogram("Histogram", "interval", "frequency", hh, PlotOrientation.VERTICAL, true, false, false);
		ChartFrame cf = new ChartFrame("Historgam", chart);
		cf.pack();
		cf.setSize(500, 300);
		cf.setVisible(true);
		
	}
}
