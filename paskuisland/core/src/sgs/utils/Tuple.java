package sgs.utils;

public class Tuple<T, E> {
	
	protected T left;
	protected E right;
	
	
	public Tuple(T a, E b) {
		left = a;
		right = b;
	}
	
	public T getLeft() {
		
		return left;
	}
     public E getRight() {
		
		return right;

    }
     public void setLeft(T a) {
    	 
    	 left = a;
    	 
     }
     public void setRight(E b) {
    	 
    	 right = b;
     }
}
