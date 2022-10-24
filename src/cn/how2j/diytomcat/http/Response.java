package cn.how2j.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class Response {
    private StringWriter stringWriter;
    private PrintWriter writer;
    private String contentType;

    public Response() {
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
    }

    public String getContentType() {
        return contentType;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getBody() {
        String content = stringWriter.toString();
        return content.getBytes(StandardCharsets.UTF_8);
    }

}
