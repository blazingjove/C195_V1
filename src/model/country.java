package model;

public class country {
    private int countryID;
    private String countryName;

    public country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**@return countryID*/
    public int getCountryID() { return countryID;}

    /**@return countryName */
    public String getCountryName() { return countryName;}

    /**changes country ID manually to input*/
    public void setCountryID(int countryID) { this.countryID = countryID;}

    /**changes country name manually to input*/
    public void setCountryName(String countryName) { this.countryName = countryName;}

}
