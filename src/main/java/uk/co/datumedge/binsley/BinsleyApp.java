package uk.co.datumedge.binsley;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.List;

public final class BinsleyApp {
    public static void main(final String[] args) {
        App app = new App();

        var organization = new OrganizationStack(app, "Organization");
        new GitHubActionsStack(app, "GitHubActions-nonprod", StackProps.builder()
                .env(Environment.builder().account("074782343366").build())
                .build(), "nonprod");
        new CdkBootstrapStack(app, "CdkBootstrap", List.of(organization.getWorkloadsOUId()));
        new BillingStack(app, "Billing");
        new BinsleyStack(app, "Binsley");

        app.synth();
    }
}
