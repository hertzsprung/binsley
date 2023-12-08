package uk.co.datumedge.binsley;

import io.github.cdklabs.cdk.stacksets.StackSetStack;
import io.github.cdklabs.cdk.stacksets.StackSetStackProps;
import software.amazon.awscdk.services.resourceexplorer2.CfnDefaultViewAssociation;
import software.amazon.awscdk.services.resourceexplorer2.CfnIndex;
import software.amazon.awscdk.services.resourceexplorer2.CfnView;
import software.constructs.Construct;

public class ResourceExplorerStackSetStack extends StackSetStack {
    public ResourceExplorerStackSetStack(Construct parent, String id) {
        this(parent, id, null);
    }

    public ResourceExplorerStackSetStack(Construct parent, String id, StackSetStackProps props) {
        super(parent, id, props);

        CfnIndex index = CfnIndex.Builder.create(this, "ResourceExplorerIndex")
                .type("AGGREGATOR")
                .build();

        CfnView view = CfnView.Builder.create(this, "ResourceExplorerView")
                .viewName("Default")
                .build();

        view.getNode().addDependency(index);

        CfnDefaultViewAssociation.Builder.create(this, "ResourceExplorerDefaultViewAssociation")
                .viewArn(view.getAttrViewArn())
                .build();
    }
}