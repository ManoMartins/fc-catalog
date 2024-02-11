plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "fc-catalog"
include("domain")
include("application")
include("infrastructure")
