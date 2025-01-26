package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.JDBC;
import model.customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class customerQuery {

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
            String customerDivisionName = "holder";

            customers customer = new customers(customerID, customerName, customerAddress, customerPostalCode, customerPhone, customerDivisionID,customerDivisionName);
            customersObservableList.add(customer);
        }
        return customersObservableList;
    }

    public static void deleteCustomer(Integer customerID) {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
