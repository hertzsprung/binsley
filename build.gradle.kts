import java.io.FileOutputStream

plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("software.amazon.awscdk:aws-cdk-lib:2.102.0")
    implementation("software.constructs:constructs:10.3.0")
    implementation("io.github.cdklabs:cdk-stacksets:0.0.148")
    implementation("com.pepperize:cdk-organizations:0.7.710")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.assertj:assertj-bom:3.24.2"))
    testImplementation("org.assertj:assertj-core")
    testImplementation(platform("software.amazon.awssdk:bom:2.21.5"))
    testImplementation("software.amazon.awssdk:cognitoidentityprovider")
    testImplementation("software.amazon.awssdk:ssm")
    testImplementation("software.amazon.awssdk:sso")
    testImplementation("software.amazon.awssdk:ssooidc")
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