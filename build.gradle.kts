buildscript {
    val agp_version by extra("8.6.0")
}
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}