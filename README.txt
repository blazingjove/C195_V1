QAM2 — QAM2 Task 1: Java Application Development
Purpose of application

The purpose of this application is to provide a GUI based scheduling desktop application.
Author: Marco Alvarez

application version: 1.0
date: 12/27/2025
IDE and java module Information

IntelliJ IDEA 2024.3.2 (Ultimate Edition)
Build #IU-243.23654.117, built on January 15, 2025
Runtime version: 21.0.5+8-b631.30 amd64 (JCEF 122.1.9)
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

javafx: openjfx-21.0.6
mysql connector: mysql-connector-java-8.0.41
java 17.0.8 sdk

Additional report: located in the reports tab generates an alert of the next occurring appointment with all the appointment information in the alert

3 Lambda expressions: located in addAppointmentController under the Initialize Method

The program starts, connects to sql database and prompts user to log in with a username and password that is checked
against the user information in the database. after login a tabbed window is Displayed with appointments, customers, reports, and settings.
week is defined and sunday to saturday in the program (on local time)
month is defined as the calendar month (in local time)

Time in sql server is UTC.
All Time displayed in program is local time.
Time of company is in EST.


Code passes assessment on first attempt

