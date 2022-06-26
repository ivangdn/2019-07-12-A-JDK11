package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	private List<Food> foods;
	
	private double durataSimulazione;
	private int numCibiPreparati;
	
	public Model() {
		this.dao = new FoodDao();
	}
	
	public void creaGrafo(int porzioni) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<>();
		dao.listAllFoods(idMap);
		
		this.foods = dao.getVertici(porzioni, idMap);
		Graphs.addAllVertices(this.grafo, this.foods);
		
		for(Adiacenza a : dao.getAdiacenze(idMap)) {
			if(this.grafo.vertexSet().contains(a.getFood1()) && this.grafo.vertexSet().contains(a.getFood2())) {
				Graphs.addEdge(this.grafo, a.getFood1(), a.getFood2(), a.getPeso());
			}
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Food> getVertici() {
		Collections.sort(this.foods, new Comparator<Food>() {

			@Override
			public int compare(Food f1, Food f2) {
				return f1.getDisplay_name().compareTo(f2.getDisplay_name());
			}
			
		});
		return this.foods;
	}
	
	public List<FoodCalorie> getFoodsCalorieMax(Food food) {
		if(this.grafo.degreeOf(food)==0)
			return null;
		
		List<FoodCalorie> foodsCalorie = new ArrayList<>();
		for(Food f : Graphs.neighborListOf(grafo, food)) {
			foodsCalorie.add(new FoodCalorie(f, this.grafo.getEdgeWeight(this.grafo.getEdge(f, food))));
		}
		
		Collections.sort(foodsCalorie);
		return foodsCalorie;
	}
	
	public void simula(int K, Food startFood) {
		Simulator sim = new Simulator(this);
		sim.init(K, startFood);
		sim.run();
		this.durataSimulazione = sim.getDurata();
		this.numCibiPreparati = sim.getNumCibiPreparati();
	}
	
	public double getDurata() {
		return this.durataSimulazione;
	}
	
	public int getNumCibiPreparati() {
		return this.numCibiPreparati;
	}

}
