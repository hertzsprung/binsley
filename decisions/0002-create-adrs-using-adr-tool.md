# 2. Create ADRs using adr-tools

Date: 2023-06-08

## Status

Accepted

## Context

Ensure ADRs are easily created in a standard format using an off-the-shelf tool.

## Decision

Use [adr-tools][adr-tools] to create skeletal ADRs.
The tool is available in the Ubuntu package repository, and rewrites of the tool are [available for a range of programming languages](https://adr.github.io/#decision-capturing-tools).

## Consequences

The ADR format is standardised.  adr-tools must be installed.

[adr-tools]: https://github.com/npryce/adr-tools
