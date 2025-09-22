import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

public class Client {
    static void main(String[] args) throws Exception {
        Socket conn = new Socket("localhost", 20304);

        var outgoing = new BufferedOutputStream(conn.getOutputStream());

        outgoing.write("GET /api/philipp HTTP/1.1\r\nContent-Type: application/json\r\nCache-Control: no-cache\r\n\r\n".getBytes());
        outgoing.flush();

        Thread.sleep(10000);
    }
}
