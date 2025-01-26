package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.JDBC;
import model.users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class userQuery {

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
}
