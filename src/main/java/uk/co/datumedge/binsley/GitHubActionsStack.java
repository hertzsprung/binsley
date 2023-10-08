package uk.co.datumedge.binsley;

import io.github.cdklabs.cdk.stacksets.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

import java.util.List;

public class GitHubActionsStack extends Stack {
    public GitHubActionsStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public GitHubActionsStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        var stackSetStack = new GitHubActionsStackSetStack(this, "StackSets");

        new StackSet(this, "StackSet", StackSetProps.builder()
                .target(StackSetTarget.fromOrganizationalUnits(OrganizationsTargetOptions.builder()
                        .organizationalUnits(List.of("ou-o930-yseapj03" /* workloads */))
                        .regions(List.of("eu-west-1"))
                        .build()))
                .template(StackSetTemplate.fromStackSetStack(stackSetStack))
                .deploymentType(DeploymentType.serviceManaged(ServiceManagedOptions.builder()
                        .autoDeployEnabled(true)
                        .autoDeployRetainStacks(false)
                        .delegatedAdmin(false)
                        .build()))
                .capabilities(List.of(Capability.NAMED_IAM))
                .build());
    }
}
