# 9. Use AWS CDK to manage AWS resources

Date: 2023-06-17

## Status

Accepted

## Context

Manage AWS resources programmatically via code and configuration.
The issue motivating this decision, and any context that influences or constrains the decision.

## Decision

Use [AWS CDK][cdk] to manage AWS resources, with CDK code written in Amazon Corretto OpenJDK 17.

## Consequences

CDK code is written in the preferred language.
Drift detection is not currently supported but, since AWS resources should not be edited manually, configurations should not drift.

[cdk]: https://docs.aws.amazon.com/cdk/v2/guide/home.html
