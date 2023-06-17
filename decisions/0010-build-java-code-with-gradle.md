# 10. Build Java code with Gradle

Date: 2023-06-17

## Status

Accepted

## Context

Use a standard build tool to compile and package Java code.

## Decision

Build Java code with [Gradle][gradle], with build scripts written in [Kotlin DSL][kotlin-dsl].

## Consequences

Customising builds is readily supported with comprehensive tool support for Kotlin DSL.
AWS CDK does not autogenerate Gradle build scripts, but [scripts can still be written manually][cdk-gradle]. 

[gradle]: https://gradle.org
[kotlin-dsl]: https://docs.gradle.org/current/userguide/kotlin_dsl.html
[cdk-gradle]: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup-project-gradle.html