package uk.co.datumedge.binsley.organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepperize.cdk.organizations.OrganizationalUnit;
import com.pepperize.cdk.organizations.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;

import java.io.UncheckedIOException;
import java.util.List;

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

        sandboxOU.attachPolicy(elevatedPowerUser());
    }

    private IPolicy elevatedPowerUser() {
            try {
        return Policy.Builder.create(this, "SandboxOUPolicy")
                .policyType(PolicyType.SERVICE_CONTROL_POLICY)
                .policyName("ElevatedPowerUser")
                .description("Allow CDK bootstrap")
                .content(new ObjectMapper().writeValueAsString(PolicyDocument.Builder.create()
                        .statements(List.of(PolicyStatement.Builder.create()
                                .effect(Effect.DENY)
                                .actions(List.of("organization:*", "account:*"))
                                .resources(List.of("*"))
                                .build()))
                        .build().toJSON()))
                .build();
        } catch (JsonProcessingException e) {
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
