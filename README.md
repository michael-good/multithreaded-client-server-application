# multithreaded-client-server-application
This Java program is made of 3 parts:
  1. Application: It plays the role of server or back-end service of the system.
  2. ClientNumbers: Client that connects to Application.
  3. ClientTerminate: Client that connects to Application.
  
# Basic functionality of the program
Application opens a socket and restricts input to at most 5 concurrent
clients. Clients will connect to the Application and write any number of 9 digit
numbers, and then close the connection. The Application must write a de-duplicated
list of these numbers to a log file in no particular order.

# Requirements
1. The Application must accept input from at most 5 concurrent clients on
TCP/IP port 4000.
2. Input lines presented to the Application via its socket must either be
composed of exactly nine decimal digits (e.g.: 314159265 or 007007009)
immediately followed by a server-native newline sequence; or a termination
sequence as detailed below.
3. Numbers presented to the Application must include leading zeros as
necessary to ensure they are each 9 decimal digits.
4. The log file, to be named "numbers.log", must be created anew and/or
cleared when the Application starts.
5. Only numbers may be written to the log file. Each number must be followed
by a server-native newline sequence.
6. No duplicate numbers may be written to the log file.
7. Any data that does not conform to a valid line of input should be discarded
and the client connection terminated immediately and without comment.
8. Every 10 seconds, the Application must print a report to standard output:
o The difference since the last report of the count of new unique
numbers that have been received.
o The difference since the last report of the count of new duplicate
numbers that have been received.
o The total number of unique numbers received for this run of the
Application.
o Example text: Received 50 unique numbers, 2 duplicates. Unique
total: 567231
9. If any connected client writes a single line with only the word "terminate"
followed by a server-native newline sequence, the Application must
disconnect all clients and perform a clean shutdown as quickly as possible.

# Build and run instructions
## Linux
Type the following commands from the command line in order:
1. cd ~
2. git clone https://github.com/michael-good/multithreaded-client-server-application.git
3. cd multithreaded-client-server-application
4. mkdir out
5. javac -cp lib/commons-lang3-3.9.jar -d out src/miguel/angel/bueno/sanchez/main/*.java

All project files are now compiled into out/ directory.
In order to properly execute the program, one must open three different terminals up and navigate to ~/multithreaded-client-server-application.
Following commands will execute the server Application and clients. Use one of them for each terminal in the corresponding order:
- java -cp lib/commons-lang3-3.9.jar:out miguel.angel.bueno.sanchez.main.Application
- java -cp lib/commons-lang3-3.9.jar:out miguel.angel.bueno.sanchez.main.ClientNumbers
- java -cp lib/commons-lang3-3.9.jar:out miguel.angel.bueno.sanchez.main.ClientTerminate

# Dependencies and other considerations
This project has been written using Java 8 OpenSDK (Java 1.8 / OpenJDK8).
It makes use of some methods from org.apache.commons.commons-lang3-3.9 from Apache Commons Lang library. 

Make sure that such dependencies are specified to the the Java compiler (javac) when building the project. Just follow instructions above for a correct compilation.

