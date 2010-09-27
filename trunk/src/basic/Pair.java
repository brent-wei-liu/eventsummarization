package basic;

public class Pair<T,E> {
	private T first;
	private E second;
	
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
		if(obj instanceof Pair<?,?>){
			Pair<T,E> pair = (Pair<T,E>)obj;
			if(pair.getFirst().equals(first) && pair.getSecond().equals(second)){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		return first.hashCode() + second.hashCode();
	}
}
