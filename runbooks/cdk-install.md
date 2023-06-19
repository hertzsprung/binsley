Based on [AWS CDK documentation][cdk-docs].

```
curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash - # https://askubuntu.com/a/548776
sudo apt-get install -y nodejs
npm view aws-cdk version # latest
npm install -g aws-cdk@2.84.0
cdk init --language java # requires mvn
```

[cdk-docs]: https://docs.aws.amazon.com/cdk/v2/guide/cli.html
