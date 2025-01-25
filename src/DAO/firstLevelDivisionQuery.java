package DAO;

import model.firstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class firstLevelDivisionQuery{
    private int divisionID;
    private String divisionName;
    public int countryID;

    public firstLevelDivisionQuery(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }
    
    public static ObservableList<firstLevelDivision> getAllFirstLevelDivisions() throws SQLException{
        ObservableList<firstLevelDivision> firstLevelDivisionList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Division_ID, Division FROM divisions WHERE Division_Level = 1";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
             ResultSet resultSet = ps.executeQuery();) {
            while (resultSet.next()){
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryID = resultSet.getInt("Country_ID");
            }
        }
        return firstLevelDivisionList;
    }

    public int getDivisionID(){ return divisionID;}

    public String getDivisionName(){ return divisionName;}

    public int getCountryID(){ return countryID;}

}
