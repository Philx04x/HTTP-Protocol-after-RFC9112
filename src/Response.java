public class Response {
    private int code;
    private String httpVersion = "HTTP/1.1";
    private String reasonPhrase;
    private String body;

    public Response(int code, String reasonPhrase, String body) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("HTTP/1.1")
                .append(" ")
                .append(code)
                .append(" ")
                .append(reasonPhrase)
                .append("\r\n")
                .append("Connection: close")
                .append("\r\n")
                .append("Content-Length: " + body.getBytes().length)
                .append("\r\n")
                .append("\r\n")
        .append(body);

        return sb.toString();
    }
}
