import org.gradle.external.javadoc.CoreJavadocOptions

plugins {
    `java-library`
    `maven-publish`
}

val isJitpack = System.getenv("JITPACK") == "true"
val libraryVersion: String = providers.gradleProperty("arkitectury.math.version").get()

group = if (isJitpack) {
    System.getenv("GROUP")?.takeIf { it.isNotBlank() } ?: "com.github.xw1w1"
} else {
    providers.gradleProperty("group").orElse("io.kanvas").get()
}
version = if (isJitpack) {
    System.getenv("VERSION")?.takeIf { it.isNotBlank() } ?: libraryVersion
} else {
    providers.gradleProperty("version").orNull?.takeUnless { it == "unspecified" } ?: libraryVersion
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(
        listOf(
            "-Xlint:all,-processing,-classfile",
            "-parameters"
        )
    )
}

dependencies {
    api(libs.joml)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
    (options as? CoreJavadocOptions)?.addBooleanOption("Xdoclint:none", true)
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "Arkitectury",
                "Build-Jdk-Spec" to "21"
            )
        )
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "arkitectury-math"
            pom {
                name.set("Arkitectury Math")
                description.set("Shared lightweight geometry and color primitives for Arkitectury and Kanvas UI.")
            }
        }
    }
}
