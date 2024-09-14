plugins {
    id("java")
    id("com.diffplug.spotless") version "6.13.0"
}

group = "com.premsan"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

spotless {
    ratchetFrom("origin/main")

    format("misc") {
        target("*.gradle", ".gitattributes", ".gitignore")

        trimTrailingWhitespace()
        indentWithTabs()
        endWithNewline()
    }
}