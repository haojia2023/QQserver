package qqservice;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ManageOfflineMessage {
    private static HashMap<String, LinkedList<Message>> hm = new HashMap<>();

    public static int SendMesList(String userID, OutputStream oos){
        LinkedList<Message> offlineList = hm.get(userID);
        if (offlineList == null)return -1;

        //发送提示信息
        Message message = new Message();
        message.setMessageType(MessageType.COMMON_MES);
        message.setSender("服务器");
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//设置发送的时间的格式
        message.setGetter(userID);
        message.setContent("开始发送离线消息");
        try {
            new ObjectOutputStream(oos).writeObject(message);
        } catch (IOException e) {
            return 0;
        }

        //开始发送离线消息
        while(true){
            Message o = null;
            try {
                o = offlineList.remove();
            } catch (NoSuchElementException e) {
                break;
            }
            try {
                new ObjectOutputStream(oos).writeObject(o);
            } catch (IOException e) {
                offlineList.addFirst(o);
                return 0;
            }
        }
        hm.remove(userID);
        return 1;

    }

    public static void AddMessage(String userId, Message message){
        LinkedList messageList = hm.get(userId);
        if(messageList == null)
            hm.put(userId,messageList = new LinkedList<Message>());
        messageList.add(message);
    }
}
