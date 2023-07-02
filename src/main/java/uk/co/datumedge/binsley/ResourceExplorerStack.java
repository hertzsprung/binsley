package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.resourceexplorer2.CfnDefaultViewAssociation;
import software.amazon.awscdk.services.resourceexplorer2.CfnIndex;
import software.amazon.awscdk.services.resourceexplorer2.CfnView;
import software.constructs.Construct;

public class ResourceExplorerStack extends Stack {
    public ResourceExplorerStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public ResourceExplorerStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        CfnIndex index = CfnIndex.Builder.create(this, "ResourceExplorerIndex")
                .type("LOCAL")
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