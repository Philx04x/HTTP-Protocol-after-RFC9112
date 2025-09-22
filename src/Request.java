import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {
    ParsingState state = ParsingState.REQUEST_LINE;
    RequestLine requestLine;
    Map<String, String> headers;


    Request(RequestLine requestLine, Map<String, String> headers) {
        this.requestLine = requestLine;
        this.headers = headers;
    }

    Request() {}

    private String requestFullState = "";
    public void parse(char[] inputStream) throws Exception {
        requestFullState = new String(inputStream);

        if(!requestFullState.contains("\r\n")) {
            return;
        }

        // RFC 9112:
        //      request-line   = method SP request-target SP HTTP-version
        //      field-line   = field-name ":" OWS field-value OWS

        switch(this.state) {
            case REQUEST_LINE -> {
                var parts = requestFullState.split("\r\n");
                var requestLine = parts[0];

                this.requestLine = (new RequestLine()).parse(requestLine);

                this.state = ParsingState.FIELD_LINE;
            }
            case FIELD_LINE -> {
                var splits = requestFullState;

                this.headers = Arrays.stream(
                        requestFullState.split("\r\n")
                )
                    .skip(1)
                    .collect(
                            Collectors.toMap(
                                    k -> k.split(":")[0],
                                    v -> v.split(":")[1].trim()
                            )
                );
            }
        }




    }

    @Override
    public String toString() {
        return requestFullState + "\n";
    }

    public class RequestLine {
        String method;
        String target;
        String version;

        RequestLine(String method, String target, String version) {
            this.method = method;
            this.target = target;
            this.version = version;
        }

        RequestLine() {}

        public RequestLine parse(String inputStream) throws Exception {
            if(inputStream.isEmpty()) return null;

            String[] parts = inputStream.split(" ");
            if(parts.length != 3) throw new Exception("Invalid Format for request line");


            RequestLine requestLine = new RequestLine();

            requestLine.method = parts[0];
            requestLine.target = parts[1];
            requestLine.version = parts[2];

            return requestLine;
        }
    }
}
