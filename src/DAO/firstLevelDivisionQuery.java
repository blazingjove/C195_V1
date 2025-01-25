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

    public int getDivisionID(){ return divisionID;}

    public String getDivisionName(){ return divisionName;}

    public int getCountryID(){ return countryID;}

}
