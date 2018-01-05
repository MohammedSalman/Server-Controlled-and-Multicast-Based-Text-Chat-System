import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ClientForm{
    public static ArrayList<String> ar = new ArrayList<>();
    static Socket SOCK = null;
    private static ConnectCommand connectCommand;

    public static String UserName = "";
    public static String RoomName = "";
    public static Date time;

    public static JFrame MainWindow = new JFrame();

    private static JButton B_CONNECT = new JButton();
    private static JButton B_DISCONNECT = new JButton();

    private static JButton B_SEND = new JButton();
    private static JLabel L_Message = new JLabel("Message: ");
    public static JTextField TF_Message = new JTextField(20);
    private static JLabel L_Conversation = new JLabel();
    public static JTextArea TA_CONVERSATION = new JTextArea();
    private static JScrollPane SP_CONVERSATION = new JScrollPane();
    private static JLabel L_ONLINE = new JLabel();
    public static JList JL_ONLINE = new JList(ar.toArray());
    private static JScrollPane SP_ONLINE = new JScrollPane();


    public static JFrame LogInWindow = new JFrame();
    public static JTextField TF_UserNameBox = new JTextField(20);
    private static JLabel L_EnterUserName = new JLabel("Enter username: ");
    private static JLabel L_EnterRoomName = new JLabel("Enter room name: ");
    public static JTextField TF_RoomNameBox = new JTextField(20);
    private static JButton B_ENTER = new JButton("ENTER");
    private static JPanel P_LogIn = new JPanel();
    private static ChatRequest chatRequest; ///////////////////////////////////////////////

    public static void main(String[] args)
    {

        BuildMainWindow();
        Initialize();
    }
    public static void Initialize()
    {
        B_SEND.setEnabled(false);
        B_DISCONNECT.setEnabled(false);
        B_CONNECT.setEnabled(true);
    }

    public static void BuildLogInWindow()
    {
        LogInWindow.setTitle("What's your name?");
        LogInWindow.setSize(400, 150);
        LogInWindow.setLocation(250, 200);
        LogInWindow.setResizable(false);
        P_LogIn = new JPanel();
        P_LogIn.add(L_EnterUserName);
        P_LogIn.add(TF_UserNameBox);
        P_LogIn.add(L_EnterRoomName);
        P_LogIn.add(TF_RoomNameBox);
        P_LogIn.add(B_ENTER);
        LogInWindow.add(P_LogIn);

        Login_Action();
        LogInWindow.setVisible(true);
    }

    public static void BuildMainWindow()
    {
        MainWindow.setTitle(UserName + "'s Chat Box");
        MainWindow.setSize(450, 500);
        MainWindow.setLocation(220, 180);
        MainWindow.setResizable(false);
        ConfigureMainWindow();
        MainWindow_Action();
        MainWindow.setVisible(true);
    }

    public static void ConfigureMainWindow()
    {

        MainWindow.setSize(500, 320);
        MainWindow.getContentPane().setLayout(null);


        B_SEND.setText("SEND");
        MainWindow.getContentPane().add(B_SEND);
        B_SEND.setBounds(250, 40, 81, 25);


        B_DISCONNECT.setText("Disconnect");
        MainWindow.getContentPane().add(B_DISCONNECT);
        B_DISCONNECT.setBounds(10, 40, 110, 25);

        B_CONNECT.setText("CONNECT");
        B_CONNECT.setToolTipText("");
        MainWindow.getContentPane().add(B_CONNECT);
        B_CONNECT.setBounds(130, 40, 110, 25);

        L_Message.setText("Message:");
        MainWindow.getContentPane().add(L_Message);
        L_Message.setBounds(10, 10, 60, 20);


        TF_Message.requestFocus();
        MainWindow.getContentPane().add(TF_Message);
        TF_Message.setBounds(70, 4, 260, 30);

        L_Conversation.setHorizontalAlignment(SwingConstants.CENTER);
        L_Conversation.setText("Conversation");
        MainWindow.getContentPane().add(L_Conversation);
        L_Conversation.setBounds(100, 70, 140, 16);

        TA_CONVERSATION.setColumns(20);
        TA_CONVERSATION.setFont(new java.awt.Font("Tahoma", 0, 12));

        TA_CONVERSATION.setLineWrap(true);
        TA_CONVERSATION.setRows(5);
        TA_CONVERSATION.setEditable(false);

        SP_CONVERSATION.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_CONVERSATION.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_CONVERSATION.setViewportView(TA_CONVERSATION);
        MainWindow.getContentPane().add(SP_CONVERSATION);
        SP_CONVERSATION.setBounds(10, 90, 330, 180);

        L_ONLINE.setHorizontalAlignment(SwingConstants.CENTER);
        L_ONLINE.setText("Currently Online");
        L_ONLINE.setToolTipText("");
        MainWindow.getContentPane().add(L_ONLINE);
        L_ONLINE.setBounds(350, 70, 130, 16);


        SP_ONLINE.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_ONLINE.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_ONLINE.setViewportView(JL_ONLINE);
        MainWindow.getContentPane().add(SP_ONLINE);
        SP_ONLINE.setBounds(350, 90, 130, 180);
    }

    public static void Login_Action()
    {
        B_ENTER.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ACTION_B_ENTER();
                    }
                }
        );
    }

    public static void ACTION_B_ENTER() {

        try
        {
            SOCK = new Socket(InetAddress.getLocalHost(), 2500);
        }
        catch (IOException e)
        {


        }
        if(!TF_UserNameBox.getText().equals(""))
        {
            UserName = TF_UserNameBox.getText().trim();
            RoomName = TF_RoomNameBox.getText().trim();
            time = new Date(); // is it current time?
            // A_Chat_Server.CurrentUsers.add(UserName);
            MainWindow.setTitle(UserName+"'s Chat Box" + "Logged to room " + RoomName);
            LogInWindow.setVisible(false);
            B_SEND.setEnabled(true);
            B_DISCONNECT.setEnabled(true);
            B_CONNECT.setEnabled(false);
            connectCommand = new ConnectCommand(SOCK, UserName, RoomName, time);
            connectCommand.Connect();
            //creating chatRequest object with current SOCK to send messages later.
            chatRequest = new ChatRequest(SOCK);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please enter a name!");
        }
    }

    public static void MainWindow_Action()
    {
        B_SEND.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                            ACTION_B_SEND();

                    }
                }
        );

        B_DISCONNECT.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ACTION_B_DISCONNECT();
                    }
                }
        );

        B_CONNECT.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BuildLogInWindow();
                    }
                }
        );
    }

    public static void ACTION_B_SEND()  {
        if(!TF_Message.getText().equals(""))
        {
            chatRequest.SEND(TF_Message.getText()); //+ "*****" + Receiver.getClientId() + "!!!!!" );  //change object later ****************
            TF_Message.requestFocus();
        }
    }

    public static void ACTION_B_DISCONNECT()
    {
        try
        {
            connectCommand.Disconnect(); //change object later *****************
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}