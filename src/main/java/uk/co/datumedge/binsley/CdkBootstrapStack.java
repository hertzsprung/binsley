package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudformation.CfnStackSet;
import software.amazon.awscdk.services.cloudformation.CfnStackSetProps;
import software.constructs.Construct;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Bootstraps the CDK for in all accounts belonging to the given Organizational Units, in the same region as this stack.
 */
public class CdkBootstrapStack extends Stack {
    public CdkBootstrapStack(final Construct parent, final String id, final StackProps stackProps, final List<String> organizationalUnits) {
        super(parent, id, stackProps);

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
                                            .organizationalUnitIds(List.copyOf(organizationalUnits))
                                            .build())
                                    .regions(List.of(getRegion()))
                            .build()))
                    .capabilities(List.of("CAPABILITY_NAMED_IAM"))
                    .parameters(List.of(trustedAccounts(), trustedAccountsForLookup(), cloudFormationExecutionPolicies()))
                    .build());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Map<String, String> trustedAccounts() {
        return Map.of(
                "parameterKey", "TrustedAccounts",
                "parameterValue", managementAccountId()
        );
    }

    private Map<String, String> trustedAccountsForLookup() {
        return Map.of(
                "parameterKey", "TrustedAccountsForLookup",
                "parameterValue", managementAccountId()
        );
    }

    private static Map<String, String> cloudFormationExecutionPolicies() {
        return Map.of(
                "parameterKey", "CloudFormationExecutionPolicies",
                "parameterValue", "arn:aws:iam::aws:policy/AdministratorAccess"
        );
    }

    private String managementAccountId() {
        return (String) this.getNode().getContext("datumedge/managementAccountId");
    }
}
