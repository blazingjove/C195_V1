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
Additional report

For the custom report, I chose to display the number of appointments per Country. I chose to have sql do all the work and created a query that contains inner joins which combines three tables and spits out two columns (Country and count). The query is complicated but made the java side was very simple and I was able to pull this information into a tableview to present the data.
How to run the program
As the program starts, a login screen is presented. The user will be required to have a valid username and password that matches information in a mysql database. This program requires java 11 and has not been tested with any other jvm.