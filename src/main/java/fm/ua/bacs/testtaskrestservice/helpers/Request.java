package fm.ua.bacs.testtaskrestservice.helpers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Request {

    public HttpResponse makeRequest(String filename, int status) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost("http://yoururl");
            StringEntity params = new StringEntity("details={\"filename\":\"" + filename + "\",\"status\":\"" + status + "\"} ");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
