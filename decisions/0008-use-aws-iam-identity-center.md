# 8. Use AWS IAM Identity Center

Date: 2023-06-16

## Status

Accepted

## Context

Secure user access to AWS with temporary credentials.

## Decision

Manage AWS identities with [AWS IAM Identity Center][iam-identity-center], 
following [AWS IAM security best practices][iam-best-practices].

## Consequences

Allows AWS CLI access without long-lived access keys.
Requires [AWS Organizations][organizations] to be enabled.

[iam-best-practices]: https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#bp-users-federation-idp
[iam-identity-center]: https://aws.amazon.com/iam/identity-center/
[organizations]: https://docs.aws.amazon.com/organizations/latest/userguide/orgs_introduction.html
