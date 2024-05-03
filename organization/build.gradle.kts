plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.pepperize:cdk-organizations:0.7.895")
    implementation("io.github.cdklabs:cdk-pipelines-github:0.4.117")
    implementation("io.github.cdklabs:cdk-stacksets:0.0.150")
    implementation("software.amazon.awscdk:aws-cdk-lib:2.137.0")
}