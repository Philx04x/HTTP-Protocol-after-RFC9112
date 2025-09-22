import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ConnectionHandler implements Runnable {
    Socket conn;

    ConnectionHandler(Socket conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        IO.println("Connection Established in thread " + Thread.currentThread().getName());

        try {
            var incoming = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            var outgoing = new BufferedWriter(
                    new OutputStreamWriter(conn.getOutputStream())
            );

            // Char ist in Java 2 Bytes deswegen 1024 Byts * 2 = 2048 Bytes
            char[] destBuffer = new char[1024];

            int charsRead = 0;
            int BUFFER_SKIP = 16;
            // ready() checkt auch ob Buffer empty
            Request parser = new Request();
            while (incoming.ready())  {
                incoming.read(destBuffer, charsRead, BUFFER_SKIP);

                charsRead += BUFFER_SKIP;

                for (int i = 1; i < charsRead; i++) {
                    char prevCh = destBuffer[i + (charsRead - BUFFER_SKIP) - 1];
                    char ch = destBuffer[i + (charsRead - BUFFER_SKIP)];

                    if(prevCh == '\r' && ch == '\n') {
                        parser.parse(
                                Arrays.copyOfRange(
                                        destBuffer, 0, i + (charsRead - BUFFER_SKIP) + 1
                                )
                        );
                    }

                    if (prevCh == '\u0000' && ch == '\u0000') {
                        break;
                    }
                }
            }

            IO.println("Incoming Request Parsed:\n" + parser.toString());

            // TODO: Hier könnte man nun das Processing machen

            // Sende Response
            Response res = new Response(200, "OK", "Hallo vom Server");

            outgoing.write(res.toString());
            outgoing.flush();

            conn.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
