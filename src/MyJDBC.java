import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyJDBC {
    public static void main(String[] args) {

        try {
            int id = 42349;

            String query = "INSERT INTO EngineeringStudents VALUES ("+ id +", 'ATE', 'HereComes', 'HurricaneBch', 2024, 1);";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/University", "root", "swag"); //À comprendre
            Statement statement = connection.createStatement(); //À comprendre
            ResultSet resultSet = statement.executeQuery(query); //À comprendre -- retourne le résultat

            while (resultSet.next()) { //Checks if there is next result
                String UniversityData = "";
                for (int i = 1; i <= 6; i++) {
                    UniversityData += resultSet.getString(i) + "-";
                }
                System.out.println(UniversityData);
            }

            // Execute the query
            int count = statement.executeUpdate(query);
            System.out.println(
                    "number of rows affected by this query= "
                            + count);

            // Closing the connection as per the
            // requirement with connection is completed
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
