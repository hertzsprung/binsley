Based on [AWS blog: Use IAM roles to connect GitHub Actions to actions in AWS][aws-github-actions].

```
aws iam create-open-id-connect-provider \
  --url "https://token.actions.githubusercontent.com" \
  --thumbprint-list "6938fd4d98bab03faadb97b34396831e3780aea1" \
  --client-id-list 'sts.amazonaws.com'

aws iam create-role \
  --role-name GitHubAction-AssumeRoleWithAction \
  --assume-role-policy-document file://github-actions-oidc.trust-policy.json
```

[aws-github-actions]: https://aws.amazon.com/blogs/security/use-iam-roles-to-connect-github-actions-to-actions-in-aws/
