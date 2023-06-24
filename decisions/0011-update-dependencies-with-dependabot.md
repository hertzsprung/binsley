# 11. Update dependencies with Dependabot

Date: 2023-06-24

## Status

Accepted

## Context

To improve the security posture, a tool is needed to detect and apply updates to dependencies, including Gradle dependencies and GitHub actions.

## Decision

Use [GitHub Dependabot][dependabot] to update dependencies on a scheduled execution.

## Consequences

Dependabot integrates directly with GitHub Actions, and supports [most common package managers][dependabot-package-managers].
The [gradle-versions-plugin][gradle-versions-plugin] provides more comprehensive Gradle support than dependabot: it detects new version of dependencies, as well as Gradle plugins and Gradle itself; but it is limited to the Gradle ecosystem.

[dependabot]: https://docs.github.com/en/code-security/dependabot
[dependabot-package-managers]: https://docs.github.com/en/code-security/dependabot/dependabot-version-updates/configuration-options-for-the-dependabot.yml-file#configuration-options-for-the-dependabotyml-file
[gradle-versions-plugin]: https://github.com/ben-manes/gradle-versions-plugin
