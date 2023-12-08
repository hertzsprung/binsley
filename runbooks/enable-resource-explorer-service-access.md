Based on [Using AWS Organizations with other AWS services](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_integrate_services.html#orgs_how-to-enable-disable-trusted-access):

```shell
aws organizations enable-aws-service-access \
  --service-principal resource-explorer-2.amazonaws.com
```