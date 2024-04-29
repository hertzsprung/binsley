# 17. Enable Gradle build cache in GitHub Actions

Date: 2024-04-29

## Status

Accepted

## Context

[GitHub Actions][adr-0005] uses [Gradle to build Java code and run tests][adr-0010].
With [all code stored in a monorepo][adr-0014], to keep workflow execution times short, Gradle should selectively build and test when changes are made to particular subdirectories. 

## Decision

Enable the [Gradle build cache] in GitHub Actions workflows using [setup-gradle].
To ensure the project can be built and tested from scratch, disable the cache in a weekly [scheduled workflow].

## Consequences

Gradle tasks are not executed if they are up-to-date according to the build cache.
If a commit does not change a Gradle subproject, then no tasks in that subproject are executed.

[adr-0005]: ./0005-build-and-deploy-with-github-actions.md
[adr-0010]: ./0010-build-java-code-with-gradle.md
[adr-0014]: ./0014-store-in-house-sources-in-a-monorepo.md
[Gradle build cache]: https://docs.gradle.org/current/userguide/build_cache.html
[scheduled workflow]: https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#schedule
[setup-gradle]: https://github.com/gradle/actions/tree/main/setup-gradle