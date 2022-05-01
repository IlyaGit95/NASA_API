import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String REMOTE_SERVICE_URI =
            "https://api.nasa.gov/planetary/apod?api_key=nvz8qCXGsqXx9SaSlX65JOJ8dbKg0IA8eL6iOzBj";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);
            System.out.println(nasa);
            HttpGet request2 = new HttpGet(nasa.getUrl());
            CloseableHttpResponse response2 = httpClient.execute(request2);
            String[] array = nasa.getUrl().split("/");
            String fileName = array[6];
            HttpEntity entity = response2.getEntity();
            if (entity != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                entity.writeTo(fileOutputStream);
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
