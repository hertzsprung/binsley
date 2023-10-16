[GitHub deployment environment docs](https://docs.github.com/en/rest/deployments/environments?apiVersion=2022-11-28)

Using a token with Administration Read & Write Repository Permissions:
```shell
export request=$(cat <<EOF
{
  "wait_timer": 0,
  "prevent_self_review": false,
  "reviewers": null,
  "deployment_branch_policy": null
}
EOF
)
  
export REPOSITORY_ID=650768827

curl -L \
  -X PUT \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repos/hertzsprung/binsley/environments/nonprod \
  -d "$request"
```

```shell
export variables=$(cat <<EOF
{
  "name": "AWS_ACCOUNT_ID",
  "value":"074782343366"
}
EOF
)
```

Using a token with Environment Read & Write Repository Permissions:
```shell
curl -L \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repositories/$REPOSITORY_ID/environments/nonprod/variables \
  -d "$variables"
```