Create an account via AWS Service Catalog Control Tower Account Factory:
```sh
export parameters=$(cat <<EOF
[
  {
    "Key": "AccountEmail",
    "Value": "js102+sandbox001@zepler.net"
  },
  {
    "Key": "AccountName",
    "Value": "sandbox-001"
  },
  {
    "Key": "ManagedOrganizationalUnit",
    "Value": "Sandbox (ou-o930-fy4el01j)"
  },
  {
    "Key": "SSOUserFirstName",
    "Value": "James"
  },
  {
    "Key": "SSOUserLastName",
    "Value": "Shaw"
  },
  {
    "Key": "SSOUserEmail",
    "Value": "hertzsprung+jshaw@gmail.com"
  }
]
EOF
)
aws --profile admin servicecatalog provision-product \
  --product-name "AWS Control Tower Account Factory" \
  --provisioning-artifact-name "AWS Control Tower Account Factory" \
  --provisioned-product-name sandbox-001 \
  --provisioning-parameters "$parameters"
```
