Bank Account Server-Client Application
Overview
This project demonstrates a simple server-client application for managing bank account transactions. The server handles multiple client connections and processes commands to add, subtract, transfer funds, and check account balances. The application ensures thread safety using locks and logs all transactions to a file.

Features
Multi-threaded Server: Handles multiple client connections simultaneously.
Thread Safety: Uses ReentrantLock to ensure thread-safe operations.
Transaction Logging: Logs all transactions with timestamps to a file.
Account Management: Supports adding, subtracting, transferring funds, and checking balances.
Technologies Used, Java, Sockets for networking, Concurrency utilities (ReentrantLock), I/O for reading/writing data, Date and time formatting

How to Run
Server
1. Compile the server code: javac servercode.java
2. Run the server: java servercode

Client
1. Compile the client code: javac clientcode.java
2. Run the client: java clientcode

Enter commands in the client console to interact with the server.

Commands
ADD,ACCOUNT,VALUE: Adds the specified value to the specified account.
Example: ADD,A,100
SUBTRACT,ACCOUNT,VALUE: Subtracts the specified value from the specified account.
Example: SUBTRACT,B,50
TRANSFER,ACCOUNT1,ACCOUNT2,VALUE: Transfers the specified value from ACCOUNT1 to ACCOUNT2.
Example: TRANSFER,A,B,200
BALANCE: Retrieves the current balances of all accounts.
Example: BALANCE
Account Balances
Account A: Initial balance is 1000.
Account B: Initial balance is 1000.
Account C: Initial balance is 1000.
Logging
All transactions are logged to transaction_log.txt with timestamps.

Example Usage
Start the server.
Start a client and connect to the server.
Enter commands in the client console:
ADD,A,100
SUBTRACT,B,50
TRANSFER,A,B,200
BALANCE
