import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Main {
    static void main(String[] args) throws IOException {
        IO.println("HTTP PROTOCOL HTTP/1.1 nach RFC9112");

        ServerSocket server = SocketFactory.serverFactory(20304);

        int connCount = 0;
        while (true) {
            Socket conn = server.accept();

            connCount++;

            Thread.startVirtualThread(
                new ConnectionHandler(conn)
            ).setName("connection-handler-" + connCount);


        }

    }
}