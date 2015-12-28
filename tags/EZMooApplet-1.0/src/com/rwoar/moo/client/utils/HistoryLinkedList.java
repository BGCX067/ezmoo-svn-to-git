package com.rwoar.moo.client.utils;

import java.util.LinkedList;

public class HistoryLinkedList<E> extends LinkedList<E> {

	private static final long serialVersionUID = -8604614898036177918L;
	private int position = -1;
	private int size_limit; //arbitrarily chose 1000 as default size limit
	
	public HistoryLinkedList(int sl){
		super();
		this.size_limit = sl;
	}
		
	public E next(){
		if (this.position >= this.size()-1)
			this.position = this.size()-1;
		else
			this.position = this.position+1;
		
		return this.get(position);
	}
	
	public E previous(){
		if (this.position <= 0){
			this.position = -1;
			return null;
		} else {
			this.position = this.position-1;	
		}
		
		return this.get(position);
	}
	
	public void addFirst(E obj){
		if (this.size() >= size_limit){
			this.removeLast();
		}
		super.addFirst(obj);
		this.position = -1;
	}

}
