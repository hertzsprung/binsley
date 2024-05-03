package uk.co.datumedge.binsley;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;
import uk.co.datumedge.binsley.organization.*;
import uk.co.datumedge.binsley.workflowmetrics.WorkflowMetricsStack;

import java.util.List;

public final class BinsleyApp {
    public static void main(final String[] args) {
        new BinsleyApp();
    }

    private final App app;

    private BinsleyApp() {
        this.app = new App();

        var organization = new OrganizationStack(app, "Organization", managementAccountStackProps());

        new ResourceExplorerStack(app, "ResourceExplorer", managementAccountStackProps(), organization.nonProdOU(), organization.sandboxOU());

        new CdkBootstrapStack(app, "CdkBootstrap", managementAccountStackProps(), List.of(organization.workloadsOUId()));
        new BillingStack(app, "Billing", StackProps.builder()
                .env(Environment.builder().account(managementAccountId()).region("us-east-1").build())
                .build());

        new GitHubActionsStack(app, "GitHubActions-test", StackProps.builder()
                .env(Environment.builder().account(binsleyTestAccountId()).build())
                .build(), "test");

        var workflowMetrics = new WorkflowMetricsStack(app, "WorkflowMetrics");

        new BinsleyStack(app, "Binsley", StackProps.builder().build(), List.of(workflowMetrics.testRunnerPolicy()));
        new SsoStack(app, "SSO", managementAccountStackProps());

        Tags.of(app).add("Project", "binsley");

        app.synth();
    }

    private StackProps managementAccountStackProps() {
        return StackProps.builder()
                .env(Environment.builder().account(managementAccountId()).build())
                .build();
    }

    private String managementAccountId() {
        return (String) app.getNode().getContext("datumedge/managementAccountId");
    }

    private String binsleyTestAccountId() {
        return (String) app.getNode().getContext("datumedge/binsley/testAccountId");
    }
}
