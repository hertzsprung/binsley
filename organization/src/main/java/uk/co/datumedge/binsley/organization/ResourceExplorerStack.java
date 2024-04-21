package uk.co.datumedge.binsley.organization;

import io.github.cdklabs.cdk.stacksets.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ram.CfnResourceShare;
import software.amazon.awscdk.services.resourceexplorer2.CfnView;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

public class ResourceExplorerStack extends Stack {
    public ResourceExplorerStack(final Construct parent, final String id, final StackProps props, final OrganizationalUnit nonProdOU, final OrganizationalUnit sandboxOU) {
        super(parent, id, props);

        var viewPerAccountStackSet = new ResourceExplorerViewPerAccountStackSetStack(this, "ResourceExplorerViewPerStackSetStack");
        var viewPerOUStackSet = new ResourceExplorerViewPerOrganizationalUnitStackSetStack(this, "ResourceExplorerViewPerOUStackSet");

        new StackSet(this, "NonProdOUStackSet", StackSetProps.builder()
                .target(StackSetTarget.fromOrganizationalUnits(OrganizationsTargetOptions.builder()
                        .organizationalUnits(List.of(nonProdOU.id()))
                        .regions(List.of(getRegion()))
                        .build()))
                .template(StackSetTemplate.fromStackSetStack(viewPerAccountStackSet))
                .deploymentType(DeploymentType.serviceManaged(ServiceManagedOptions.builder()
                        .autoDeployEnabled(true)
                        .autoDeployRetainStacks(false)
                        .delegatedAdmin(false)
                        .build()))
                .build());

        StackSet sandboxStackSet = new StackSet(this, "SandboxStackSet", StackSetProps.builder()
                .target(StackSetTarget.fromOrganizationalUnits(OrganizationsTargetOptions.builder()
                        .organizationalUnits(List.of(sandboxOU.id()))
                        .regions(List.of(getRegion()))
                        .build()))
                .template(StackSetTemplate.fromStackSetStack(viewPerOUStackSet))
                .deploymentType(DeploymentType.serviceManaged(ServiceManagedOptions.builder()
                        .autoDeployEnabled(true)
                        .autoDeployRetainStacks(false)
                        .delegatedAdmin(false)
                        .build()))
                .build());

        CfnView sandboxOUView = CfnView.Builder.create(this, "ResourceExplorerSandboxOUView")
                .viewName("SandboxOU")
                .scope(sandboxOU.arn())
                .build();

        sandboxOUView.getNode().addDependency(sandboxStackSet);

        CfnResourceShare sandboxOUResourceShare = CfnResourceShare.Builder.create(this, "SandboxOUResourceExplorerShare")
                .name("ResourceExplorerSandboxOUShare")
                .resourceArns(List.of(sandboxOUView.getAttrViewArn()))
                .permissionArns(List.of("arn:aws:ram::aws:permission/AWSRAMPermissionResourceExplorerViews"))
                .principals(List.of(sandboxOU.arn()))
                .allowExternalPrincipals(false)
                .build();

        StackSet sandboxDefaultViewAssociationStackSet = new StackSet(this, "SandboxDefaultViewAssociationStackSet", StackSetProps.builder()
                .target(StackSetTarget.fromOrganizationalUnits(OrganizationsTargetOptions.builder()
                        .organizationalUnits(List.of(sandboxOU.id()))
                        .regions(List.of(getRegion()))
                        .parameterOverrides(Map.of("defaultViewArn", sandboxOUView.getAttrViewArn()))
                        .build()))
                .template(StackSetTemplate.fromStackSetStack(new ResourceExplorerDefaultViewAssociationStackSetStack(this, "ResourceExplorerDefaultViewAssociationStackSet", null)))
                .deploymentType(DeploymentType.serviceManaged(ServiceManagedOptions.builder()
                        .autoDeployEnabled(true)
                        .autoDeployRetainStacks(false)
                        .delegatedAdmin(false)
                        .build()))
                .build());

        sandboxDefaultViewAssociationStackSet.getNode().addDependency(sandboxOUResourceShare);
    }
}