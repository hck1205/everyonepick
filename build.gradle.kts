buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.20")
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.6")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
}
