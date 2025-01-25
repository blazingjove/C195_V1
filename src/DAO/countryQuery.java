package DAO;

import model.country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import main.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class countryQuery extends country {

    public countryQuery(int countryID, String countryName) {
        super(countryID, countryName);
    }

    public static ObservableList<country> getAllCountries() throws SQLException{
        ObservableList<country> countryList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Country_ID, Country FROM countries";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
             ResultSet resultSet = ps.executeQuery();) {

            while (resultSet.next()) {
                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                countryList.add(new country(countryID, countryName));
            }
        }
        return countryList;
    }
}
