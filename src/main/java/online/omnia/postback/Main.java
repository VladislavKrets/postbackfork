package online.omnia.postback;


import online.omnia.postback.controller.PostbackController;
import online.omnia.postback.core.exceptions.NoClickIdException;
import online.omnia.postback.core.threads.PostbackHandlerThread;
import online.omnia.postback.core.utils.FileWorkingUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by lollipop on 11.07.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, String> properties = FileWorkingUtils.iniFileReader();

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(properties.get("port")));
        Thread thread;
        Socket socket;
        while (true) {
            socket = serverSocket.accept();
            thread = new Thread(new PostbackHandlerThread(socket));
            thread.setDaemon(true);
            thread.start();
        }
    }
}
