package uk.co.datumedge.binsley;

import software.amazon.awscdk.App;

public final class BinsleyApp {
    public static void main(final String[] args) {
        App app = new App();

        new OrganizationStack(app, "Organization");
        new GitHubActionsStack(app, "GitHubActions");
        new BillingStack(app, "Billing");
        new BinsleyStack(app, "Binsley");

        app.synth();
    }
}
