package uk.co.datumedge.binsley.workflowmetrics;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class WorkflowMetricsStack extends Stack {
    public WorkflowMetricsStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public WorkflowMetricsStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        Queue.Builder.create(this, "WorkflowMetricsTestQueue")
                .build();
    }
}
