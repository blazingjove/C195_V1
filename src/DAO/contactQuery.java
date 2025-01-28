package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.contacts;
import main.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The {@code contactQuery} class provides methods to interact with the contacts table in the database.
 * It includes functionality to retrieve contact details, such as contact IDs, names, and a list of all contacts.
 * These methods utilize SQL queries to fetch the required data from the database.
 */
public class contactQuery {

    /**parses database for all contact
     * @return contactsObservableList a list of all contacts in the database*/
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

    /**fetches contact name with given name
     * @param contactName the given contact name as a string
     * @return contactID the contact ID*/
    public static int getContactIDByName(String contactName) throws SQLException{
        int contactID = 0;
        String sqlQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery)) {
            ps.setString(1, contactName);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                contactID = resultSet.getInt("Contact_ID");
            }
        }
        return contactID;
    }

    /**gets the contact associated with contact ID
     * @param editAppointmentContactID the contactID
     * @return contactName a string of name*/
    public static String getContactByContactID(int editAppointmentContactID) {
        String contactName = null;
        String sqlQuery = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, editAppointmentContactID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                contactName = resultSet.getString("Contact_Name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return contactName;
    }

    /**gets all the contact names in the database
     * @return contactNamesList a list of all contact names*/
    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNamesList = FXCollections.observableArrayList();
        String sqlQuery = "SELECT Contact_Name FROM contacts";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                String contactName = resultSet.getString("Contact_Name");
                contactNamesList.add(contactName);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return contactNamesList;
    }

}
