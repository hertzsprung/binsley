# 14. Store in-house sources in a monorepo

Date: 2024-01-09

## Status

Accepted

## Context

A simple approach is sought for structuring in-house sources: code, configuration, and documentation.
With only one committer, there is little need for team coordination or collaboration, and conflicting changes are unlikely.

## Decision

Store in-house sources in a monorepo.
Download third-party code libraries from central repositories such as [Maven Central][maven-central].

## Consequences

* A deployed system is identifiable by a single git commit
* Global search, navigation, renaming and refactoring are straightforward
* To keep execution times short, GitHub Actions workflows should [selectively][github-actions-workflow-paths] build, test, and deploy when changes are made to particular subdirectories

[github-actions-workflow-paths]: https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onpushpull_requestpull_request_targetpathspaths-ignore
[maven-central]: https://central.sonatype.com