import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Created by mohammed on 2/2/16.
 */
public class ChatRequest {

    static Socket SOCK;
    public ChatRequest(Socket SOCK)
    {
        ChatRequest.SOCK =SOCK;

    }

    public void SEND(String text) {
       Date time = new Date();
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(SOCK.getOutputStream());
            dos.writeUTF(" @" + time+ ": " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientForm.TF_Message.setText("");
    }
}