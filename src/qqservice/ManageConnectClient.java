package qqservice;

import java.util.HashMap;
import java.util.Map;

public class ManageConnectClient {
    private static HashMap<String,ServerConnectClientThread> hs = new HashMap<>();

    public static java.util.Set<Map.Entry<String, ServerConnectClientThread>> GetAll(){
        return hs.entrySet();
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
