# 20. Restrict AWS account administrator actions in the sandbox organizational unit

Date: 2024-05-06

## Status

Accepted

## Context

Users of AWS accounts in the sandbox organizational unit (OU) should be allowed to perform any action a sandbox account,
subject to guardrails to prevent a user from:
* modifying the [organization][organization-actions] or [account][account-actions] itself
* performing IAM actions, except for policies and roles

In particular, a user should be able to [bootstrap the CDK][cdk-bootstrap] in a sandbox account, but [PowerUserAccess] is not enough because this AWS-managed policy denies actions on IAM roles and policies.

## Decision

Apply a [service control policy] to the sandbox OU to enforce guardrails on all users of sandbox accounts, including administrators.

## Consequences

A user can be granted [AdministratorAccess][administrator] to bootstrap the CDK in a sandbox account,
but without permission to administer the account, the organization, or IAM users or groups.

[account-actions]: https://docs.aws.amazon.com/service-authorization/latest/reference/list_awsaccountmanagement.html
[administrator]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/AdministratorAccess.html
[cdk-bootstrap]: https://stackoverflow.com/a/71923639/150884
[organization-actions]: https://docs.aws.amazon.com/organizations/latest/APIReference/API_Operations.html
[PowerUserAccess]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/PowerUserAccess.html
[service control policy]: https://docs.aws.amazon.com/organizations/latest/userguide/orgs_manage_policies_scps.html