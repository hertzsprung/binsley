package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.cognito.*;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.SessionTagsPrincipal;
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


        IRole githubRole = Role.fromRoleName(this, "GitHubRole", "GitHubActions");

        UserPool userPool = UserPool.Builder.create(this, "UserPool").build();

        UserPoolResourceServer userPoolResourceServer = userPool.addResourceServer("any-endpoint", UserPoolResourceServerOptions.builder()
                .identifier("any-endpoint")
                .scopes(List.of(ResourceServerScope.Builder.create()
                        .scopeName("something.read")
                        .scopeDescription("read something")
                        .build())).build());

        UserPoolClient userPoolClient = userPool.addClient("Client", UserPoolClientOptions.builder()
                .generateSecret(true)
                .oAuth(OAuthSettings.builder()
                        .scopes(List.of(OAuthScope.custom("any-endpoint/something.read")))
                        .flows(OAuthFlows.builder().clientCredentials(true).build())
                        .build())
                .authFlows(AuthFlow.builder().userSrp(true).build())
                .preventUserExistenceErrors(true)
                .build());

        // needed because the OAuthScope references the ResourceServerScope
        userPoolClient.getNode().addDependency(userPoolResourceServer);

        UserPoolDomain userPoolDomain = userPool.addDomain("Domain", UserPoolDomainOptions.builder()
                .cognitoDomain(CognitoDomainOptions.builder().domainPrefix("binsley").build())
                .build());

        Authorizer authorizer = CognitoUserPoolsAuthorizer.Builder.create(this, "ApiCognitoAuthorizer")
                .cognitoUserPools(List.of(userPool))
                .build();

        LambdaRestApi api = LambdaRestApi.Builder.create(this, "Api")
                .defaultMethodOptions(MethodOptions.builder()
                        .authorizationType(AuthorizationType.COGNITO)
                        .authorizer(authorizer)
                        .authorizationScopes(List.of("any-endpoint/something.read"))
                        .build())
                .handler(apiLambda)
                .endpointTypes(List.of(EndpointType.REGIONAL))
                .deployOptions(StageOptions.builder().stageName("v1").build())
                .build();

        StringParameter apiBaseUrl = StringParameter.Builder.create(this, "ApiBaseUrl")
                .parameterName("/Binsley/ApiBaseUrl")
                .stringValue(api.getUrl())
                .build();

        StringParameter userPoolId = StringParameter.Builder.create(this, "UserPoolId")
                .parameterName("/Binsley/UserPoolId")
                .stringValue(userPool.getUserPoolId())
                .build();

        StringParameter userPoolClientId = StringParameter.Builder.create(this, "UserPoolClientId")
                .parameterName("/Binsley/UserPoolClientId")
                .stringValue(userPoolClient.getUserPoolClientId())
                .build();

        StringParameter userPoolDomainBaseUrl = StringParameter.Builder.create(this, "UserPoolDomainBaseUrl")
                .parameterName("/Binsley/UserPoolDomainBaseUrl")
                .stringValue(userPoolDomain.baseUrl())
                .build();

        IRole testRunner = Role.Builder.create(this, "TestRunner")
                .roleName("BinsleyTestRunner")
                .assumedBy(new SessionTagsPrincipal(githubRole))
                .build();

        apiBaseUrl.grantRead(testRunner);
        userPool.grant(testRunner, "cognito-idp:DescribeUserPoolClient");
        userPoolId.grantRead(testRunner);
        userPoolClientId.grantRead(testRunner);
        userPoolDomainBaseUrl.grantRead(testRunner);
    }
}
