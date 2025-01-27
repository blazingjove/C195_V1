package DAO;

import javafx.scene.control.TextField;
import model.firstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Provides functionality for interacting with first-level divisions in the database.
 * <p>
 * This class defines an object to represent first-level divisions and includes methods
 * for retrieving information from the database about divisions, countries, and related details.
 */
public class firstLevelDivisionQuery {
    private int divisionID;
    private String divisionName;
    public int countryID;

    /**
     * Initializes a new instance of the firstLevelDivisionQuery class, representing
     * a first-level geographic division with a specified unique ID, name, and associated country ID.
     *
     * @param divisionID   The unique identifier for the division.
     * @param divisionName The name of the division.
     * @param countryID    The unique identifier for the country associated with the division.
     */
    public firstLevelDivisionQuery(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * Retrieves a complete list of all first-level divisions from the database.
     * <p>
     * Executes a SQL query to fetch first-level division records from the `first_level_divisions` table
     * and returns them as an ObservableList of firstLevelDivision objects.
     *
     * @return An ObservableList containing all firstLevelDivision objects retrieved from the database.
     * @throws SQLException If an SQL error occurs while executing the query or accessing the database.
     */
    public static ObservableList<firstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<firstLevelDivision> firstLevelDivisionList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * from first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
        ResultSet ds = ps.executeQuery();

        while (ds.next()){
            int divisionID = ds.getInt("Division_ID");
            String divisionName = ds.getString("Division");
            int countryID = ds.getInt("Country_ID");

            firstLevelDivision firstLevelDivision = new firstLevelDivision(divisionID, divisionName, countryID);
            firstLevelDivisionList.add(firstLevelDivision);
        }

        return firstLevelDivisionList;
    }

    /**
     * Retrieves the name of the country associated with a specific division ID from the database.
     * <p>
     * Executes a SQL query to find the corresponding country name for the provided division ID
     * by joining the `countries` and `first_level_divisions` tables.
     *
     * @param divisionID The unique identifier of the division for which the country name is being retrieved.
     * @return A string representing the corresponding country name, or null if no matching record is found.
     * @throws SQLException If an SQL error occurs during query execution or database access.
     */
    public static String getCountryNameByDivisionID(int divisionID) throws SQLException {
        String countryName = null;
        String sqlQuery = "SELECT countries.Country FROM countries " +
                "JOIN first_level_divisions ON first_level_divisions.Country_ID = countries.Country_ID " +
                "WHERE first_level_divisions.Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
        ps.setInt(1, divisionID);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            countryName = resultSet.getString("Country");
        }
        return countryName;
    }

    /**
     * Retrieves the name of the division (e.g., state or province) associated with a given division ID from the database.
     * <p>
     * Executes a SQL query to fetch the division name corresponding to the provided division ID
     * from the `first_level_divisions` table.
     *
     * @param divisionID The unique identifier of the division whose name is being retrieved.
     * @return A string containing the division name associated with the specified division ID, or null if no match is found.
     * @throws SQLException If an SQL error occurs during query execution or database access.
     */
    public static String getDivisionNameByDivisionID(int divisionID) throws SQLException {
        String divisionName = null;
        String sqlQuery = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
        ps.setInt(1, divisionID);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            divisionName = resultSet.getString("Division");
        }
        return divisionName;
    }

//    public int getDivisionID(String divisionName) throws SQLException {
//        String sqlQuery = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
//        ps.setString(1, divisionName);
//        ResultSet resultSet = ps.executeQuery();
//
//        if (resultSet.next()) {
//            return resultSet.getInt("Division_ID");
//        } else {
//            throw new SQLException("No Division found with the provided name: " + divisionName);
//        }
//    }
}
