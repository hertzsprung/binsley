package uk.co.datumedge.binsley.workflowmetrics;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.Policy;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;

import java.util.List;

public class WorkflowMetricsStack extends Stack {
    private final Policy testRunnerPolicy;

    public WorkflowMetricsStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public WorkflowMetricsStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        testRunnerPolicy = Policy.Builder.create(this, "WorkflowMetrics/BinsleyTestRunner")
                .policyName("WorkflowMetrics")
                .statements(List.of(PolicyStatement.Builder.create()
                                .actions(List.of("cloudwatch:GetMetricData", "cloudwatch:PutMetricData"))
                                .resources(List.of("*"))
                        .build()))
                .build();
    }

    public Policy testRunnerPolicy() {
        return testRunnerPolicy;
    }
}
