package uk.co.datumedge.binsley;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2Token {
    @Test
    void obtainOAuth2Token() throws IOException, InterruptedException {
        try (CognitoIdentityProviderClient cognito = CognitoIdentityProviderClient.create();
             SsmClient ssm = SsmClient.create()) {

            var userPoolDomainBaseUrl = URI.create(ssm.getParameter(b -> b.name("/Binsley/UserPoolDomainBaseUrl")).parameter().value());

            String userPoolId = ssm.getParameter(b -> b.name("/Binsley/UserPoolId")).parameter().value();
            String userPoolClientId = ssm.getParameter(b -> b.name("/Binsley/UserPoolClientId")).parameter().value();
            String userPoolClientSecret = cognito.describeUserPoolClient(b -> b.userPoolId(userPoolId).clientId(userPoolClientId)).userPoolClient().clientSecret();

            String form = Map.of("grant_type", "client_credentials",
                    "client_id", userPoolClientId,
                    "client_secret", userPoolClientSecret,
                    "scope", "any-endpoint/something.read"
                    )
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest oauth2Request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .uri(userPoolDomainBaseUrl.resolve("./oauth2/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<String> oauth2Response = HttpClient.newHttpClient()
                    .send(oauth2Request, HttpResponse.BodyHandlers.ofString());

            assertThat(oauth2Response.statusCode()).isEqualTo(200);
        }
    }
}
