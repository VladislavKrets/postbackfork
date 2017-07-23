package online.omnia.postback.core.threads;

import online.omnia.postback.controller.PostbackController;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

/**
 * Created by lollipop on 19.07.2017.
 */
public class PostbackHandlerThread implements Runnable{

    private Socket socket;
    final static Logger logger = Logger.getLogger(PostbackHandlerThread.class);

    public PostbackHandlerThread(Socket socket) {
        this.socket = socket;
        try {
            socket.setSoTimeout(2000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Open input stream");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Getting url from input stream");
            StringBuilder urlBuilder = new StringBuilder();

            String url;
            String host = socket.getInetAddress().getHostName();

            while ((url = reader.readLine()) != null) {
                if (url.contains("GET")) {
                    String[] partsRequest = url.split(" ");
                    if (partsRequest.length == 3) {
                        url = partsRequest[1];
                    }

                    System.out.println(url);
                    urlBuilder.append(url);
                    break;
                }

            }
            System.out.println("Creating new postback controller");
            PostbackController controller = new PostbackController();
            System.out.println("sending postback to controller");
            String answer = controller.sendPostback(host + urlBuilder.toString(), null);
            System.out.println("Open output stream");
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(answer.getBytes("UTF-8"));
            outputStream.write("Status: 200\r\n".getBytes("UTF-8"));
            outputStream.write("Server: nginx\r\n".getBytes("UTF-8"));
            outputStream.write("Content-Type: text/html\r\n".getBytes("UTF-8"));
            outputStream.write("Cache-Control: no-cache\r\n".getBytes("UTF-8"));
            outputStream.write(("Date: " + new Date(System.currentTimeMillis()) + "\r\n").getBytes("UTF-8"));
            outputStream.write("X-Content-Type-Options: noshiff\r\n".getBytes("UTF-8"));
            outputStream.write("X-XSS-Protection: 1\r\n".getBytes("UTF-8"));
            outputStream.write("X-Frame-Options: Deny\r\n\r\n".getBytes("UTF-8"));
            outputStream.write("<html><body><p>OK</p></body></html>".getBytes("UTF-8"));
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.debug("Exception during open streams from socket");
            logger.debug(e.getMessage());
        }
    }
}
