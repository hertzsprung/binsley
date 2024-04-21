package uk.co.datumedge.binsley.organization;

import io.github.cdklabs.cdk.stacksets.StackSetStack;
import io.github.cdklabs.cdk.stacksets.StackSetStackProps;
import software.amazon.awscdk.services.resourceexplorer2.CfnIndex;
import software.constructs.Construct;

public class ResourceExplorerViewPerOrganizationalUnitStackSetStack extends StackSetStack {
    public ResourceExplorerViewPerOrganizationalUnitStackSetStack(Construct parent, String id) {
        this(parent, id, null);
    }

    public ResourceExplorerViewPerOrganizationalUnitStackSetStack(Construct parent, String id, StackSetStackProps props) {
        super(parent, id, props);

        CfnIndex.Builder.create(this, "ResourceExplorerIndex")
                .type("AGGREGATOR")
                .build();
    }
}