package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		INIZIO_PREPARAZIONE,
		FINE_PREPARAZIONE
	}
	
	private Double time;
	private EventType type;
	private int stazione;
	private Food food;
	
	public Event(Double time, EventType type, int stazione, Food food) {
		super();
		this.time = time;
		this.type = type;
		this.stazione = stazione;
		this.food = food;
	}

	public Double getTime() {
		return time;
	}

	public EventType getType() {
		return type;
	}

	public int getStazione() {
		return stazione;
	}

	public Food getFood() {
		return food;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

}
