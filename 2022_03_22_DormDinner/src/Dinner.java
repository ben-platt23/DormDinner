import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/*
 * Author: Ben Platt
 * Date: 03/22/2022
 * 
 * This program firstly reads in a list of ingredients that the user has and a list of possible recipes to be cooked.
 * 
 * For the first project milestone, it only read the ingredients as strings and created objects for each possible recipe 
 * with attributes of title, cook time and ingredients.
 * 
 * For the final project submission (THIS ONE) it figures out which recipes that the user can make based on the ingredients
 * that they have. If the user can't make any, it generates and prints a shopping list containing all the ingredients
 * in the recipe list that is not found in the ingredients list. If there is one possible recipe, it prints out all the details 
 * of that recipe. If there are multiple recipes, it prompts the user for the most time they can spend cooking. Then it 
 * removes the recipes that are greater than that maximum time. It then sorts the remaining contender 
 * recipes by time to cook (least to greatest) and then prints it out all the relevant details about them.
 * 
 *  For the convenience of testing, I've included three different csv file lists of ingredients. 
 *  "MultipleOptionsTest" has multiple recipes that can be made with the ingredients contained. 
 *  "Ingredients.csv" has one recipe that can be made with the contained ingredients
 *  "ShoppingList.csv" has NO recipes that can be made with the ingredients, and will therefore generate an appropriate shopping list
 */
public class Dinner {

	public static void main(String[] args) {
		// initialize file reader and various arraylists
		// file reader
		Scanner scan = null; 
		// read in
	    ArrayList Ingredients = new ArrayList();
	    ArrayList<Recipe> Recipes = new ArrayList();
	    // created based on what is read in
	    ArrayList<Recipe> PossibleRecipes = new ArrayList();
	    ArrayList ShoppingList = new ArrayList();
	   
	    // read in strings from ingredients csv
	    try{
		    scan = new Scanner(new FileReader("Ingredients.csv"));											 
		    while(scan.hasNext()){ 
		    	String ingredient = scan.nextLine();
			    Ingredients.add(ingredient); 
		    }
	    } catch (FileNotFoundException e) {
			System.out.println("File not found " + e);
		} finally{
		    if(scan != null){
			    scan.close();
		    }
	    }
	    
	    // create new scanner for recipesknown file
	    Scanner input = null;
	    
	    // read in and store recipe objects fromn recipesknown csv
	    try{
		    input = new Scanner(new FileReader("RecipesKnown.csv"));
		    while(input.hasNext()){ 
		    	String line = input.nextLine();
		    	// get indices of where the tabs are in the read line
		    	ArrayList<Integer> TabIndices = new ArrayList<Integer>();
		    	for(int i = 0; i < line.length(); i++) {
		    		if(line.charAt(i) == '\t') {
		    			TabIndices.add(i);
		    		}
		    	}
		    	// set title of recipe to a variable, to be added to the recipe object
		    	String Title = line.substring(0, TabIndices.get(0));
		    	// set cook time of recipe to a variable, to be added to the recipe object
		    	String time = line.substring(TabIndices.get(0)+1, TabIndices.get(1));
		    	// set ingredients in recipe to an arraylist, to be added to the recipe object
		    	ArrayList RecipeIngredients = new ArrayList();
		    	// Get all the ingredients in one string
		    	String AllIngredients = line.substring(TabIndices.get(1)+1);
		    	// Separate the ingredients string into it's component ingredients. 
		    	// Do this by looping through the big string and substringing where the commas are
		    	// Firstly I need to know where in the string that these commas are. I can add these positions to an arraylist
		    	ArrayList<Integer> CommaIndices = new ArrayList<Integer>();
		    	for(int i = 0; i < AllIngredients.length(); i++) {
		    		if(AllIngredients.charAt(i) == ',') {
		    			CommaIndices.add(i);
		    		}
		    	}
		    	// If there are no commas, no need for a substring, all the ingredients are present
		    	if(CommaIndices.size() == 0) {
		    		RecipeIngredients.add(AllIngredients);
		    	}
		    	// otherwise, the first substring will be from 0 to the first comma
		    	else {
		    		RecipeIngredients.add(AllIngredients.substring(0, CommaIndices.get(0)));
		    		// after that, need to go from CommaIndices(0) to CommaIndices(1). So loop from 0 to CommaIndices.size()
		    		for(int i = 1; i < CommaIndices.size(); i++) {
		    			RecipeIngredients.add(AllIngredients.substring(CommaIndices.get(i-1)+1, CommaIndices.get(i)));
		    		}
		    		// The last ingredient will be from the last CommaIndice element through the rest of the string
		    		RecipeIngredients.add(AllIngredients.substring(CommaIndices.get(CommaIndices.size()-1)+1));
		    	}
		    	// Now I have all the recipe attributes and I can create my object, add it to the list of objects and repeat.
		    	Recipe NewRecipe = new Recipe(Title, time, RecipeIngredients);
		    	Recipes.add(NewRecipe);
	    	}
		    	
	    } catch (FileNotFoundException e) {
			System.out.println("File not found " + e);
		} finally{
		    if(input != null){
			    input.close();
		    }
	    }
	    System.out.println("------------ BEGIN STUFF BEING READ IN ------------");
	    // print out read ingredients strings
	    System.out.println("The Ingredients You Have");
	    System.out.println(Ingredients);
	    System.out.println();
	    // print out recipes objects. Each object gets its own line
	    System.out.println("The Recipes You Gave");
	    for(int i = 0; i < Recipes.size(); i++) {
	    	System.out.println(Recipes.get(i));
	    }
	    System.out.println("------------ END STUFF BEING READ IN ------------");
	    System.out.println();
	    // Removes the default object that's at the top of the RecipesKnown.csv file 
	    // only does this if it's title is "Title" in case this program is run with a different/modified .csv file
	    Recipe defaultRecipe = Recipes.get(0);
	    String defaultTitle = defaultRecipe.getRecipeTitle();
	    if(defaultTitle.equals("Title")) {
	    	Recipes.remove(0);
	    }
		/*
		 * Prepare list of possible recipes Loop through the recipes array list and
		 * check if every element of each recipe can be found in the ingredients
		 * Outer loop = array list 
		 * inner loop = ingredients in array list
		 */
	    for(int i = 0; i < Recipes.size(); i++) {
	    	// Get the i'th object from the array list and then it's required ingredients
	    	Recipe CheckRecipe = Recipes.get(i);
	    	ArrayList CheckIngredients = CheckRecipe.getIngredients();
	    	// boolean that changes to false in the inner loop IF every required ingredient is not in the Ingredients (read in) array list
	    	Boolean isPossible = true;
	    	for(int j = 0; j < CheckIngredients.size(); j++) {
	    		if(!Ingredients.contains(CheckIngredients.get(j))) {
	    			isPossible = false;
	    			ShoppingList.add(CheckIngredients.get(j));
	    		}
	    	}
	    	if(isPossible == true) {
	    		PossibleRecipes.add(CheckRecipe);
	    	}
	    }

	    
	    // This next part is all the stuff that depends on what's in the PossibleRecipes ArrayList
	    // If no possible recipes :(
	    if(PossibleRecipes.size() == 0) {
	    	System.out.println("Oh no! You can't make any of the known recipes!");
	    	System.out.println("Fear not, I have prepared a shopping list of everything you need from the recipes Below");
	    	System.out.println("Needed ingredients:");
	    	System.out.println(ShoppingList);
	    }
	    // If only one possible recipe
	    else if(PossibleRecipes.size() == 1) {
	    	System.out.println("You're in luck, you can make ONE WHOLE RECIPE!");
	    	System.out.println("Here's everything you need to know about it:");
	    	System.out.println(PossibleRecipes);
	    }
	    // If more than one possible recipe
	    else {
	    	// Prompting user for max cook time and taking it in as int
	    	Scanner options = new Scanner(System.in);
	    	System.out.println("Well, look at you! You can make multiple recipes!");
	    	System.out.println("What's the most amount of time you're willing to spend cooking?");
	    	int MaxTime = options.nextInt();
	    	// since scanner is buggy with ints
	    	options.nextLine();
	    	// Adding everything to a separate options list, to be removed if MaxTime is less than the required time on the recipe
	    	ArrayList<Recipe> Options= new ArrayList();
	    	for(int i = 0; i < PossibleRecipes.size(); i++) {
	    		Options.add(PossibleRecipes.get(i));
	    	}
	    	// loop to check if the user's maximum time is less than any of the recipes cook time
	    	for(int i = 0; i < PossibleRecipes.size(); i++) {
	    		// Get the i'th object from the array list and then it's required cooktime, convert to an int
	    			Recipe CheckRecipe = PossibleRecipes.get(i);
			    	String stringTime = CheckRecipe.getCookTime();
			    	int numberTime = Integer.parseInt(stringTime);;
			    	// check if the required time is greater than the max time. Removes from options if it is.
			    	if(numberTime > MaxTime ) {
			    		Options.remove(PossibleRecipes.get(i));
			    	}
	    		}
	    	// Get the cook times, to be sorted recursively
	    	ArrayList<Integer> CooktimeOptions = new ArrayList(); 
	    	for(int i = 0; i < Options.size(); i++) {
	    		Recipe CheckRecipe = Options.get(i);
	    		String stringCooktime = CheckRecipe.getCookTime();
	    		int intCooktime = Integer.parseInt(stringCooktime);
	    		CooktimeOptions.add(intCooktime);
	    	}
	    	
	    	// sort the times using java library
	    	Collections.sort(CooktimeOptions);
	    	// convert the integers in the arraylist to strings and add to new arraylist
	    	ArrayList<String> StringCookTimeOptions = new ArrayList();
	    	for(int i = 0; i < CooktimeOptions.size(); i++) {
	    		String NewTime = String.valueOf(CooktimeOptions.get(i));
	    		StringCookTimeOptions.add(NewTime);
	    	}
	    	// add the recipes corresponding to each time from the possible recipes
	    	ArrayList<Recipe> SortedOptions = new ArrayList();
	    	for(int i = 0; i < StringCookTimeOptions.size(); i++) {
	    		String NewTime = StringCookTimeOptions.get(i);
	    		for(int j = 0; j < Options.size(); j++) {
	    			Recipe CheckRecipe = Options.get(j);
	    			String CheckCookTime = CheckRecipe.getCookTime();
	    			if(NewTime.equals(CheckCookTime)) {
	    				SortedOptions.add(CheckRecipe);
	    			}
	    		}
	    	}
	    	// Display new options
	    	if(SortedOptions.size() == 0) {
	    		System.out.println("Oh no, you don't have enough time to cook any of these recipes!");
	    	}
	    	else {
	    		System.out.println("Here are your options, sorted from least to most cook-time for your convenience!");
		    	System.out.println(SortedOptions);
	    	}
	    }
	}
}
