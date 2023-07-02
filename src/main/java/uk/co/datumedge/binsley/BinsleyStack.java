package uk.co.datumedge.binsley;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class BinsleyStack extends Stack {
    public BinsleyStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public BinsleyStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        Function helloWorldLambda = Function.Builder.create(this, "GetStartedLambdaProxyIntegration")
                .runtime(Runtime.NODEJS_18_X)
                .handler("index.handler")
                .code(Code.fromAsset("src/main/resources/GetStartedLambdaProxyIntegration/"))
                .build();

        LambdaRestApi.Builder.create(this, "LambdaSimpleProxy")
                .handler(helloWorldLambda)
                .build();
    }
}
