Based on [AWS blog: Use IAM roles to connect GitHub Actions to actions in AWS][aws-github-actions].

```sh
aws iam create-open-id-connect-provider \
  --url "https://token.actions.githubusercontent.com" \
  --thumbprint-list "6938fd4d98bab03faadb97b34396831e3780aea1" \
  --client-id-list 'sts.amazonaws.com'

aws iam create-role \
  --role-name GitHubAction-AssumeRoleWithAction \
  --assume-role-policy-document file://github-actions-oidc.trust-policy.json
```

For multi-account:
```sh
aws cloudformation activate-organizations-access

export template_body=$(cat <<EOF
{
  "Resources": {
    "GitHubOIDCProvider": {
      "Type" : "AWS::IAM::OIDCProvider",
      "Properties" : {
          "ClientIdList" : [ "sts.amazonaws.com" ],
          "ThumbprintList" : [ "6938fd4d98bab03faadb97b34396831e3780aea1" ],
          "Url" : "https://token.actions.githubusercontent.com"
        }
     }
   }
}
EOF
)

```sh
aws cloudformation create-stack-set \
  --stack-set-name "GitHubActionsOICD" \
  --permission-model SERVICE_MANAGED \
  --template-body "$template_body" \
  --auto-deployment Enabled=true,RetainStacksOnAccountRemoval=false

aws cloudformation create-stack-instances \
  --stack-set-name "GitHubActionsOICD" \
  --deployment-targets OrganizationalUnitIds=ou-o930-yseapj03 \
  --regions eu-west-1
```

[aws-github-actions]: https://aws.amazon.com/blogs/security/use-iam-roles-to-connect-github-actions-to-actions-in-aws/
