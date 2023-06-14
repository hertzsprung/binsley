# 7. Prefer Amazon Corretto OpenJDK 17

Date: 2023-06-13

## Status

Accepted

## Context

A programming language should be preferred that is openly available, widely supported, with mature development tools and a large Q&A community.

## Decision

Prefer [Amazon Corretto OpenJDK 17][corretto17] as the standard programming language, particularly since it is natively supported by [AWS Lambda][lambda-java17].

## Consequences

Improvements released in later Java versions will be unavailable.

[corretto17]: https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/what-is-corretto-17.html
[lambda-java17]: https://aws.amazon.com/about-aws/whats-new/2023/04/aws-lambda-java-17/
