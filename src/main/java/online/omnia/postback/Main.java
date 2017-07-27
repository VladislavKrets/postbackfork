package online.omnia.postback;


import online.omnia.postback.core.dao.MySQLDaoImpl;
import online.omnia.postback.core.threads.PostbackHandlerThread;
import online.omnia.postback.core.utils.FileWorkingUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by lollipop on 11.07.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, String> properties = FileWorkingUtils.iniFileReader();
        System.out.println("initializing db");
        MySQLDaoImpl.getInstance();
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(properties.get("port")));
        Thread thread;
        Socket socket;
        while (true) {
            System.out.println("Waiting for a socket...");
            socket = serverSocket.accept();
            System.out.println("Socket accepted. Creating new thread");
            thread = new Thread(new PostbackHandlerThread(socket));
            thread.setDaemon(true);
            System.out.println("Starting new thread...");
            thread.start();
            System.out.println("Thread started");
        }

    }
}
