package model;

public class users {

    public int userID;
    public String userName;
    public String userPassword;

    public users() {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /** Primary Key
     * @return userID
     * int(10)
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return userName
     * varchar(50) (unique)
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return userPassword
     * text
     */
    public String getUserPassword() {
        return userPassword;
    }
}
