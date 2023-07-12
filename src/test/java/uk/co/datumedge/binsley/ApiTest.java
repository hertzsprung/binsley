package uk.co.datumedge.binsley;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiTest {
    @Test
    void test() throws IOException, InterruptedException {
        try (SsmClient ssm = SsmClient.create()) {
            var apiBaseUri = URI.create(ssm.getParameter(b -> b.name("/Binsley/ApiBaseUrl")).parameter().value());

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(HttpRequest.newBuilder().GET().uri(apiBaseUri).build(), HttpResponse.BodyHandlers.ofString());
            assertThat(response.body()).isEqualTo("{\"message\":\"Hello world\"}");
        }
    }
}
