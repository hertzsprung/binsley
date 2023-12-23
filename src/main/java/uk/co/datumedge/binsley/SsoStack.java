package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sso.CfnAssignment;
import software.amazon.awscdk.services.sso.CfnPermissionSet;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

public class SsoStack extends Stack {
    public SsoStack(final Construct parent, final String id, final StackProps stackProps) {
        super(parent, id, stackProps);

        var binsleyTestReadOnlyAccessPermissionSet = CfnPermissionSet.Builder.create(this, "BinsleyTestReadOnlyAccessPermissionSet")
                .instanceArn(ssoInstanceArn())
                .name("BinsleyTestReadOnlyAccess")
                .description("Provides read-only access, with the ability to assume the BinsleyTestRunner role")
                .managedPolicies(List.of("arn:aws:iam::aws:policy/ReadOnlyAccess"))
                .customerManagedPolicyReferences(List.of(Map.of("name", BinsleyStack.ASSUME_BINSLEY_TEST_RUNNER_ROLE_NAME)))
                .build();

        CfnAssignment.Builder.create(this, "BinsleyTestReadOnlyAccessAssignment")
                .instanceArn(ssoInstanceArn())
                .permissionSetArn(binsleyTestReadOnlyAccessPermissionSet.getAttrPermissionSetArn())
                .principalType("GROUP")
                .principalId("12b5c4a4-20e1-701d-bf20-b287b27f743c")
                .targetType("AWS_ACCOUNT")
                .targetId(binsleyTestAccountId())
                .build();
    }

    private String ssoInstanceArn() {
        return (String) getNode().getContext("datumedge/ssoInstanceArn");
    }

    private String binsleyTestAccountId() {
        return (String) getNode().getContext("datumedge/binsley/testAccountId");
    }
}
