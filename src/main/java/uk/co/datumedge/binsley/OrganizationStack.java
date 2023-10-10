package uk.co.datumedge.binsley;

import com.pepperize.cdk.organizations.Organization;
import com.pepperize.cdk.organizations.OrganizationalUnit;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

// OUs won't be registered with Control Tower, which can only be done via the AWS Console
// https://repost.aws/questions/QUowD5zNXBQ6ewmXDas_wH8A/is-it-possible-to-creating-control-tower-ous-programmatically-with-selected-guardrails
public class OrganizationStack extends Stack {

    private final OrganizationalUnit workloadsOU;

    public OrganizationStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

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

        OrganizationalUnit.Builder.create(this, "NonProdWorkloadsOU")
                .organizationalUnitName("NonProd")
                .parent(workloadsOU)
                .build();

        OrganizationalUnit.Builder.create(this, "SandboxOU")
                .organizationalUnitName("Sandbox")
                .parent(organization.getRoot())
                .build();
    }

    public String getWorkloadsOUId() {
        return workloadsOU.getOrganizationalUnitId();
    }
}
