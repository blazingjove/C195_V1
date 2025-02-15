package model;

/**contacts class used to define and retrieve information specific to contacts class*/
public class contacts {
    public int contactID;
    public String contactName;
    public String contactEmail;

    public contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**Primary Key
     * @return contactID
     * int(10)
     */
    public int getID() {
        return contactID;
    }

    /**
     * @return contactName
     * varchar(50)
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return contactEmail
     * varchar(50)
     */
    public String getContactEmail() {
        return contactEmail;
    }
}