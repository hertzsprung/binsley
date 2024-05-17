package uk.co.datumedge.binsley.organization;

import com.pepperize.cdk.organizations.OrganizationalUnit;
import com.pepperize.cdk.organizations.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import static com.pepperize.cdk.organizations.PolicyType.SERVICE_CONTROL_POLICY;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Declares the hierarchy of Organizational Units (OUs).
 */
// OUs won't be registered with Control Tower, which can only be done via the AWS Console
// https://repost.aws/questions/QUowD5zNXBQ6ewmXDas_wH8A/is-it-possible-to-creating-control-tower-ous-programmatically-with-selected-guardrails
public class OrganizationStack extends Stack {
    private final OrganizationalUnit workloadsOU;
    private final OrganizationalUnit nonProdOU;
    private final OrganizationalUnit sandboxOU;

    public OrganizationStack(Construct parent, String id, StackProps props) {
        super(parent, id, props);

        Organization organization = Organization.Builder.create(this, "Organization").build();

        workloadsOU = OrganizationalUnit.Builder.create(this, "WorkloadsOU")
                .organizationalUnitName("Workloads")
                .parent(organization.getRoot())
                .build();

        OrganizationalUnit.Builder.create(this, "ProdWorkloadsOU")
                .organizationalUnitName("Prod")
                .parent(workloadsOU)
                .build();

        nonProdOU = OrganizationalUnit.Builder.create(this, "NonProdWorkloadsOU")
                .organizationalUnitName("NonProd")
                .parent(workloadsOU)
                .build();

        sandboxOU = OrganizationalUnit.Builder.create(this, "SandboxOU")
                .organizationalUnitName("Sandbox")
                .parent(organization.getRoot())
                .build();

        organization.getRoot().attachPolicy(policy("DeploymentRegions", "scp-deny-using-other-regions.json")
                .description("Deny actions outside deployment regions")
                .build());

        organization.getRoot().attachPolicy(policy("AccountBaseline", "scp-deny-changing-account-baseline-configuration.json")
                .description("Deny changes to baseline account configuration")
                .build());

        organization.getRoot().attachPolicy(policy("NoPurchases", "scp-deny-making-agreements-purchases-and-reservations.json")
                .description("Deny long-term financial commitments")
                .build());

        organization.getRoot().attachPolicy(policy("NoIAM", "scp-deny-modifying-central-iam-resources.json")
                .description("Deny actions on IAM users, groups, or central IAM resources")
                .build());
    }

    private Policy.Builder policy(String id, String fileName) {
        try {
            var policyDocument = new String(Objects.requireNonNull(getClass().getResourceAsStream(fileName)).readAllBytes(), UTF_8);

            return Policy.Builder.create(this, id)
                .policyType(SERVICE_CONTROL_POLICY)
                .policyName(id)
                .content(policyDocument);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String workloadsOUId() {
        return workloadsOU.getOrganizationalUnitId();
    }

    public uk.co.datumedge.binsley.organization.OrganizationalUnit nonProdOU() {
        return convert(nonProdOU);
    }

    public uk.co.datumedge.binsley.organization.OrganizationalUnit sandboxOU() {
        return convert(sandboxOU);
    }

    private static uk.co.datumedge.binsley.organization.OrganizationalUnit convert(OrganizationalUnit ou) {
        return new uk.co.datumedge.binsley.organization.OrganizationalUnit(ou.getOrganizationalUnitId(), ou.getOrganizationalUnitArn());

    }
}
