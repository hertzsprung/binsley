[GitHub deployment environment docs](https://docs.github.com/en/rest/deployments/environments?apiVersion=2022-11-28)

## Create an environment
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
  
export ENVIRONMENT=test
  
curl -L \
  -X PUT \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repos/hertzsprung/binsley/environments/$ENVIRONMENT \
  -d "$request"
```

## Create the AWS_ACCOUNT_ID environment-scoped variable
Using a token with Environment Read & Write Repository Permissions:
```shell
export variables=$(cat <<EOF
{
  "name": "AWS_ACCOUNT_ID",
  "value":"074782343366"
}
EOF
)

export ENVIRONMENT=test
export REPOSITORY_ID=650768827

curl -L \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repositories/$REPOSITORY_ID/environments/$ENVIRONMENT/variables \
  -d "$variables"
```

## Create the ROLLBACK_TOKEN repository secret
Using a token with Secrets Read & Write Repository Permissions:
```shell
# get the repository public key
curl -L \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repos/hertzsprung/binsley/actions/secrets/public-key

export KEY_ID=3380204578043523366 # provided in REST API response
export ENCRYPTED_VALUE=... # provided by github-environments-encrypt-secret.js

export variables=$(cat <<EOF
{
  "encrypted_value": "$ENCRYPTED_VALUE",
  "key_id":"$KEY_ID"
}
EOF
)
  
export REPOSITORY_ID=650768827

curl -L \
  -X PUT \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repos/hertzsprung/binsley/actions/secrets/ROLLBACK_TOKEN \
  -d "$variables"
```

See also:
* [Encrypting secrets for the REST API](https://docs.github.com/en/rest/guides/encrypting-secrets-for-the-rest-api?apiVersion=2022-11-28)