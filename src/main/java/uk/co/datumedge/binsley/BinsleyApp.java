package uk.co.datumedge.binsley;

import software.amazon.awscdk.App;

public final class BinsleyApp {
    public static void main(final String[] args) {
        App app = new App();

        new BinsleyStack(app, "CdkStack");

        app.synth();
    }
}
