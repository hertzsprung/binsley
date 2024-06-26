plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.144.0")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.assertj:assertj-bom:3.26.0"))
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.awaitility:awaitility:4.2.1")
    testImplementation(platform("software.amazon.awssdk:bom:2.25.65"))
    testImplementation("software.amazon.awssdk:cloudwatch")
    testImplementation("software.amazon.awssdk:sso")
    testImplementation("software.amazon.awssdk:ssooidc")
    testImplementation("software.amazon.awssdk:sts")
    testImplementation(platform("org.slf4j:slf4j-bom:2.0.13"))
    testImplementation("org.slf4j:slf4j-simple:2.0.13")
}