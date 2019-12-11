# BankApplication

## Premise
This program is not intended to be considered as a real-world application but rather to be considered as a small summer project
with the goal to learn Java programming and its unique features.

The following is a general list about concepts implemented in this project:
* Java RMI
* JavaFx
* Apache POI
* JDBC
* Printing using JavaFx.print

## Introduction
BankApplication is a project that consists of two programs that share a common code base.</br>
The two programs are a GUI based Client that connects to Server program using Java RMI.
The server is CLI based and it connects to a MySql database to store and retrieve data such as Users and different types
of Accounts. Client, Server and Database all work on the localhost.</br>

## Features
### Server
The Server program implemeants the methods of a JAVA Remote interface (Java RMI).</br>
The main characteristics of the Server are its ability to write to an excel file using the Apache POI libraries and
its ability to store and retreive data to a Database.

The following is a general list about the features implemented on the Server side of the project:
* Implemeant methods of a Remote interface
* Create a new excel file at start up
* Write to the excel file to log data
* Store user accounts on a database
* Hash user passwords
* Store different types of bank accounts(Savings, Chequing and GIC) on the same table using the BLOB data type
* Retreive bank accounts from database
* Prompt user with meaningful messages about what the client is doing
* Provide some security so that User Account with Admin rights have more features than regular User Accounts

### Client
* Provide a GUI to interact with the Server
* Registration and Login
* Display accounts in a Table
* Interface to update user's email, password and full name
* Interface to print a page to the computer's default printer
* Interface to create Accounts(Savings, Chequing and GIC)
* Interface to delete Accounts
* Pop-up window for warnings(Wrong email, minimum age not respected, wrong login credentials, etc)
* Interface to deposit, withdraw or transfer money between accounts
* Provide some security so that User Account with Admin rights have more features than regular User Accounts
