# 5. Build and deploy with GitHub Actions

Date: 2023-06-11

## Status

Accepted

## Context

A managed service is needed to build and deploy the project.

## Decision

Build and deploy with [GitHub Actions][gh-actions], which integrates directly with GitHub project hosting.
This is an opportunity to learn how to use GitHub Actions.

## Consequences

Build and deployment configuration is peculiar to GitHub, but this is true of most build services.

[gh-actions]: https://github.com/features/actions
