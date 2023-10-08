package uk.co.datumedge.binsley;

import io.github.cdklabs.cdk.stacksets.StackSetStack;
import io.github.cdklabs.cdk.stacksets.StackSetStackProps;
import software.amazon.awscdk.services.iam.*;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;


public class GitHubActionsStackSetStack extends StackSetStack {
    public GitHubActionsStackSetStack(Construct parent, String id) {
        this(parent, id, null);
    }

    public GitHubActionsStackSetStack(Construct parent, String id, StackSetStackProps props) {
        super(parent, id, props);
        // avoid OpenIdConnectProvider L2 construct because it cannot access a file asset bucket for StackSets
        // https://github.com/aws/aws-cdk/issues/20460
        // https://github.com/aws/aws-cdk/issues/21197
        var openIdConnectProvider = new CfnOIDCProvider(this, "GitHubActionsOpenIdConnectProvider", CfnOIDCProviderProps.builder()
                .url("https://token.actions.githubusercontent.com")
                .clientIdList(List.of("sts.amazonaws.com"))
                .thumbprintList(List.of("6938fd4d98bab03faadb97b34396831e3780aea1", "1c58a3a8518e8759bf075b76b750d4f2df264fcd"))
                .build());

        var oidcPrincipal = new FederatedPrincipal(
                openIdConnectProvider.getAttrArn(),
                Map.of("StringEquals", Map.of(
                        "token.actions.githubusercontent.com:sub", "repo:hertzsprung/binsley:ref:refs/heads/main",
                        "token.actions.githubusercontent.com:aud", "sts.amazonaws.com")),
                "sts:AssumeRoleWithWebIdentity"
        );

        var oidcPolicyStatement = new PolicyStatement(PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .actions(List.of("sts:AssumeRole"))
                .resources(List.of("*"))
                .conditions(Map.of("ForAnyValue:StringEquals", Map.of("iam:ResourceTag/aws-cdk:bootstrap-role", List.of(
                        "deploy",
                        "lookup",
                        "file-publishing",
                        "image-publishing"
                ))))
                .build());

        new Role(this, "GitHubActionsRole", RoleProps.builder()
                .roleName("GitHubActions")
                .assumedBy(oidcPrincipal)
                .inlinePolicies(Map.of("AssumeCdkBootstrapRoles", new PolicyDocument(PolicyDocumentProps.builder()
                        .statements(List.of(oidcPolicyStatement))
                        .build())))
                .build());
    }
}
