package uk.co.datumedge.binsley;

import com.pepperize.cdk.organizations.OrganizationalUnit;
import io.github.cdklabs.cdk.stacksets.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

import java.util.List;

public class ResourceExplorerStack extends Stack {
    public ResourceExplorerStack(final Construct parent, final String id, final StackProps props, final OrganizationalUnit nonProdOU) {
        super(parent, id, props);

        var stackSetStack = new ResourceExplorerStackSetStack(this, "ResourceExplorerStackSetStack");

        new StackSet(this, "StackSet", StackSetProps.builder()
                .target(StackSetTarget.fromOrganizationalUnits(OrganizationsTargetOptions.builder()
                        .organizationalUnits(List.of(nonProdOU.getOrganizationalUnitId()))
                        .regions(List.of(getRegion()))
                        .build()))
                .template(StackSetTemplate.fromStackSetStack(stackSetStack))
                .deploymentType(DeploymentType.serviceManaged(ServiceManagedOptions.builder()
                        .autoDeployEnabled(true)
                        .autoDeployRetainStacks(false)
                        .delegatedAdmin(false)
                        .build()))
                .build());
    }
}