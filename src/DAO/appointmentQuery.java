package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
            LocalDateTime start = convertTimeDateLocal(rs.getTimestamp("Start").toLocalDateTime());
            //LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = convertTimeDateLocal(rs.getTimestamp("End").toLocalDateTime());
            //LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            appointments appointment = new appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, start, end, customerID, userID, contactID);
            appointmentsObservableList.add(appointment);
        }

        return appointmentsObservableList;
    }

    /**converts the time to local time from UTC
     * @param start the time as UTC
     * @return the time as local time*/
    private static LocalDateTime convertTimeDateLocal(LocalDateTime start) {
        return ZonedDateTime.of(start, java.time.ZoneId.of("UTC"))
                .withZoneSameInstant(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
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

    /**Method that filters appointments to display current month only.
     * @return appointmentsByMonth returns list of appointments that are that month*/
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
    /**Method that filters appointments to display current week only.
     * @return appointmentsByWeek only appointments that are from that week*/
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

    /**IsCustomerInAppointments checks if there are appointments associated with the input customer ID.
     * @param customerID customer_ID from sql server as INT
     * @return Boolean returns true or false*/
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

    public static boolean createAppointment(int appointmentID, String title, String description, String location, String type, int customerID, int userID, int contactID) {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // You would replace these date placeholders with actual LocalDateTime inputs when integrating
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = LocalDateTime.now().plusHours(1);

            ps.setInt(1, appointmentID);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, location);
            ps.setString(5, type);
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(start));
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(end));
            ps.setInt(8, customerID);
            ps.setInt(9, userID);
            ps.setInt(10, contactID);

            int affectedRows = ps.executeUpdate(); // Execute the query
            return affectedRows > 0; // Return true if rows were added successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if insertion failed
    }

    /**parses database for unique types among appointments
     * @return uniqueTypes a list of types found in th appointment table, not case sensative*/
    public static ObservableList<String> getUniqueAppointmentTypes() {
        ObservableList<String> uniqueTypes = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Type FROM appointments";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                uniqueTypes.add(rs.getString("Type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uniqueTypes;
    }

    /**parses the appointment database object for appointments with specific month and type
     * @param selectedType the selected type selected in the reports tab
     * @param selectedMonth the selected month selected in the reports tab
     * @return Int the number of appointments that meet both parameters*/
    public static int getAppointmentCountByMonthAndType(String selectedMonth, String selectedType) {
        String sql = "SELECT COUNT(*) AS Count FROM appointments WHERE MONTHNAME(Start) = ? AND Type = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, selectedMonth);
            ps.setString(2, selectedType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if no matches are found or an error occurs
    }

    /**get appointments associated with the given contact ID
     * @param contactID the given contactID
     * @return appointmentsByContact list of appointments that have the given contactID in them*/
    public static ObservableList<appointments> getAppointmentsByContactID(int contactID) {
        ObservableList<appointments> appointmentsByContact = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                appointments appointment = new appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, start, end, customerID, userID, contactID);
                appointmentsByContact.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsByContact;
    }

    /**gets the appointment that will occur the soonest in the future
     * @return appointment returns appointments object*/
    public static appointments getNearestAppointment() {
        String sql = "SELECT * FROM appointments WHERE Start > ? ORDER BY Start ASC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                return new appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, start, end, customerID, userID, contactID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no future appointment is found or on error
    }

    /**when logging in it returns an appointment that is nearest for the user
     * @return appointments an appointments that is withing 15 start from the user login time*/
    public static appointments getUpcomingAppointment(int userId, ZonedDateTime currentTime) {
        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start > ? AND Start <= ? ORDER BY Start ASC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(currentTime.toLocalDateTime()));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(currentTime.toLocalDateTime().plusMinutes(15)));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int contactID = rs.getInt("Contact_ID");
                return new appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, start, end, customerID, userId, contactID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no upcoming appointment is found or on error
    }
}
