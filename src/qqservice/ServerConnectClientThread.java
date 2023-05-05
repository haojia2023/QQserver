package qqservice;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userID;

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClientThread(String userID, Socket s){
        this.socket = s;
        this.userID = userID;
    }


    @Override
    public void run() {
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message o = (Message) ois.readObject();



                if(o.getMassageType().equals(MessageType.GET_ONLINE_USER)){
                    Message mes = new Message();
                    mes.setGetter(o.getSender());
                    mes.setMassageType(MessageType.SEND_ONLINE_USER);
                    mes.setContent(ManageConnectClient.getOnlineList());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(mes);
                }else if(o.getMassageType().equals(MessageType.USER_EXIT)){
                    ManageConnectClient.delSCT(o.getSender());
                    socket.close();
                    break;
                }
            } catch (Exception e) {

            }

        }
    }
}
