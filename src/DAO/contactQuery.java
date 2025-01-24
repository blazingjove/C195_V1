package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.contacts;

import main.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class contactQuery {

    public static ObservableList<contacts> getContacts() {
        ObservableList<contacts> contactsObservableList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM contacts";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");
                contacts contact = new contacts(contactID, contactName, email);
                contactsObservableList.add(contact);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return contactsObservableList;
    }
}
