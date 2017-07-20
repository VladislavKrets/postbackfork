package online.omnia.postback.core.threads;

import online.omnia.postback.controller.PostbackController;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by lollipop on 19.07.2017.
 */
public class PostbackHandlerThread implements Runnable{

    private Socket socket;
    final static Logger logger = Logger.getLogger(PostbackHandlerThread.class);

    public PostbackHandlerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            StringBuilder urlBuilder = new StringBuilder();
            String url;
            while ((url = reader.readLine()) != null) {
                urlBuilder.append(url);
            }
            PostbackController controller = new PostbackController();
            String answer = controller.sendPostback(urlBuilder.toString(), null);

            writer.write(answer);
            writer.flush();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            logger.debug("Exception during open sreams from socket");
            logger.debug(e.getMessage());
        }
    }
}
