pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Ingale"
includeBuild("gradlePlugins")
include(":app")

val apiImpl
    get() = listOf(Module("api"), Module("impl"))

val coreModule = Module(
    name = "core",
    submodules = listOf(
        Module(
            name = "audioPlayer",
            submodules = listOf(Module("api"), Module("impl"))
        ),
        Module("decompose"),
        Module("presentation"),
        Module("ui"),
        Module("utils"),
        Module("viewState"),
    )
)

val root = Module(
    name = "root",
    submodules = apiImpl,
)

val featureLocal = Module(
    name = "featureLocal",
    submodules = listOf(
        Module(
            name = "core",
            submodules = listOf(
                Module("data"),
                Module("domain"),
                Module("presentation"),
            )
        ),
        Module(
            name = "featureHome",
            submodules = listOf(
                Module("domain"),
                Module("presentation"),
            )
        ),
        Module("featureRequirePermissions"),
        Module(
            name = "featureSongsSet",
            submodules = listOf(
                Module("presentation"),
            )
        ),
        Module(
            name = "featurePlaylists",
        ),
        Module(
            name = "navigation",
            submodules = apiImpl + Module("ui"),
        )
    )
)

val featureYoutube = Module("featureYoutube")

val resources = Module(
    "resources",
    submodules = listOf(
        Module("bottomBar"),
        Module("icon"),
        Module("playbackActions"),
        Module("songOperations"),
        Module("strings"),
    )
)

includeModules(
    coreModule,
    root,
    featureLocal,
    featureYoutube,
    resources,
    Module(
        "bottomBarApi",
    ),
    Module("globalPlaybackPresentation"),
)

data class Module(
    val name: String,
    val submodules: List<Module> = emptyList()
)

private fun includeModules(vararg modules: Module) {
    modules.forEach { module ->
        module.getPaths().forEach { include(it) }
    }
}

private fun Module.getPaths(): List<String> =
    if(submodules.isEmpty()) {
        listOf(this.name)
    } else {
        val list = mutableListOf<String>()
        submodules.forEach { submodule ->
            val path = this.name
            submodule.getPaths().forEach {
                list.add("$path:$it")
            }
        }
        list
    }
