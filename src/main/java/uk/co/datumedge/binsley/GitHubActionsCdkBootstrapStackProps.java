package uk.co.datumedge.binsley;

import software.amazon.awscdk.StackProps;

import java.util.List;
import java.util.Objects;

public class GitHubActionsCdkBootstrapStackProps implements StackProps {
    private final List<String> organizationalUnits;

    static Builder builder() {
        return new Builder();
    }

    private GitHubActionsCdkBootstrapStackProps(Builder builder) {
        this.organizationalUnits = Objects.requireNonNull(builder.organizationalUnits, "organizationalUnits is required");
    }

    public List<String> getOrganizationalUnits() {
        return List.copyOf(organizationalUnits);
    }

    public static final class Builder {
        private List<String> organizationalUnits;

        public Builder organizationalUnits(List<String> organizationalUnits) {
            this.organizationalUnits = organizationalUnits;
            return this;
        }

        public GitHubActionsCdkBootstrapStackProps build() {
            return new GitHubActionsCdkBootstrapStackProps(this);
        }
    }
}