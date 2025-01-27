package DAO;

import model.users;
import main.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**provides functionality with the users*/
public class userValidation extends users {

    public userValidation(int userID, String userName, String userPassword) {
        super(userID, userName, userPassword);
    }

    /**
     * method used to validate username and password
     * @param username username entered in field
     * @param password password entered in field
     * @return used to define if valid username and password are used by checking for user id greater or lower than 0.
     */
    public static int validateUser(String username, String password) {
        try {
            String sqlQuery = "SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("User validation successful");
                return resultSet.getInt("User_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User validation failed");
        return -1;
    }

}
