package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {
	
	//dati in ingresso
	private int K;
	private Food startFood;
//	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;
	
	//dati in uscita
	private double durata;
	private int numCibiPreparati;
	
	//modello del mondo
	private List<Food> cibiInPreparazione; //l'indice è la stazione (da 0 a K-1) e il valore è il cibo su quella stazione
	private List<Food> cibiPreparati; //cibi già preparati
	private int stazioniOccupate;
	private List<FoodCalorie> foodsCalorie;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	public Simulator(Model model) {
		this.model = model;
	}
	
	public void init(int K, Food startFood) {
		this.K = K;
		this.startFood = startFood;
		
		this.durata = 0;
		this.numCibiPreparati = 0;
		
		this.cibiInPreparazione = new ArrayList<>();
		for(int i=0; i<this.K; i++) {
			this.cibiInPreparazione.add(null);
		}
		this.cibiPreparati = new ArrayList<>();
		this.stazioniOccupate = 0;
		this.foodsCalorie = new ArrayList<>();
		
		this.queue = new PriorityQueue<>();
		List<FoodCalorie> vicini = this.model.getFoodsCalorieMax(this.startFood);
		if(vicini==null) {
			throw new IllegalArgumentException("Vertice di partenza isolato");
		}
		for(int i=0; i<this.K && i<vicini.size(); i++) {
			queue.add(new Event(0.0, EventType.INIZIO_PREPARAZIONE, i, vicini.get(i).getFood()));
			this.foodsCalorie.add(vicini.get(i));
		}
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			this.durata = e.getTime();
			ProcessEvent(e);
		}
	}

	private void ProcessEvent(Event e) {
		double time = e.getTime();
		Food f = e.getFood();
		int stazione = e.getStazione();
		EventType type = e.getType();
		
		List<FoodCalorie> vicini = this.model.getFoodsCalorieMax(f);
		
		if(this.stazioniOccupate!=0 || vicini!=null) {
			switch(type) {
			case INIZIO_PREPARAZIONE:
				this.cibiInPreparazione.set(stazione, f);
				this.stazioniOccupate++;
				FoodCalorie foodCal = this.foodsCalorie.get(stazione);
				queue.add(new Event(time+foodCal.getCalorie(), EventType.FINE_PREPARAZIONE, stazione, f)); //SBAGLIATO!!!
				break;
				
			case FINE_PREPARAZIONE:
				this.cibiPreparati.add(f);
				this.numCibiPreparati++;
				this.stazioniOccupate--;
				Food prossimo = null;
				for(FoodCalorie vicino : vicini) {
					if(!this.cibiPreparati.contains(vicino.getFood()) && !this.cibiInPreparazione.contains(vicino.getFood())) {
						prossimo = vicino.getFood();
						break;
					}
				}
				
				if(prossimo!=null) {
					queue.add(new Event(time, EventType.INIZIO_PREPARAZIONE, stazione, prossimo));
				}
				
				break;
			}
		}
	}
	
	public double getDurata() {
		return this.durata;
	}
	
	public int getNumCibiPreparati() {
		return this.numCibiPreparati;
	}

}
