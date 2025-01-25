package model;

public class firstLevelDivision {
    private int divisionID;
    private String divisionName;
    private int countryID;

    public firstLevelDivision(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    public int getDivisionID() {return divisionID;}

    public String getDivisionName() {return divisionName;}

    public int getCountryID() {return countryID;}

}
