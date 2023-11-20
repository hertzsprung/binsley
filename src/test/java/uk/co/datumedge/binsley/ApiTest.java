package uk.co.datumedge.binsley;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.Role;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiTest {
    @Test
    void test() throws IOException, InterruptedException {
        try (IamClient iam = IamClient.builder().build();
             StsClient sts = StsClient.create()) {
            Role testRunnerRole = iam.getRole(b -> b.roleName("BinsleyTestRunner")).role();
            Credentials credentials = sts.assumeRole(b -> b.roleArn(testRunnerRole.arn()).roleSessionName("BinsleyTestRun")).credentials();
            var sessionCredentials = AwsSessionCredentials.create(credentials.accessKeyId(), credentials.secretAccessKey(), credentials.sessionToken());
            var credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);

            try (CognitoIdentityProviderClient cognito = CognitoIdentityProviderClient.builder().credentialsProvider(credentialsProvider).build();
                 SsmClient ssm = SsmClient.builder().credentialsProvider(credentialsProvider).build()) {
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

                HttpResponse<String> response;
                try (HttpClient httpClient = HttpClient.newHttpClient()) {
                    HttpResponse<InputStream> oauth2Response = httpClient.send(oauth2Request, HttpResponse.BodyHandlers.ofInputStream());

                    assertThat(oauth2Response.statusCode()).isEqualTo(200);
                    String accessToken = new ObjectMapper().readTree(oauth2Response.body()).get("access_token").asText();

                    var apiBaseUri = URI.create(ssm.getParameter(b -> b.name("/Binsley/ApiBaseUrl")).parameter().value());

                    response = httpClient.send(HttpRequest.newBuilder().GET().header("Authorization", accessToken).uri(apiBaseUri).build(), HttpResponse.BodyHandlers.ofString());
                }
                assertThat(response.body()).isEqualTo("{\"message\":\"Hello world\"}");
            }
        }
    }
}
