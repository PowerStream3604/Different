# Different
This is a simple website for everyone who wants to implement register, login, email authentication functions with
nice and clean design.

This is a website implemented with jsp using ajax to transmit data between pages.

It uses smtp.googlegmail.com to authentication email.


For the database I used mysql. 

The connection with the DB and the delete, update, select, insert are implemented with java Bean files.
which are 
# Java
## LogonDataBean.java
which have the getters and setters for the data being inserted for registration

## LogonDBBean.java 
page used to insert data for user registration(and creates the connection with the database too

## Gmail.java 
page that designates the email address and the password used to authenticate user email

# Registration

## Register.css
this is the file for the design of Register.html file

## Register.html
page that gets the input for registration

## register.js
javascript file that validates for the input and sends data to email authentication page(emailSendAction.jsp) and also to regsterPro.jsp for registeration and to confirmId.jsp to check whether the Id is valid.

## registerPro.jsp
jsp file that gets the value from register.js file and transmits it to LogonDBBean.java with current time value set by using the System.currentTimeMillis() function.

# Email Authentication

## emailCheckAction.jsp
jsp file that sends email with the email address transmitted from register.js file. this file sends authentication email by using google smtp server.

the credentials the located in Gmail.java file which have to be alternated to your email address and password

## emailCheckAction.jsp 
jsp file that gets the email from the DataBase makes the URL with sha256 instance which will be used to check whether the email authentication is successfull or not.

## Gmail.java
java file that has the information(email address, password) to connect to google smtp server

# Login

## Login.html
this file gets css part itself. html file that gets the input.
![login](https://user-images.githubusercontent.com/63450340/106701774-8c186280-662a-11eb-8182-186593475e52.PNG)
## login.js 
uses ajax to give the inputted value to loginPro.jsp and do the validation

## loginPro.jsp
this file calls the function userCheck to validate with the values inputted from Login.html

## logout.jsp
this jsp file invalidates the session
