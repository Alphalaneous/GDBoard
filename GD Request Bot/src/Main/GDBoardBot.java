package Main;

import com.cavariux.twitchirc.Json.JsonObject;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

class GDBoardBot {
    static boolean connected = false;
    static boolean failed = false;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket clientSocket;
    private static JDialog dialog = new JDialog();
    private static JPanel panel = new JPanel();
    private static JLabel tf = new JLabel("Connecting...  ");
    private static JButtonUI defaultUI = new JButtonUI();
    private static RoundedJButton button = new RoundedJButton("\uE72C");

    private static int attempts = 0;
    static void start() throws IOException {
        attempts++;
        if(attempts == 5){
            APIs.setOauth();
            start();
            attempts = 0;
            return;
        }
        dialog.setSize(new Dimension(200,100));
        tf.setForeground(Color.WHITE);
        tf.setFont(new Font("bahnschrift", Font.BOLD, 20));
        defaultUI.setBackground(new Color(50, 50, 50));
        defaultUI.setHover( new Color(80, 80, 80));
        defaultUI.setSelect( new Color(70, 70, 70));

        button.setPreferredSize(new Dimension(50, 50));
        button.setUI(defaultUI);
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                APIs.setOauth();
                attempts = 0;
            }
        });
        panel.add(tf);
        panel.add(button);
        panel.setBackground(new Color(31, 31, 31));
        panel.setLayout(new GridBagLayout());
        dialog.add(panel);

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        for ( WindowListener wl : dialog.getWindowListeners())
            dialog.removeWindowListener(wl);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        dialog.setResizable(false);
        dialog.setTitle("Connecting to GDBoard");
        dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - dialog.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - dialog.getHeight()/2);
        dialog.setVisible(true);

        try {
            clientSocket = new Socket("165.227.53.200", 2963);
            //clientSocket = new Socket("localhost", 2963);
        }
        catch (ConnectException e){
            System.out.println("failed");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            start();
            return;
        }
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread thread = new Thread(() -> {
            String inputLine;
            while (true) {
                while(clientSocket.isClosed() || !clientSocket.isConnected()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if ((inputLine = in.readLine()) == null) break;
                } catch (IOException e) {
                    break;
                }
                String event = "";
                System.out.println(inputLine);
                try {
                    JsonObject object = JsonObject.readFrom(inputLine);
                    if (object.get("event") != null) {
                        event = object.get("event").toString().replaceAll("\"", "");
                    }
                    if (event.equalsIgnoreCase("connected")) {
                        connected = true;
                        dialog.setVisible(false);
                    } else if (event.equalsIgnoreCase("connect_failed")) {
                        System.out.println(object.get("error").toString().replaceAll("\"", ""));
                        failed = true;
                    } if ((event.equalsIgnoreCase("command") || event.equalsIgnoreCase("level_request")) && Main.allowRequests) {
                        String sender = object.get("sender").toString().replaceAll("\"", "");
                        String message = object.get("message").toString().replaceAll("\"", "");
                        boolean mod = object.get("mod").asBoolean();
                        boolean sub = object.get("sub").asBoolean();
                        Thread thread1 = new Thread(() -> {
                            try {
                                while(ServerChatBot.processing){
                                    Thread.sleep(50);
                                }
                                ServerChatBot.onMessage(sender, message, mod, sub);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        thread1.start();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dialog.setVisible(true);
            System.out.println("failed here");
            try {
                start();
                Thread.sleep(1000);
                JSONObject authObj = new JSONObject();
                authObj.put("request_type", "connect");
                authObj.put("oauth", Settings.oauth);
                GDBoardBot.sendMessage(authObj.toString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
    static void sendMessage(String message){
        out.println(message);
    }
    static void restart() throws IOException {
        if(clientSocket != null) {
            clientSocket.close();
        }
        clientSocket = new Socket("165.227.53.200", 2963);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject authObj = new JSONObject();
        authObj.put("request_type", "connect");
        authObj.put("oauth", Settings.oauth);
        GDBoardBot.sendMessage(authObj.toString());
    }
}
