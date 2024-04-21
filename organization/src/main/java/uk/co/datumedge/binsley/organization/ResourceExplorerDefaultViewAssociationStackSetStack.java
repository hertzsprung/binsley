package uk.co.datumedge.binsley.organization;

import io.github.cdklabs.cdk.stacksets.StackSetStack;
import io.github.cdklabs.cdk.stacksets.StackSetStackProps;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.services.resourceexplorer2.CfnDefaultViewAssociation;
import software.constructs.Construct;

/**
 * Declares the Resource Explorer default view of the {@code defaultViewArn} CloudFormation parameter,
 * which must be specified as a <a href="https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/stackinstances-override.html">parameter override</a>.
 */
public class ResourceExplorerDefaultViewAssociationStackSetStack extends StackSetStack {
    public ResourceExplorerDefaultViewAssociationStackSetStack(Construct parent, String id, StackSetStackProps props) {
        super(parent, id, props);

        CfnParameter defaultViewArn = CfnParameter.Builder.create(this, "defaultViewArn")
                .type("String")
                .defaultValue("<defaultViewArnMissing>")
                .build();

        CfnDefaultViewAssociation.Builder.create(this, "ResourceExplorerDefaultViewAssociation")
                .viewArn(defaultViewArn.getValueAsString())
                .build();
    }
}