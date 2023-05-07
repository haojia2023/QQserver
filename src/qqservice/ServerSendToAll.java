package qqservice;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ServerSendToAll implements Runnable{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入要发送的内容（输入Exit表示退出）");
            String s = scanner.next();
            if ("Exit".equalsIgnoreCase(s))
                return;
            Message message = new Message();
            message.setMessageType(MessageType.File_MES);
            message.setSender("服务器");
            message.setContent(s);
            message.setGetter("-1");
            message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//设置发送的时间的格式
            try {
                ManageConnectClient.ToAll(message);
                System.out.println("发送成功");
            } catch (IOException e) {
                System.out.println("发送失败");
            }
        }
    }
}
