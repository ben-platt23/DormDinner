import java.util.ArrayList;
/*
 * Author: Ben Platt
 * Date: 03/02/2022
 * This is the Recipe class of the Project 2 milestone. It's purpose is to create objects for each possible
 * recipe that is read in by the scanner. These objects should have attributes of cook time, recipe title and ingredients.
 * The ingredients are contained in an array list with each element being a different ingredient.
 */
public class Recipe {
	// Initialize  different attributes of a recipe
	String recipeTitle;
	String cookTime;
	ArrayList ingredients = new ArrayList();
	
	// constructor for the recipe
	public Recipe(String recipeTitle, String cookTime, ArrayList ingredients) {
		this.recipeTitle = recipeTitle;
		this.cookTime = cookTime;
		this.ingredients = ingredients;
	}
	
	// toString method
	public String toString() {
		return "Recipe [recipeTitle=" + recipeTitle + ", cookTime=" + cookTime + ", ingredients=" + ingredients + "]";
	}
	
	// Getters and setters
	// get and set title
	public String getRecipeTitle() {
		return recipeTitle;
	}

	public void setRecipeTitle(String recipeTitle) {
		this.recipeTitle = recipeTitle;
	}
	// get and set cook time
	public String getCookTime() {
		return cookTime;
	}

	public void setCookTime(String cookTime) {
		this.cookTime = cookTime;
	}
	// get and set ingredients
	public ArrayList getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList ingredients) {
		this.ingredients = ingredients;
	}
	
	
	
}
