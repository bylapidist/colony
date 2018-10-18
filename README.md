### Releasing a new version

- Run: `npm version <major|minor|patch>`
- Generate change log: `npm run changelog`
- Commit your changes on `feature/yourbranch` and submit a pull request to `develop`
- After merged to `develop`, tag with semver: `git tag v1.0.x` and `git push --tags` 