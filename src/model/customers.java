package model;

public class customers {

    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private int customerDivisionID;
    private String divisionName;

    public customers(int customerID, String customerName, String customerAddress, String customerPostalCode,
                         String customerPhoneNumber, int customerDivisionID, String divisionName)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerDivisionID = customerDivisionID;
        this.divisionName = divisionName;
    }

        /**
         * @return customerID
         */
        public Integer getCustomerID() {
            return customerID;
        }

        /**
         * @param customerID
         */
        public void setCustomerID(Integer customerID) {
            this.customerID = customerID;
        }

        /**
         * @return customerName
         */
        public String getCustomerName() {
            return customerName;
        }

        /**
         * @param customerName
         */
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        /**
         * @return customerAddress
         */
        public String getCustomerAddress() {
            return customerAddress;
        }

        /**
         * @param address
         */
        public void setCustomerAddress(String address) {
            this.customerAddress = address;
        }

        /**
         * @return customerPostalCode
         */
        public String getCustomerPostalCode() {
            return customerPostalCode;
        }

        /**
         * @param postalCode
         */
        public void setCustomerPostalCode(String postalCode) {
            this.customerPostalCode = postalCode;
        }

        /**
         * @return customerPhoneNumber
         */
        public String getCustomerPhone() {
            return customerPhoneNumber;
        }

        /**
         * @param phone
         */
        public void setCustomerPhone(String phone) {
            this.customerPhoneNumber = phone;
        }

        /**
         * @return divisionID
         */
        public Integer getCustomerDivisionID() {
            return customerDivisionID;
        }

        /**
         * @return divisionName
         */
        public String getDivisionName() {
        return divisionName;
        }

        /**
         * @param divisionID
         */
        public void setCustomerDivisionID(Integer divisionID) {
            this.customerDivisionID = divisionID;
        }
    }
