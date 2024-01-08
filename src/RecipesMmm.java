import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RecipesMmm {
    public static void main(String[] args) {
        System.out.println(randomRecipeSuggester());
    }

    public static String randomRecipeSuggesterWithConstraint(String ingredient){
        try{
            String recipeQuery = "SELECT * FROM recipeIngredients WHERE aliased_ingredient_name LIKE '%" + ingredient + "%' ORDER BY RAND() LIMIT 1;";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RecipesDB", "root", "swag"); //À comprendre
            Statement statement = connection.createStatement(); //À comprendre
            ResultSet resultSet = statement.executeQuery(recipeQuery);
            resultSet.next(); //Bring to first row
            return recipeSuggester(Integer.parseInt(resultSet.getString(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
    public static String randomRecipeSuggester() {
        int randomID = (int)(Math.random() * 45772) + 1;
        return recipeSuggester(randomID);
    }
    public static String recipeSuggester(int id) {
        String recipe = "";
        try{
            String recipeQuery = "SELECT * FROM recipes WHERE recipe_id = "+ id +";";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RecipesDB", "root", "swag"); //À comprendre
            Statement statement = connection.createStatement(); //À comprendre
            ResultSet resultSet = statement.executeQuery(recipeQuery); //À comprendre -- retourne le résultat

        resultSet.next(); //Bring to first row
        for (int i = 1; i <= 4; i++) {
            recipe += resultSet.getString(i) + "-";
        }
        //System.out.println(recipe);

        String recipeIngredientQuery = "SELECT * FROM recipeIngredients WHERE recipe_id = "+ resultSet.getString(1);
        resultSet = statement.executeQuery(recipeIngredientQuery);
        while (resultSet.next()) { //Checks if there is next result
            recipe += "\n";
            for (int i = 1; i <= 4; i++) {
                recipe += resultSet.getString(i) + ";";
            }
        }
        //System.out.println(recipe);

    } catch (Exception e) {
        e.printStackTrace();
    }
        return recipe;
    }

    public static void recipe_Ingredients_CSVToSQLTable() {
        String path = "C:\\Users\\tokat\\IdeaProjects\\testJavaAndSQL\\src\\04_Recipe-Ingredients_Aliases.csv";
        String line = "";

        try {

            BufferedReader br = new BufferedReader(new FileReader(path));
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RecipesDB", "root", "swag");
            Statement statement = connection.createStatement(); //À comprendre

            int count = 0;
            br.readLine(); //lire/éliminer la première ligne qui représente les titres des colonnes
            while((line = br.readLine()) != null) { //Ajouter chaque ligne du CSV au tableau "recipes"
                //Exception avec "," gérée en enlevant la virgule dans les " "
                if (line.indexOf('"') != -1) { //TODO Need to do tests in future to implement features
                    String[] tempStringTab = line.split("\"");
                    //Pour entre les "
                    for (int i = 1; i < tempStringTab.length - 1; i++) {
                        tempStringTab[i] = tempStringTab[i].replace(",", "");
                    }
                    line = String.join("\"", tempStringTab);
                }
                //Exception avec "'" gérée en ajoutant "'" additionnel à "'"
                if (line.indexOf("'") != -1) {
                    String[] tempStringTab = line.split("\'");
                    line = String.join("''", tempStringTab);
                }
                if (line.indexOf("\\") != -1) {
                    line = line.replace("\\", "");
                }

                String[] values = line.split(",");
                System.out.println(values[0] + "-" + values[1] + "-" + values[2] + "-" + values[3]); //Debugging to check si c'est traité
                String query = "INSERT INTO recipeIngredients VALUES (" + values[0] + ", '" + values[1] + "', '" + values[2] + "', '" + values[3] + "');";

                // Execute the query
                count += statement.executeUpdate(query); //TODO Tentative de compter le nb de lignes exécutées
            }

            System.out.println("Count (lignes affectées) = " + count);

            // Closing the connection as per the
            // requirement with connection is completed
            connection.close();

        } catch (Exception e) { //À comprendre
            e.printStackTrace();
        }
    }

    public static void recipe_Details_CSVToSQLTable() {
        String path = "C:\\Users\\tokat\\IdeaProjects\\testJavaAndSQL\\src\\01_Recipe_Details.csv";
        String line = "";

        try {

            BufferedReader br = new BufferedReader(new FileReader(path));
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RecipesDB", "root", "swag");
            Statement statement = connection.createStatement(); //À comprendre

            int count = 0;
            br.readLine(); //lire/éliminer la première ligne qui représente les titres des colonnes
            while((line = br.readLine()) != null) { //Ajouter chaque ligne du CSV au tableau "recipes"
                //Exception avec "," gérée en enlevant la virgule dans les " "
                if (line.indexOf('"') != -1) { //TODO Need to do tests in future to implement features
                    String[] tempStringTab = line.split("\"");
                    tempStringTab[1] = tempStringTab[1].replace(",", "");
                    line = String.join("\"", tempStringTab);
                }
                //Exception avec "'" gérée en ajoutant "'" additionnel à "'"
                if (line.indexOf("'") != -1) {
                    String[] tempStringTab = line.split("\'");
                    line = String.join("''", tempStringTab);
                }
                String[] values = line.split(",");
                System.out.println(values[0] + " " + values[1] + " " + values[2] + " " + values[3]); //Debugging to check si c'est traité
                String query = "INSERT INTO recipes VALUES (" + values[0] + ", '" + values[1] + "', '" + values[2] + "', '" + values[3] + "');";

                // Execute the query
                count += statement.executeUpdate(query); //TODO Tentative de compter le nb de lignes exécutées
            }

            System.out.println("Count (lignes affectées) = " + count);

            // Closing the connection as per the
            // requirement with connection is completed
            connection.close();

        } catch (Exception e) { //À comprendre
            e.printStackTrace();
        }
    }
}
