package qqservice;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class ServerConnectClientThread extends Thread{
    private boolean loop = true;
    private Socket socket;
    private String userID;

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClientThread(String userID, Socket s){
        this.socket = s;
        this.userID = userID;
    }


    @Override
    public void run() {
        while(loop){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message o = (Message) ois.readObject();


                String messageType = o.getMessageType();
                if(messageType.equals(MessageType.GET_ONLINE_USER)){
                    Message mes = new Message();
                    mes.setGetter(o.getSender());
                    mes.setMessageType(MessageType.SEND_ONLINE_USER);

                    //可配合心跳检测实现检测实时用户列表
                    //ManageConnectClient.UpdateOnline();
                    mes.setContent(ManageConnectClient.getOnlineList());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(mes);
                }else if(messageType.equals(MessageType.USER_EXIT)){
                    ManageConnectClient.delSCT(o.getSender());
                    socket.close();
                    break;
                }else if(messageType.equals(MessageType.COMMON_MES)){
                    if(o.getGetter().equals("-1")){
                        o.setGetter("所有人");
                        for (Map.Entry<String, ServerConnectClientThread> stringServerConnectClientThreadEntry : ManageConnectClient.GetAll()) {
                            new ObjectOutputStream(stringServerConnectClientThreadEntry.getValue().getSocket().getOutputStream())
                                    .writeObject(o);
                        }
                    }else{
                        new ObjectOutputStream(ManageConnectClient.searchSCT(o.getGetter()).getSocket().getOutputStream())
                                .writeObject(o);
                    }
                }
            } catch (Exception e) {

            }

        }
    }
}
