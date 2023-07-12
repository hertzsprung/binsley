# 12. Run tests with JUnit

Date: 2023-07-12

## Status

Accepted

## Context

An automated test framework is needed to run tests written in Java.

## Decision

Use [JUnit][junit], a widely used Java test framework.

## Consequences

JUnit integrates with [Gradle][gradle] ([ADR #10][adr10])
and [GitHub Actions][action-junit-report] ([ADR #5][adr5]).

[adr5]: 0005-build-and-deploy-with-github-actions.md
[adr10]: 0010-build-java-code-with-gradle.md
[action-junit-report]: https://github.com/mikepenz/action-junit-report
[gradle]: https://gradle.org
[junit]: https://junit.org/junit5/