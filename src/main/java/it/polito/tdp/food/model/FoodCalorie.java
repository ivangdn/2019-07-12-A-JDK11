package it.polito.tdp.food.model;

public class FoodCalorie implements Comparable<FoodCalorie>{
	
	Food food;
	Double calorie;
	
	public FoodCalorie(Food food, Double calorie) {
		super();
		this.food = food;
		this.calorie = calorie;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Double getCalorie() {
		return calorie;
	}

	public void setCalorie(Double calorie) {
		this.calorie = calorie;
	}

	@Override
	public int compareTo(FoodCalorie other) {
		return this.calorie.compareTo(other.calorie);
	}
	
}
