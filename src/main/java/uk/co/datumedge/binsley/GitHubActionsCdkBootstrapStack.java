package uk.co.datumedge.binsley;

import io.github.cdklabs.cdk.stacksets.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.cloudformation.CfnStackSet;
import software.amazon.awscdk.services.cloudformation.CfnStackSetProps;
import software.constructs.Construct;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GitHubActionsCdkBootstrapStack extends Stack {
    public GitHubActionsCdkBootstrapStack(final Construct parent, final String id, final GitHubActionsCdkBootstrapStackProps props) {
        super(parent, id, props);

        var stackSetStack = new GitHubActionsStackSetStack(this, "StackSets");

        new StackSet(this, "StackSet", StackSetProps.builder()
                .target(StackSetTarget.fromOrganizationalUnits(OrganizationsTargetOptions.builder()
                        .organizationalUnits(props.getOrganizationalUnits())
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

        try {
            new CfnStackSet(this, "CdkBootstrapStackSet", CfnStackSetProps.builder()
                    .stackSetName("CdkBootstrap")
                    .templateBody(Files.readString(Paths.get("build/cdk-bootstrap.generated.yaml"), StandardCharsets.UTF_8))
                    .autoDeployment(CfnStackSet.AutoDeploymentProperty.builder()
                            .enabled(true)
                            .retainStacksOnAccountRemoval(false)
                            .build())
                    .permissionModel("SERVICE_MANAGED")
                    .stackInstancesGroup(List.of(CfnStackSet.StackInstancesProperty.builder()
                                    .deploymentTargets(CfnStackSet.DeploymentTargetsProperty.builder()
                                            .organizationalUnitIds(props.getOrganizationalUnits())
                                            .build())
                                    .regions(List.of("eu-west-1"))
                            .build()))
                    .capabilities(List.of("CAPABILITY_NAMED_IAM"))
                    .build());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
