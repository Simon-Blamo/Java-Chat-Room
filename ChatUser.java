
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ChatUser extends Thread {
    private Socket clientSocket;
    private int userID;
    private String userName;
    private String color;
    private String resetColor = "\u001B[0m";
    private InetAddress address;
    private ArrayList<ChatUser> listOfUsers;
    private String message;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public ChatUser(Socket clientSocket, InetAddress address, String userName, String color, int userID){
        this.clientSocket = clientSocket;
        this.address = address;
        this.userName = userName;
        this.color = color;
        this.userID = userID;
        try {
            this.oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("IO error in User Server thread.");
            this.stop();
        }
        
    }

    public Socket getClientSocket(){
        return this.clientSocket;
    }

    public int getUserID(){
        return this.userID;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getColor(){
        return this.color;
    }

    public InetAddress getIP_Address(){
        return this.address;
    }

    public static void clearTerminal(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public void updateListOfUsers(ArrayList <ChatUser> LOU) {
        this.listOfUsers = LOU;
    }

    public ObjectOutputStream getObjectOutputStream(){
        return this.oos;
    }

    public void printListOfUsers() throws IOException{
        System.out.println("Server's IP Address: ".concat(InetAddress.getLocalHost().toString()));
        System.out.println("Active Users:");
        for (ChatUser user : listOfUsers){
            System.out.println("    ".concat(user.getUserName()).concat("(IP: ").concat(user.getIP_Address().toString()).concat("; User ID: ").concat(String.valueOf(user.getUserID())).concat(")"));
        }
    }

    public String convertReadObject(){
        String returnString = "";
        try {
            returnString = (String) ois.readObject();
        } catch (Exception e){
            System.out.println("IO error in User Server thread.");
            this.stop();
        }
        return returnString;
    }

    public void broadcastJoin(){
        synchronized (listOfUsers) {
            String msg = "User ".concat(getUserName()).concat("[").concat(getIP_Address().toString()).concat("] has joined!");
            for(ChatUser user : listOfUsers){
                if(this != user){
                    try {
                        user.getObjectOutputStream().writeObject(getColor().concat(msg).concat(resetColor).concat("\n"));
                    } catch (IOException e) {
                        System.out.println("IO ERROR! Client " + this.getName() + "terminated abruptly!");
                    }
                    
                }
            }
            try {
                clearTerminal();
                printListOfUsers();
            } catch (IOException e) {
                e.printStackTrace();
                this.stop();
            }
            
        }
    }

    public void broadcastMessage(String msg){
        synchronized (listOfUsers){
            for(ChatUser user : listOfUsers){
                if(this != user){
                    try {
                        user.getObjectOutputStream().writeObject(getColor().concat(getUserName()).concat(": ").concat(msg).concat(resetColor).concat("\n"));
                        user.getObjectOutputStream().flush();
                    } catch (IOException e) {
                        System.out.println("IO ERROR! Client " + this.getName() + "terminated abruptly!");
                        try {
                            handleExit();
                        } catch (Exception e0) {
                            e.printStackTrace();
                            this.stop();
                        }
                        
                    }
                    
                }
            }
        }
    }

    public void broadcastExit(){
        String msg = "User ".concat(getUserName()).concat("[").concat(getIP_Address().toString()).concat("] has left!");
        synchronized (listOfUsers){
            for(ChatUser user : listOfUsers){
                if(this != user){
                    try {
                        user.getObjectOutputStream().writeObject(getColor().concat(msg).concat(resetColor).concat("\n"));
                    } catch (IOException e) {
                        System.out.println("IO ERROR! Client " + this.getName() + "terminated abruptly!");
                        try {
                            handleExit();
                        } catch (Exception e0) {
                            e.printStackTrace();
                        }
                    }
                    
                }
            }
        }
    }

    public void handleExit() throws IOException {
        synchronized(listOfUsers){
            for(int i = 0; i < listOfUsers.size(); i++){
                if(listOfUsers.get(i) == this){
                    listOfUsers.remove(i);
                    break;
                }
            }
            try {
                clearTerminal();
                printListOfUsers();
            } catch (IOException e) {
                e.printStackTrace();
                this.stop();
            }
            getObjectOutputStream().writeObject(null);
            oos.flush();
        }

    } 

    public void run(){
        broadcastJoin();

        while(true) {
            message = convertReadObject();
            if(message.equalsIgnoreCase("exit")){
                break;
            } else {
                broadcastMessage(message);
            }
        }

        broadcastExit();

        try {
            handleExit();
        } catch (IOException e){
            System.out.println("IO error in User Server thread.");
            this.stop();
        }
        this.stop();
    }

}
