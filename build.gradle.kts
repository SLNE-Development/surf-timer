plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

version = findProperty("version") as String
group = "dev.slne.surf.timer"

surfPaperPluginApi {
    mainClass("dev.slne.surf.timer.PaperMain")
    authors.add("red")
    foliaSupported(true)

    generateLibraryLoader(false)
}