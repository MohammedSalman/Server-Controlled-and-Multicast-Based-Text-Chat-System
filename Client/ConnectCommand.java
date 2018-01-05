import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Created by mohammed on 1/31/16.
 */
public class ConnectCommand {
    String userName;
    String chatroomName;
    Date time;
    Receiver receiver;
    Socket SOCK;
    public ConnectCommand(Socket SOCK, String userName, String chatroomName, Date time) {
        this.SOCK = SOCK;
        this.userName = userName;
        this.chatroomName = chatroomName;
        this.time = time;
    }
    public void Connect() //String username, String chatroomname, Date time
    {
        System.out.println("current variables are " + userName + chatroomName + time.toString());
       /* this.userName =username;
        this.chatroomName=chatroomname;
        this.time=time;*/
        try {
            DataOutputStream dos = new DataOutputStream(SOCK.getOutputStream());
            dos.writeUTF("#####" + userName + "@@@@@" + chatroomName + "$$$$$" + this.time + "%%%%%"); //no need
            System.out.println("SOCK info is " + SOCK.toString());
            receiver = new Receiver(SOCK);
            Thread X = new Thread(receiver);
            X.start();
        } catch (Exception e) {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, "Server not responding.");
            System.exit(0);
        }
    }
    public void Disconnect() throws IOException {

        SOCK.close();
        JOptionPane.showMessageDialog(null, "You have been disconnected");
        System.exit(0);
    }
}
