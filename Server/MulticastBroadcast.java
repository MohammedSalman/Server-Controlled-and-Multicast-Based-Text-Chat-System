import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mohammed on 2/1/16.
 */
public class MulticastBroadcast implements Runnable
{
    private final int clientId;
    private final InetAddress group;
    Socket SOCK;
    MulticastSocket MSOCK;
    static HashMap<InetAddress, List<String>> usersInGroup = new HashMap<>();
    // A list to store all users for that particular group where group is the key in the hashmap.
    List<String> userList = new ArrayList<>();

    public MulticastBroadcast(Socket SOCK, int clientId) throws IOException, InterruptedException
    {
        this.SOCK=SOCK;
        this.clientId= clientId;
        MSOCK = new MulticastSocket(8885);
        group = InetAddress.getByName(Server.userInfo.get(clientId).get(2)); // 2 for the MCAST IP.
        MSOCK.joinGroup(group);
        String CurrentUser = Server.userInfo.get(clientId).get(0);  // 0 for the userName
        if (!usersInGroup.containsKey(group))
        {
            userList.add(CurrentUser);
            usersInGroup.put(group, userList);
        }
        else
        usersInGroup.get(group).add(CurrentUser);
        // broadcast a new joined member to all users.
        Thread.sleep(2000);
        BroadcastThisUserHasJoined();
    }

    private void BroadcastThisUserHasJoined() throws IOException {
        String currentUser =(Server.userInfo.get(clientId).get(0));
        String msg= ("User " + usersInGroup.get(group) + "****Joined the group****" + currentUser + "&");
        Broadcast(msg);
    }

    private void BroadcastUserHasLeft() throws IOException {
        String currentUser =(Server.userInfo.get(clientId).get(0));
        usersInGroup.get(group).remove(currentUser);
        Server.userInfo.remove(clientId);
        Broadcast("User " + usersInGroup.get(group) + "****Left the group****" + currentUser + "&");
    }

    @Override
    public void run() {
        while (true)
        {
            String currentUser =(Server.userInfo.get(clientId).get(0));
            try {
                DataInputStream dis = new DataInputStream(SOCK.getInputStream());
                String msg= dis.readUTF();
                if (msg.contains("#####") && msg.contains("@@@@@") && msg.contains("$$$$$") && msg.contains("%%%%%"))
                    continue; // skipping the first message as it is for only joining the group
                Broadcast(currentUser+ " " +  msg);
            }
            catch (IOException e)
            {
                // e.printStackTrace();
                System.out.print(e);
                try {
                    BroadcastUserHasLeft();
                    SOCK.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return;
            }
            }
    }

    private void Broadcast(String msg) throws IOException {
        DatagramPacket data = new DatagramPacket(msg.getBytes(), 0, msg.length(), group, 8885);
        MSOCK.send(data);
    }
}
