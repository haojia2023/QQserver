package qqservice;
import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class QQServer {
    public static void main(String[] args) throws IOException {
        new QQServer();
    }
    private ServerSocket ss = null;
    private static HashMap<String,User> validUsers = new HashMap<>();
    static {
        for(int i = 0;i<10;i++) {
            validUsers.put((i + 1)  +  "",new User( (i + 1) + "","123456"));
        }
    }

    private boolean checkUser(String s,String psw){
        return s != null && validUsers.get(s) != null && psw.equals(validUsers.get(s).getPassID());
    }

    public QQServer() throws IOException {
        try {
            ss = new ServerSocket(9999);
            new Thread(new ServerSendToAll()).start();
            while(true){
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User o = (User) ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message mes = new Message();


                String userID = o.getUserID();
                if (checkUser(userID,o.getPassID())) {
                    mes.setMessageType(MessageType.LOGIN_SUCCEED);
                    oos.writeObject(mes);
                    ServerConnectClientThread sct = ManageConnectClient.searchSCT(userID);
                    if(sct != null) {
                        sct.getSocket().close();
                        sct.setLoop(false);
                    }
                    sct = new ServerConnectClientThread(userID, socket);
                    sct.start();
                    ManageConnectClient.addSCT(userID,sct);
                    //开始发送离线消息
                    switch (ManageOfflineMessage.SendMesList(userID, socket.getOutputStream())){
                        case 0:
                            System.out.println(userID + "的离线消息没有发送成功");
                            break;
                        case 1:
                            System.out.println(userID + "的离线消息全部发送成功");
                            break;
                    }
                }
                else {
                    mes.setMessageType(MessageType.LOGIN_FAIL);
                    oos.writeObject(mes);
                    socket.close();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(ss != null)
                ss.close();
        }
    }
}
