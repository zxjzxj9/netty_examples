package netty_examples;

public class EchoKqueueServer {

    private final int port;
    public EchoKqueueServer(int port) {
        this.port = port;
    }

    public void start() {

    }

    public static void main(String[] args) {
        EchoKqueueServer echoKqueueServer = new EchoKqueueServer(Integer.parseInt(args[0]));
        echoKqueueServer.start();
    }
}
