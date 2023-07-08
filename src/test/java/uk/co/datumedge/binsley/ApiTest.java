package uk.co.datumedge.binsley;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ssm.SsmClient;

public class ApiTest {
    @Test
    void test() {
        try (SsmClient ssm = SsmClient.create()) {
            String apiBaseUrl = ssm.getParameter(b -> b.name("/Binsley/ApiBaseUrl")).parameter().value();
            System.out.println(apiBaseUrl);
        }
    }
}
