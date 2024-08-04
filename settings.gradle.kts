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

val layerModules
    get() = listOf(Module("data"), Module("domain"), Module("presentation"))
val apiImpl
    get() = listOf(Module("api"), Module("impl"))

val coreModule = Module(
    name = "core",
    submodules = listOf(
        Module(
            name = "audioPlayer",
            submodules = listOf(Module("api"), Module("impl"))
        ),
        Module("dataModel"),
        Module("domainModel"),
        Module("navigation"),
        Module("presentation"),
        Module("ui"),
        Module("utils"),
        Module("viewState"),
    )
)

val featureLocal = Module(
    name = "featureLocal",
    submodules = listOf(
        Module(
            name = "core",
            submodules = listOf(
                Module("data"),
                Module("ui"),
                Module("viewState"),
            )
        ),
        Module(
            name = "featureHome",
            submodules = layerModules
        ),
        Module("featureRequirePermissions"),
        Module(
            name = "featureSongsSet",
            submodules = listOf(
                Module("presentation"),
            )
        ),
        Module(
            name = "navigation",
            submodules = listOf(
                Module("api"),
                Module("impl"),
                Module("graph")
            )
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
    featureLocal,
    featureYoutube,
    resources,
    Module(
        "bottomBar",
        submodules = apiImpl
    ),
    Module("musicBar"),
)

data class Module(
    val name: String,
    val submodules: List<Module> = emptyList()
)

fun includeModules(vararg modules: Module) {
    modules.forEach { module ->
        module.getPaths().forEach { include(it) }
    }
}

fun Module.getPaths(): List<String> =
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
