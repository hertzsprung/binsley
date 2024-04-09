# 16. Automatically rollback on deployment failure

Date: 2024-04-08

## Status

Accepted

## Context

[Committing to the main branch triggers a workflow][adr-0013]
to [build, test, and deploy to the test environment, then the production environment][adr-0015].
An approach is sought to ensure that—even if any workflow step fails—[the monorepo][adr-0014], test environment and production environment remain consistent.

## Decision

If a workflow fails, automatically rollback the commit, and any deployment to test or production environments.

### Git commit rollback

The commit history on the main branch should reflect the changes successfully deployed to production.
So when a change triggers a workflow failure, remove it from the commit history on the main branch,
and moved to a new branch:

```shell
branch_name="rollback-$(printf '%(%Y-%m-%d_%H.%M.%S)T\n')"
git branch $branch_name
git checkout main
git reset --hard HEAD~1
git push origin main --force
git push --set-upstream origin $branch_name
```

Once the offending commit is removed from the main branch, rerun the workflow.
If the workflow fails again, do not roll back again; this avoids a cascade of rollbacks.

### Example scenario

1. Commit 1: workflow succeeds
2. Commit 2: workflow succeeds
3. Commit 3: workflow fails 
   1. Commit 3 is moved to a new branch
   2. the workflow is rerun against Commit 2
4. Commit 2: workflow fails, no further actions taken

## Consequences

* A workflow failure automatic rolls back the commit and any deployments.
* Any failure occurring after a workflow completes successfully would need to be detected by a different mechanism, such as a [canary][cloudwatch-synthetic-monitoring].
* `git pull` will not remove an offending commit from the main branch of the working tree, use `git reset --hard origin/main` instead

[adr-0013]: ./0013-adopt-trunk-based-development.md
[adr-0014]: ./0014-store-in-house-sources-in-a-monorepo.md
[adr-0015]: ./0015-continuously-deploy-to-test-then-production.md
[cloudwatch-synthetic-monitoring]: https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/CloudWatch_Synthetics_Canaries.html