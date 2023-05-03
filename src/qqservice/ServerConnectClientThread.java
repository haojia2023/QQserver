package qqservice;

import qqcommon.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userID;

    public ServerConnectClientThread(String userID,Socket s){
        this.socket = s;
        this.userID = userID;
    }

    @Override
    public void run() {
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message o = (Message) ois.readObject();
            } catch (Exception e) {

            }

        }
    }
}
