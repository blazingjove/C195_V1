package model;

import java.time.LocalDateTime;

/**Appointments class used to define components and return specific components through methods below*/
public class appointments {
    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private LocalDateTime start;
    private LocalDateTime end;
    public int customerID;
    public int userID;
    public int contactID;

    public appointments(int appointmentID, String appointmentTitle, String appointmentDescription,
                        String appointmentLocation, String appointmentType, LocalDateTime start, LocalDateTime end, int customerID,
                        int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /** Primary Key
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**@return appointmentTitle
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**@return appointmentDescription
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**@return appointmentLocation
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**@return appointmentType
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**@return start
     */
    public LocalDateTime getStart() {
        System.out.println("start " + start);
        return start;
    }

    /**@return end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /** Foreign Key
     * @return customerID
     */
    public int getCustomerID () {
        return customerID;
    }

    /** foreign Key
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /** Foreign Key
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }
}
