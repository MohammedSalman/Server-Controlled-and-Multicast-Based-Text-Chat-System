import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by mohammed on 1/31/16.
 */
public class Server {
    static ServerSocket server;
    Socket SOCK;
    MulticastBroadcast MB;
    static int count; // to be used as ClientId
    static HashMap<Integer, List<String>> userInfo = new HashMap<>();
    static HashMap<String, String> roomInfo = new HashMap<>(); //An hashmap to store chatRooms with their IP's.
    public Server() throws IOException
    {
        server = new ServerSocket(2500);
        try
        {
        while (true)
        {
            SOCK = server.accept();
            DataInputStream dis = new DataInputStream(SOCK.getInputStream());
            String msg = dis.readUTF();
             // this is a command from unicast to join the group in multicast
                int clientId = count++;
                String  userName = msg.substring(msg.lastIndexOf("#")+1, msg.indexOf("@"));
                String  chatroomName = msg.substring(msg.lastIndexOf("@")+1, msg.indexOf("$"));
                String McastIP;
                /*
                if chatroomName doesn't exist before, then generate new
                Multicast IP and add it to the hashmap roomInfo
                */

                if (!roomInfo.containsKey(chatroomName))
                {
                    McastIP = GenerateMcastIP();
                    roomInfo.put(chatroomName, McastIP);
                }
                else
                {
                    McastIP = roomInfo.get(chatroomName);
                }
                List<String> TEMP_LIST= new ArrayList<>();
                //adding userName, chatroomName, and time to arraylist.
                TEMP_LIST.add(userName);
                TEMP_LIST.add(chatroomName);
                TEMP_LIST.add(McastIP);
                //adding the previous arraylist to the hashmap with they key (clientId).
                userInfo.put(clientId, TEMP_LIST);
                /*
                generate McastIP
                send clientId to the client along with the Multicast IP
                */
                DataOutputStream dos = new DataOutputStream(SOCK.getOutputStream());
                dos.writeUTF("##clientId##" + clientId + "$$McastIP&&" + McastIP + "%%%%%");

           // Getting things ready for broadcasting the message
                MB = new MulticastBroadcast (SOCK, clientId);
                Thread X = new Thread (MB);
                X.start();
        }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    private String GenerateMcastIP() {
        Random r = new Random();
        // this is wrong because this might produce the same IP but that is rare
        return "224." + r.nextInt(255) + "." + r.nextInt(255) + "." + r.nextInt(255);
    }
    public static void main(String[] args) throws IOException
    {
        new Server();
    }
}