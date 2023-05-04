package qqservice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ManageConnectClient {
    private static HashMap<String,ServerConnectClientThread> hs = new HashMap<>();

    public static Collection<ServerConnectClientThread> GetAll(){
        return hs.values();
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
            stringBuffer.append(s.getKey()+" ");
        }
        return new String(stringBuffer);
    }

    public static void delSCT(String userID){
        hs.remove(userID);
    }
}
