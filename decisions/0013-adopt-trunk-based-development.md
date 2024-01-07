# 13. Adopt trunk-based development

Date: 2024-01-06

## Status

Accepted

## Context

A git workflow is sought that facilitates continuous integration and continuous delivery.
With only one committer, there is little need for team coordination, collaboration, or code review.

## Decision

Adopt [trunk-based development][trunk-based-development], with all changes [committed directly to the main (trunk) branch][direct-to-trunk].
A commit to the main branch triggers a [GitHub Actions workflow][github-actions-workflow] to build, test, and deploy the change.
No other branches are needed for development or release.

## Consequences

* Comprehensive automated testing is needed to verify changes committed to the main branch.
* [Synthetic monitoring][cloudwatch-synthetic-monitoring] is needed to verify changes deployed to production or non-production environments.
* Automated procedures are needed to rollback bad changes committed to the main branch,
or to rollback bad changes deployed to an environment.
* With only one committer, the risk of conflicting concurrent changes is negligible; [preventing concurrent GitHub Actions workflow runs][github-actions-concurrent-workflows] alleviates this risk

[cloudwatch-synthetic-monitoring]: https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/CloudWatch_Synthetics_Canaries.html
[direct-to-trunk]: https://trunkbaseddevelopment.com/committing-straight-to-the-trunk/
[github-actions-concurrent-workflows]: https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#concurrency
[github-actions-workflow]: https://docs.github.com/en/actions/using-workflows/about-workflows
[trunk-based-development]: https://trunkbaseddevelopment.com