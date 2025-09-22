import java.io.IOException;
import java.net.ServerSocket;

public class SocketFactory {
    static ServerSocket serverFactory(int port) throws IOException {
        return new ServerSocket(port);
    }
}
