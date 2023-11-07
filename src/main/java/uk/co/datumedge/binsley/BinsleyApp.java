package uk.co.datumedge.binsley;

import software.amazon.awscdk.App;

import java.util.List;

public final class BinsleyApp {
    public static void main(final String[] args) {
        App app = new App();

        var organization = new OrganizationStack(app, "Organization");
        new CdkBootstrapStack(app, "CdkBootstrap", CdkBootstrapStackProps.builder()
                .organizationalUnits(List.of(organization.getWorkloadsOUId()))
                .build());
        new BillingStack(app, "Billing");
        new BinsleyStack(app, "Binsley");

        app.synth();
    }
}
