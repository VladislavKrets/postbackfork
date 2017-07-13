package online.omnia.postback.core.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class HttpMethodsUtils {
    private static CloseableHttpClient httpClient;
    final static Logger logger = Logger.getLogger(HttpMethodsUtils.class);

    static {
        httpClient = HttpClients.createDefault();
    }

    public static String getMethod(String url, Map<String, String> headers){
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
            return getAnswer(httpGet);
        } catch (Exception e) {
            logger.debug("Exception during executing get method");
            logger.debug(e.getMessage());
        }
        return "";
    }

    private static String getAnswer(HttpRequestBase http) throws IOException {
        CloseableHttpResponse response = httpClient.execute(http);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder answerBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            answerBuilder.append(line);
        }
        reader.close();
        response.close();
        return answerBuilder.toString();
    }

    public static String postMethod(String url, List<NameValuePair> nameValuePairs, Map<String, String> headers) throws IOException {
        try {
            HttpPost httpPost = new HttpPost(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            return getAnswer(httpPost);
        } catch (IOException e) {
            logger.debug("Exception during executing post method");
            logger.debug(e.getMessage());
        }
        return "";
    }
}
