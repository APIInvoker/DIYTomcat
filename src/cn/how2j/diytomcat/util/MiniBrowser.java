package cn.how2j.diytomcat.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 单元测试用的一个模拟浏览器。<br/>这个浏览器会模拟发送 Http 协议的请求，并且获取完整的 Http 响应，通过这种方式，我们就可以更好的理解浏览器与服务器是如何通信。<br/>就可以不用每次打开浏览器输地址来看效果了，方便。
 *
 * @since 2022/9/29 22:09
 * @author Zheng Xin
 */
public class MiniBrowser {

    public static void main(String[] args) {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString = getContentString(url, false);
        System.out.println(contentString);
        String httpString = getHttpString(url, false);
        System.out.println(httpString);
    }

    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false);
    }

    public static String getContentString(String url) {
        return getContentString(url, false);
    }

    public static String getContentString(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (null == result)
            return null;
        return new String(result, StandardCharsets.UTF_8).trim();
    }

    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url, gzip);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        int pos = -1;
        for (int i = 0; i < response.length - doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);

            if (Arrays.equals(temp, doubleReturn)) {
                pos = i;
                break;
            }
        }
        if (-1 == pos)
            return null;

        pos += doubleReturn.length;

        return Arrays.copyOfRange(response, pos, response.length);
    }

    public static String getHttpString(String url, boolean gzip) {
        byte[] bytes = getHttpBytes(url, gzip);
        return new String(bytes).trim();
    }

    public static String getHttpString(String url) {
        return getHttpString(url, false);
    }

    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (-1 == port)
                port = 80;
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            client.connect(inetSocketAddress, 1000);
            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Host", u.getHost() + ":" + port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "how2j mini browser / java1.8");

            if (gzip)
                requestHeaders.put("Accept-Encoding", "gzip");

            String path = u.getPath();
            if (path.length() == 0)
                path = "/";

            String firstLine = "GET " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }

            httpRequestString.delete(httpRequestString.length() - 2, httpRequestString.length());
            PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
            pWriter.println(httpRequestString);
            InputStream is = client.getInputStream();

            result = readBytes(is);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString().getBytes(StandardCharsets.UTF_8);
        }

        return result;

    }

    public static byte[] readBytes(InputStream is) throws IOException {
        int buffer_size = 1024;
        byte[] buffer = new byte[buffer_size];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int length = is.read(buffer);
            if (-1 == length)
                break;
            baos.write(buffer, 0, length);
            if (length != buffer_size)
                break;
        }
        return baos.toByteArray();
    }
}
