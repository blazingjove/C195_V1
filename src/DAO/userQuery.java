package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.JDBC;
import model.users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class userQuery {

    /**gets all users from the database
     * @return usersObservableList list of all users
     * @exception SQLException basic error handling*/
    public static ObservableList<users> getAllUsers() throws SQLException {
        ObservableList<users> usersObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet ds = ps.executeQuery();
        while (ds.next()){
            int userID = ds.getInt("User_ID");
            String userName = ds.getString("User_Name");
            String userPassword = ds.getString("Password");

            users user = new users(userID, userName, userPassword);
            usersObservableList.add(user);
        }
        return usersObservableList;
    }

    /**get username from given userID
     * @param userID int
     * @return userName string*/
    public static String getUserByUserID(int userID) {
        String userName = null;
        String sqlQuery = "SELECT User_Name FROM users WHERE User_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, userID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                userName = resultSet.getString("User_Name");
            }
        } catch (SQLException ex) {}
        return userName;
    }
}
