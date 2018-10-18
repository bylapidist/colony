### Releasing a new version

- On `develop`: `git checkout -b release/v1.0.x`
- Commit your changes on `release/v1.0.x` and submit a pull request to `master`
- `git checkout master && git pull`
- Run: `npm version <major|minor|patch>`
- `git checkout -b chore/changelog && npm run changelog`
- Create a pull request to `master`
- Merge `master` into `develop`
