package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.*;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

public class StackSetsStack extends Stack {
    public StackSetsStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public StackSetsStack(Construct parent, String id, StackProps props) {
        super(parent, id, props);

        var openIdConnectProvider = new OpenIdConnectProvider(this, "GitHubActionsOpenIdConnectProvider", OpenIdConnectProviderProps.builder()
                .url("https://token.actions.githubusercontent.com")
                .clientIds(List.of("sts.amazonaws.com"))
                .build());

        var oidcPrincipal = new FederatedPrincipal(
                openIdConnectProvider.getOpenIdConnectProviderArn(),
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
