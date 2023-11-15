package uk.co.datumedge.binsley;

import io.github.cdklabs.cdkpipelines.github.GitHubActionRole;
import io.github.cdklabs.cdkpipelines.github.GitHubActionRoleProps;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

import java.util.List;

public class GitHubActionsStack extends Stack {
    public GitHubActionsStack(final Construct parent, final String id, final StackProps props, final String gitHubEnvironment) {
        super(parent, id, props);

        new GitHubActionRole(this, "GitHubActionsRole", GitHubActionRoleProps.builder()
                .roleName("GitHubActions")
                .subjectClaims(List.of("repo:hertzsprung/binsley:environment:" + gitHubEnvironment))
                .build());
    }
}
