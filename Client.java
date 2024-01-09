import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static volatile boolean clientHasExited = false;
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        System.out.print("Enter the server's IP address: ");
        String serverAddress = scanner.nextLine();
        try {
            socket = new Socket(serverAddress, 2048);
        } catch (ConnectException e) {
            System.err.println("Connection failed. Please check the server address and port.");
            e.printStackTrace();
            System.exit(1);
        }
        sendUserName(socket);
        Thread senderThread = new Thread(new Sender(socket));
        Thread receiverThread = new Thread(new Receiver(socket));

        senderThread.start();
        receiverThread.start();
        while(clientHasExited == false){
            Thread.sleep(100);
        }
        Thread.sleep(2000);
        socket.close();
        System.exit(0);
    }
    
    public static void sendUserName(Socket s) throws IOException{
        String userName = "";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        userName = scanner.nextLine();
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(userName);
        oos.flush();
    }
}

class Sender implements Runnable {
    private Socket socket;

    public Sender(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Enter a message: \n");
                String message = scanner.nextLine();
                oos.writeObject(message);
                oos.flush();

                if (message.equals("exit")) {
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

class Receiver implements Runnable {
    private Socket socket;

    public Receiver(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run(){
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object receivedObject = ois.readObject();
                if (receivedObject == null) {
                    break;
                }
                String reply = (String) receivedObject;
                
                System.out.println(reply);
            }
            ois.close();
            Client.clientHasExited = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
