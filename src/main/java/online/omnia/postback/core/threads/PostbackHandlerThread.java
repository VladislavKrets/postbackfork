package online.omnia.postback.core.threads;

import online.omnia.postback.controller.MainController;
import online.omnia.postback.queue.QueueWriter;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.Date;

/**
 * Thread which gets socket and handles it
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

    /**
     * Method gets socket and handles it
     * Writes url to file
     * Returns ok page
     */
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
                System.out.println(url);
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
            //MainController controller = new MainController();
            //System.out.println("sending postback to controller");
            //String answer = controller.sendPostback(host + urlBuilder.toString());
            //QueueWriter.writeUrl(host + urlBuilder.toString());
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
            String ip = null;

            if (socketAddress instanceof InetSocketAddress) {
                InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
                if (inetAddress instanceof Inet4Address)
                    ip = inetAddress.toString().split("/")[1];
                else if (inetAddress instanceof Inet6Address)
                    ip = inetAddress.toString().split("/")[1];
                else
                    System.err.println("Not an IP address.");
            } else {
                System.err.println("Not an internet protocol socket.");
            }
            System.out.println("sending");
            QueueWriter.sendMessage(host + urlBuilder.toString() + " " + ip);
            System.out.println("Open output stream");
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes("UTF-8"));
            outputStream.write("OK".getBytes("UTF-8"));
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.debug("Exception during open streams from socket");
            logger.debug(e.getMessage());
        }
    }
}
