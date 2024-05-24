package uk.co.datumedge.binsley.workflowmetrics;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricDataResponse;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.time.Instant;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class WorkflowMetricsTest {
    @Test
    void sendsCustomCloudWatchMetricOnGitHubWorkflowStatusEvent() {
        try (StsClient sts = StsClient.create()) {
            String accountId = sts.getCallerIdentity().account();
            Credentials credentials = sts.assumeRole(b -> b
                    .roleArn("arn:aws:iam::%s:role/BinsleyTestRunner".formatted(accountId))
                    .roleSessionName("BinsleyTestRun")).credentials();
            var sessionCredentials = AwsSessionCredentials.create(credentials.accessKeyId(), credentials.secretAccessKey(), credentials.sessionToken());
            var credentialsProvider = StaticCredentialsProvider.create(sessionCredentials);

            try (CloudWatchClient cloudWatch = CloudWatchClient.builder().credentialsProvider(credentialsProvider).build()) {
                long workflowCompletionTime = Instant.now().toEpochMilli();

                deliverGitHubWorkflowStatusEvent(cloudWatch, workflowCompletionTime);

                await()
                        .atMost(60, SECONDS)
                        .pollInterval(1, SECONDS)
                        .untilAsserted(() -> assertThat(cloudwatchMetrics(cloudWatch)).isEqualTo(workflowCompletionTime));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void deliverGitHubWorkflowStatusEvent(CloudWatchClient cloudWatch, long workflowCompletionTime) {
        cloudWatch.putMetricData(b -> b
                .namespace("DatumEdge/Binsley/Test/Workflow")
                .metricData(m -> m
                    .metricName("CompletionTime")
                        .value((double) workflowCompletionTime)
                        .unit(StandardUnit.MILLISECONDS)
                ));
    }

    @SuppressWarnings("unchecked")
    private Double cloudwatchMetrics(CloudWatchClient cloudWatch) {
        GetMetricDataResponse response = cloudWatch.getMetricData(b -> b
                .maxDatapoints(1)
                .startTime(Instant.now().minusSeconds(120))
                .metricDataQueries(q -> q
                        .id("workflowCompletionTime")
                        .metricStat(s -> s.metric(m -> m
                                .namespace("DatumEdge/Binsley/Test/Workflow")
                                .metricName("CompletionTime"))
                        .stat("Maximum")
                        .period(120 /* seconds */)))
                .endTime(Instant.now())
        );

        return response.metricDataResults()
                .stream()
                .findAny()
                .flatMap(result -> result.values().stream().findAny())
                .orElse(null);
    }
}
