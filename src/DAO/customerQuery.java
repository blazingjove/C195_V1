package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.JDBC;
import model.customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**customerQuery gets all customers along from pulling specific information from the customer object*/
public class customerQuery {

    /**
     * Fetches all customers from the database.
     * @return ObservableList of customers containing all customer records
     */
    public static ObservableList<customers> getAllCustomers() throws SQLException {
        ObservableList<customers> customersObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet ds = ps.executeQuery();
        while (ds.next()){
            int customerID = ds.getInt("Customer_ID");
            String customerName = ds.getString("Customer_Name");
            String customerAddress = ds.getString("Address");
            String customerPostalCode = ds.getString("Postal_Code");
            String customerPhone = ds.getString("Phone");
            int customerDivisionID = ds.getInt("Division_ID");
            String customerDivisionName = firstLevelDivisionQuery.getDivisionNameByDivisionID(customerDivisionID);

            customers customer = new customers(customerID, customerName, customerAddress, customerPostalCode, customerPhone, customerDivisionID,customerDivisionName);
            customersObservableList.add(customer);
        }
        return customersObservableList;
    }

    /**
     * Deletes a customer from the database with the specified customer ID.
     *
     * @param customerID the ID of the customer to delete
     */
    public static void deleteCustomer(Integer customerID) {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the customer name associated with the given customer ID.
     *
     * @param CustomerID the ID of the customer
     * @return the name of the customer as a String
     */
    public static String getCustomerByCustomerID(int CustomerID) {
        String customerName = null;
        String sqlQuery = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, CustomerID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                customerName = resultSet.getString("Customer_Name");
            }
        } catch (SQLException ex) {}
        return customerName;
    }
}
