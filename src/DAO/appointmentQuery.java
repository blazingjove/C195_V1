package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import static main.JDBC.connection;


public class appointmentQuery{

    /**Observablelist for all appointments in database.
     * @throws SQLException error catching
     * @return appointmentsObservableList
     */
    public static ObservableList<appointments> getAllAppointments() throws SQLException {
        ObservableList<appointments> appointmentsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments";
        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");
            //LocalDateTime start = convertTimeDateLocal(rs.getTimestamp("Start").toLocalDateTime());
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            //LocalDateTime end = convertTimeDateLocal(rs.getTimestamp("End").toLocalDateTime());
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            appointments appointment = new appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, start, end, customerID, userID, contactID);
            appointmentsObservableList.add(appointment);
        }

        return appointmentsObservableList;
    }

    /**Method that deletes appointment based on appointment ID.
     * @param customer customer class
     * @return result
     * @throws SQLException error catching
     */
    public static int deleteAppointment(int customer) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, customer);
        int result = ps.executeUpdate();
        ps.close();

        return result;
    }

    /**Method that filters appointments to display current month only.*/
    public static ObservableList<appointments> getAppointmentsByCurrentMonth() throws SQLException {
        ObservableList<appointments> appointmentsByMonth = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        ObservableList<appointments> allAppointments = getAllAppointments();

        for (appointments appt : allAppointments) {
            if (appt.getStart().getMonth() == now.getMonth() && appt.getStart().getYear() == now.getYear()) {
                appointmentsByMonth.add(appt);
            }
        }

        return appointmentsByMonth;
    }
    /**Method that filters appointments to display current week only.*/
    public static ObservableList<appointments> getAppointmentsByCurrentWeek() throws SQLException {
        ObservableList<appointments> appointmentsByWeek = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        ObservableList<appointments> allAppointments = getAllAppointments();

        for (appointments appt : allAppointments) {
            LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1); // Start of the week (Monday)
            LocalDateTime endOfWeek = startOfWeek.plusDays(6); // End of the week (Sunday)

            if (!appt.getStart().isBefore(startOfWeek) && !appt.getStart().isAfter(endOfWeek)) {
                appointmentsByWeek.add(appt);
            }
        }

        return appointmentsByWeek;
    }

    public static boolean isCustomerInAppointments(Integer customerID) {
        try {
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**appointmentIDNext method parses the database for the highest appointment ID found and adds 1 and uses that ID for the next appointments
     * to be created.
     * @return 1 the appointment ID used if appointments table is empty.*/
    public static int appointmentIDNext() {
        try {
            String sql = "SELECT MAX(Appointment_ID) AS MaxID FROM appointments";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Default to 1 if there are no existing appointments
    }

}
