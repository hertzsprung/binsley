Based on [AWS CDK documentation][cdk-docs].

```
curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash - # https://askubuntu.com/a/548776
sudo apt-get install -y nodejs
npm view aws-cdk version # latest
npm install -g aws-cdk@2.84.0
cdk init --language java # requires mvn
```

# Allow GitHub Actions to assume CDK roles

Based on "[What IAM permissions are needed to use CDK Deploy?][so-cdk-iam]"

```
aws iam put-role-policy \
  --role-name GitHubAction-AssumeRoleWithAction \
  --policy-name AssumeCdkRolesPolicy \
  --policy-document file://cdk-install.policy.json
```

# Permissions boundary

Based on [Secure CDK deployments with IAM permission boundaries][permissions-boundary].

```
aws iam create-policy \
  --policy-name cdk-boundary-policy \
  --policy-document file://cdk-install.permissions-boundary.policy.json \
  --description "CDK Bootstrap boundary permissions policy"

cdk bootstrap --custom-permissions-boundary cdk-boundary-policy
```

[cdk-docs]: https://docs.aws.amazon.com/cdk/v2/guide/cli.html
[permissions-boundary]: https://aws.amazon.com/blogs/devops/secure-cdk-deployments-with-iam-permission-boundaries/
[so-cdk-iam]: https://stackoverflow.com/q/57118082/150884