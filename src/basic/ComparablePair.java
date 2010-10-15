package basic;

import java.util.Map;

public class ComparablePair<T extends Comparable,E extends Comparable> implements Comparable{
	private T first;
	private E second;
	
	private boolean compareByFirst = true;
	
	public ComparablePair(){
		
	}
	
	public ComparablePair(T first, E second){
		this.first = first;
		this.second = second;
	}
	
	public ComparablePair(Map.Entry<T, E> entry){
		first = entry.getKey();
		second = entry.getValue();
	}
	
	public T getFirst() {
		return first;
	}
	
	public void setFirst(T first) {
		this.first = first;
	}
	
	public E getSecond() {
		return second;
	}
	
	public void setSecond(E second) {
		this.second = second;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof ComparablePair<?,?>){
			ComparablePair<T,E> pair = (ComparablePair<T,E>)obj;
			if(pair.getFirst().equals(first) && pair.getSecond().equals(second)){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		return first.hashCode() + second.hashCode();
	}

	@Override
	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof ComparablePair){
			ComparablePair pair = (ComparablePair<T, E>)obj;
			if(compareByFirst == true){
				return first.compareTo(pair.getFirst());
			}
			else if(compareByFirst == false){
				return second.compareTo(pair.getSecond());
			}
		}
		
		return 0;
	}
	
	public void setComparator(boolean first){
		compareByFirst = first;
	}
}
