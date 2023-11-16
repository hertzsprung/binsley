package uk.co.datumedge.binsley;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.List;

public final class BinsleyApp {
    public static void main(final String[] args) {
        new BinsleyApp();
    }

    private final App app;

    private BinsleyApp() {
        this.app = new App();

        var organization = new OrganizationStack(app, "Organization", managementAccountStackProps());
        new CdkBootstrapStack(app, "CdkBootstrap", managementAccountStackProps(), List.of(organization.getWorkloadsOUId()));
        new BillingStack(app, "Billing", StackProps.builder()
                .env(Environment.builder().account(managementAccountId()).region("us-east-1").build())
                .build());

        new GitHubActionsStack(app, "GitHubActions-nonprod", StackProps.builder()
                .env(Environment.builder().account(nonprodAccountId()).build())
                .build(), "nonprod");

        new BinsleyStack(app, "Binsley");

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

    private String nonprodAccountId() {
        return (String) app.getNode().getContext("datumedge/nonprodAccountId");
    }
}
