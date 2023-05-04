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
    private void UserISOnline(){
        ManageConnectClient.GetAll();
    }
    public QQServer() throws IOException {
        try {
            ss = new ServerSocket(9999);

            while(true){
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User o = (User) ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message mes = new Message();


                if (checkUser(o.getUserID(),o.getPassID())) {
                    mes.setMassageType(MessageType.LOGIN_SUCCEED);
                    oos.writeObject(mes);
                    ServerConnectClientThread sct = new ServerConnectClientThread(o.getUserID(), socket);
                    sct.start();
                    ManageConnectClient.addSCT(o.getUserID(),sct);
                }
                else {
                    mes.setMassageType(MessageType.LOGIN_FAIL);
                    oos.writeObject(mes);
                    socket.close();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }
}
