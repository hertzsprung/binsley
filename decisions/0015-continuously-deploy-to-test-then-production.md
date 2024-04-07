# 15. Continuously deploy to test then production

Date: 2024-04-07

## Status

Accepted

## Context

[Committing to the main branch triggers a workflow to build, test, and deploy the change][adr-0013].
An approach is sought to ensure the correctness of changes deployed to production. 

## Decision

Two environments, _test_ and _production_, are always operational.
Committing to the main branch triggers a GitHub Actions workflow that:
1. compiles code and runs tests within the GitHub Actions runner
2. deploys the change to the test environment and runs tests against the test environment
3. deploys the change to the production environment and runs tests against the production environment

## Consequences

In addition to the consequences identified in [ADR 13][adr-0013]:
* Identical code and infrastructure are deployed to test and production environments (except while a workflow is deploying to an environments).
* When the system grows large, the GitHub Actions workflow can be configured to build, test, and deploy selected subsystems, [triggered by changes to particular directories][github-actions-on-push-paths] in the [monorepo][adr-0014].

[adr-0013]: ./0013-adopt-trunk-based-development.md
[adr-0014]: ./0014-store-in-house-sources-in-a-monorepo.md
[github-actions-on-push-paths]: https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#running-your-workflow-only-when-a-push-affects-specific-files