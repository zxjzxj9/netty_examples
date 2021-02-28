package netty_examples;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.lang.Thread;

public class PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        try {
            for(;;) {
                final Socket client = socket.accept();
                System.out.println("Accepting connection from " + client);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = client.getOutputStream();
                            out.write(("Hi, " + client + " \n").getBytes(StandardCharsets.UTF_8));
                            out.flush();
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        PlainOioServer server = new PlainOioServer();
        server.serve(Integer.parseInt(args[0]));
    }
}
