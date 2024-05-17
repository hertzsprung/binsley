# 18. Restrict AWS account administrator actions in the sandbox organizational unit

Date: 2024-05-06

## Status

Accepted

## Context

Users of AWS accounts in the sandbox organizational unit (OU) should be allowed to perform any action a sandbox account,
subject to guardrails to prevent a user from:
* modifying the [organization][organization-actions] or [account][account-actions] itself
* performing IAM actions on IAM users or groups, or IAM resources associated with SSO

In particular, a user should be able to [bootstrap the CDK][cdk-bootstrap] in a sandbox account, but [PowerUserAccess] is not enough because this AWS-managed policy denies actions on IAM roles and policies.

## Decision

Apply [service control policies] to the organization root to enforce guardrails on all [IAM principals] in all [member accounts], including users and administrators of sandbox accounts.
[aws-scps-for-sandbox-and-training-accounts] provides a suitable basis for these service control policies.

## Consequences

A user can be granted [AdministratorAccess][administrator] to bootstrap the CDK in a sandbox account,
but without permission to administer the account, the organization, or IAM users or groups.

[account-actions]: https://docs.aws.amazon.com/service-authorization/latest/reference/list_awsaccountmanagement.html
[administrator]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/AdministratorAccess.html
[aws-scps-for-sandbox-and-training-accounts]: https://github.com/welldone-cloud/aws-scps-for-sandbox-and-training-accounts
[cdk-bootstrap]: https://stackoverflow.com/a/71923639/150884
[IAM principals]: https://docs.aws.amazon.com/IAM/latest/UserGuide/intro-structure.html#intro-structure-principal
[member accounts]: https://docs.aws.amazon.com/organizations/latest/userguide/orgs_getting-started_concepts.html
[organization-actions]: https://docs.aws.amazon.com/organizations/latest/APIReference/API_Operations.html
[PowerUserAccess]: https://docs.aws.amazon.com/aws-managed-policy/latest/reference/PowerUserAccess.html
[service control policies]: https://docs.aws.amazon.com/organizations/latest/userguide/orgs_manage_policies_scps.html