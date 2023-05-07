package qqservice;

import qqcommon.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ManageConnectClient {
    private static final HashMap<String,ServerConnectClientThread> hs = new HashMap<>();

    public static void ToAll(Message o) throws IOException {
        for (Map.Entry<String, ServerConnectClientThread> stringServerConnectClientThreadEntry : hs.entrySet()) {
            if (!o.getSender().equals(stringServerConnectClientThreadEntry.getKey()))
                new ObjectOutputStream(stringServerConnectClientThreadEntry.getValue().getSocket().getOutputStream())
                        .writeObject(o);
        }
    }
    public static void addSCT(String s,ServerConnectClientThread sct){
        hs.put(s,sct);
    }
    public static ServerConnectClientThread searchSCT(String s){
        return hs.get(s);
    }

    public static String getOnlineList(){
        StringBuffer stringBuffer = new StringBuffer();
        for(Map.Entry s:ManageConnectClient.hs.entrySet()){
            stringBuffer.append(s.getKey()).append(" ");
        }
        return new String(stringBuffer);
    }

    public static void delSCT(String userID){
        hs.remove(userID);
    }

    public static void UpdateOnline(){
        for (Map.Entry<String, ServerConnectClientThread> stringServerConnectClientThreadEntry : hs.entrySet()) {
            System.out.println(stringServerConnectClientThreadEntry.getKey() + stringServerConnectClientThreadEntry.getValue().getSocket().isClosed());
            if(stringServerConnectClientThreadEntry.getValue().getSocket().isClosed()) {
                delSCT(stringServerConnectClientThreadEntry.getKey());
            }
        }
    }
}
