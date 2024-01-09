import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    private static ServerSocket server;
    private static final int port = 2048;
    private static ArrayList<ChatUser> UserList = new ArrayList<ChatUser>();
    private static String[] colors = {
        "\u001B[31m",
        "\u001B[32m",
        "\u001B[33m",
        "\u001B[34m",
        "\u001B[35m",
        "\u001B[36m",
    };
    private static int colorPicker = 0;
    private static int totalUsersJoined = 0;
    private static ObjectInputStream ois = null;

    public static void main (String args[]) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        System.out.println("Server's IP Address: ".concat(InetAddress.getLocalHost().toString()));
        
        while(true) {
            Socket socket = server.accept();
            ois = new ObjectInputStream(socket.getInputStream());
            String nameOfNewUser = (String) ois.readObject();
            ChatUser newUser = new ChatUser(socket, socket.getInetAddress(), nameOfNewUser, colors[colorPicker], totalUsersJoined);
            colorPicker = (colorPicker + 1) % 6;
            totalUsersJoined++;
            UserList.add(newUser);
            for(ChatUser user : UserList){
                user.updateListOfUsers(UserList);
            }
            newUser.start();
        }
    }
}
