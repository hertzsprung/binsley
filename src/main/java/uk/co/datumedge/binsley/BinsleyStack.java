package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.ssm.StringParameter;
import software.constructs.Construct;

import java.util.List;

public class BinsleyStack extends Stack {
    public BinsleyStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public BinsleyStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        Function apiLambda = Function.Builder.create(this, "ApiLambda")
                .runtime(Runtime.NODEJS_18_X)
                .handler("index.handler")
                .code(Code.fromAsset("src/main/resources/GetStartedLambdaProxyIntegration/"))
                .build();

        LambdaRestApi api = LambdaRestApi.Builder.create(this, "Api")
                .handler(apiLambda)
                .endpointTypes(List.of(EndpointType.REGIONAL))
                .deployOptions(StageOptions.builder().stageName("v1").build())
                .build();

        StringParameter apiBaseUrl = StringParameter.Builder.create(this, "ApiBaseUrl")
                .parameterName("/binsley/ApiBaseUrl")
                .stringValue(api.getUrl())
                .build();

        IRole githubRole = Role.fromRoleName(this, "GitHubRole", "GitHubAction-AssumeRoleWithAction");

        IRole testRunner = Role.Builder.create(this, "TestRunner")
                .roleName("BinsleyTestRunner")
                .assumedBy(githubRole)
                .build();

        apiBaseUrl.grantRead(testRunner);
    }
}
