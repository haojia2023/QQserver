package qqservice;

import java.util.HashMap;

public class ManageConnectClient {
    private static HashMap<String,ServerConnectClientThread> hs = new HashMap<>();

    public static void addSCT(String s,ServerConnectClientThread sct){
        hs.put(s,sct);
    }
    public static ServerConnectClientThread searchSCT(String s){
        return hs.get(s);
    }
}
