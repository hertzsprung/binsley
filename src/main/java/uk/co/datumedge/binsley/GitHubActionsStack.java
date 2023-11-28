package uk.co.datumedge.binsley;

import io.github.cdklabs.cdkpipelines.github.GitHubActionRole;
import io.github.cdklabs.cdkpipelines.github.GitHubActionRoleProps;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

import java.util.List;
import java.util.Objects;

public class GitHubActionsStack extends Stack {
    public GitHubActionsStack(final Construct parent, final String id, final StackProps props, final String gitHubEnvironment) {
        super(parent, id, props);

        var role = new GitHubActionRole(this, "GitHubActionsRole", GitHubActionRoleProps.builder()
                .roleName("GitHubActions")
                .subjectClaims(List.of("repo:hertzsprung/binsley:environment:" + gitHubEnvironment))
                .build());

        Objects.requireNonNull(((Role) role.getRole()))
                .addToPolicy(PolicyStatement.Builder.create()
                        .actions(List.of("sts:AssumeRole"))
                        .resources(List.of("arn:aws:iam::" + getAccount() + ":role/BinsleyTestRunner"))
                        .build());
    }
}
