package uk.co.datumedge.binsley;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudwatch.Alarm;
import software.amazon.awscdk.services.cloudwatch.Metric;
import software.amazon.awscdk.services.cloudwatch.Stats;
import software.amazon.awscdk.services.cloudwatch.actions.SnsAction;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.EmailSubscription;
import software.constructs.Construct;

import java.util.Map;

public class BillingStack extends Stack {
    public BillingStack(final Construct parent, final String id) {
        this(parent, id, StackProps.builder()
                .env(Environment.builder().region("us-east-1").build())
                .build());
    }

    public BillingStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        Alarm billingAlarm = Alarm.Builder.create(this, "BillingAlarm")
                .metric(Metric.Builder.create()
                        .metricName("EstimatedCharges")
                        .namespace("AWS/Billing")
                        .dimensionsMap(Map.of("Currency", "USD"))
                        .period(Duration.hours(6))
                        .statistic(Stats.MAXIMUM)
                        .build())
                .threshold(billingMonthlyUsdAlarmThreshold())
                .evaluationPeriods(1)
                .alarmDescription("Billing alarm")
                .build();

        final Topic billingAlarmTopic = Topic.Builder.create(this, "BillingAlarmTopic")
                .displayName("Billing alarm")
                .build();

        billingAlarmTopic.addSubscription(EmailSubscription.Builder.create(billingAlarmEmail()).build());

        billingAlarm.addAlarmAction(new SnsAction(billingAlarmTopic));
    }

    private Number billingMonthlyUsdAlarmThreshold() {
        return (Number) this.getNode().getContext("datumedge/billing:monthlyUsdAlarmThreshold");
    }

    private String billingAlarmEmail() {
        return (String) this.getNode().getContext("datumedge/billing:alarmEmail");
    }
}
