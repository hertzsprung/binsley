import java.io.FileOutputStream

plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

application {
    mainClass.set("uk.co.datumedge.binsley.BinsleyApp")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("software.amazon.awscdk:aws-cdk-lib:2.128.0")
    implementation("software.constructs:constructs:10.3.0")
    implementation("io.github.cdklabs:cdk-pipelines-github:0.4.116")
    implementation("io.github.cdklabs:cdk-stacksets:0.0.150")
    implementation("com.pepperize:cdk-organizations:0.7.874")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.assertj:assertj-bom:3.25.3"))
    testImplementation("org.assertj:assertj-core")
    testImplementation(platform("software.amazon.awssdk:bom:2.24.5"))
    testImplementation("software.amazon.awssdk:cognitoidentityprovider")
    testImplementation("software.amazon.awssdk:iam")
    testImplementation("software.amazon.awssdk:ssm")
    testImplementation("software.amazon.awssdk:sso")
    testImplementation("software.amazon.awssdk:ssooidc")
    testImplementation("software.amazon.awssdk:sts")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
}

val generateCdkBootstrap = tasks.register<Exec>("generateCdkBootstrap") {
    commandLine("cdk", "bootstrap", "--show-template")

    doFirst {
        standardOutput = FileOutputStream(layout.buildDirectory.file("cdk-bootstrap.generated.yaml").get().asFile)
    }
}

tasks.named("run") {
    dependsOn(generateCdkBootstrap)
}