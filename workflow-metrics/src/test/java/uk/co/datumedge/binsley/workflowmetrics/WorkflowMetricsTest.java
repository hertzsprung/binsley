package uk.co.datumedge.binsley.workflowmetrics;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricDataResponse;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class WorkflowMetricsTest {
    @Test
    void sendsCustomCloudWatchMetricOnGitHubWorkflowStatusEvent() {
        long workflowCompletionTime = Instant.now().toEpochMilli();

        deliverGitHubWorkflowStatusEvent(workflowCompletionTime);

        await().untilAsserted(() -> assertThat(cloudwatchMetrics()).isEqualTo(workflowCompletionTime));
    }

    @SuppressWarnings("unchecked")
    private void deliverGitHubWorkflowStatusEvent(long workflowCompletionTime) {
        try (CloudWatchClient cloudWatch = CloudWatchClient.create()) {
            cloudWatch.putMetricData(b -> b
                    .namespace("DatumEdge/Binsley/Test/Workflow")
                    .metricData(m -> m
                        .metricName("CompletionTime")
                            .value((double) workflowCompletionTime)
                            .unit(StandardUnit.MILLISECONDS)
                    ));
        }
    }

    @SuppressWarnings("unchecked")
    private Double cloudwatchMetrics() {
        try (CloudWatchClient cloudWatch = CloudWatchClient.create()) {
            GetMetricDataResponse response = cloudWatch.getMetricData(b -> b
                    .maxDatapoints(1)
                    .startTime(Instant.now().minusSeconds(60))
                    .metricDataQueries(q -> q
                            .id("workflowCompletionTime")
                            .metricStat(s -> s.metric(m -> m
                                    .namespace("DatumEdge/Binsley/Test/Workflow")
                                    .metricName("CompletionTime"))
                            .stat("Maximum")
                            .period(60 /* seconds */)))
                    .endTime(Instant.now())
            );

            return response.metricDataResults()
                    .stream()
                    .findAny()
                    .flatMap(result -> result.values().stream().findAny())
                    .orElse(null);
        }
    }
}
