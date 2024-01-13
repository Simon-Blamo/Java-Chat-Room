# Java Chat Room
This Java Chat Room Application is a simple yet effective real-time communication tool built using Java's robust networking and concurrency features. It allows multiple users to connect to a chat server and communicate with each other through text messages. Each user is represented by a unique color, enhancing the chat room's visual appeal and user distinction.

## Project Specifics
- **Server-Client Architecture:** The application follows a classic server-client model. The server manages client connections, maintains a list of active users, and handles message broadcasting.
- **Multithreading:** Both server and client sides employ multithreading to handle multiple client connections and simultaneous read-write operations efficiently.
- **Network Communication:** Utilizes Java's Socket programming for network communication, with ObjectOutputStream and ObjectInputStream to send and receive messages.
- **Terminal-Based Interface:** The application runs in the console, making it lightweight and easily runnable on various environments.


## Getting Started
### Prerequisites
- Java Development Kit (JDK) installed on your system.

### Accessing the Repo/Files
1. Clone the repository:
   
   `git clone https://github.com/Simon-Blamo/Java-Chat-Room.git`

2. Navigate to the Java Chat Room directory:

   `cd Java-Chat-Room`

### Running the Server
1. Open a terminal or command prompt.
2. Navigate to the directory containing the Server.java file.
3. Compile the ChatUser helper file: javac ChatUser.java
4. Start the server:

   `java Server.java`
   
6. The server will start and display its IP address.

### Running the Client
1. Open a new terminal or command prompt for each client.
2. Navigate to the directory containing the Client.java file.
3. Start the client:

   `java Client.java`
   
5. Enter the server's IP address as prompted.
6. Enter a username as prompted.
7. Start chatting! Type exit to leave the chat room.

### Interaction
- After connecting, type messages in the client's console and press Enter to send.
- Each message from different users will appear in unique colors.

## Disclaimer
- This application is developed for educational purposes and demonstrates basic Java networking and multithreading concepts.
- It runs in a local network environment and is not intended for use over the internet.
- User interface is console-based, limiting the scope of visual customization and user interaction features.
