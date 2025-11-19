plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("me.modmuss50.mod-publish-plugin")
    id("com.github.johnrengelman.shadow")
}

val minecraft = stonecutter.current.version
val loader = loom.platform.get().name.lowercase()

version = "${mod.version}+$minecraft"
group = mod.group
base {
    archivesName.set("${mod.id}-$loader")
}

repositories {
    maven("https://maven.neoforged.net/releases/")

    // ModMenu
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.nucleoid.xyz/")

    // MidnightLib
    maven("https://maven.midnightdust.eu/snapshots/")

    // Jigsaw modules
    maven("https://api.modrinth.com/maven")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://aperlambda.github.io/maven")
    mavenCentral()
    maven("https://maven.gegy.dev")

    maven("https://www.cursemaven.com")
    maven("https://jitpack.io")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.quiltmc.org/repository/release/")
}
dependencies {
    minecraft("com.mojang:minecraft:$minecraft")

    modCompileOnlyApi ("maven.modrinth:cull-leaves:${mod.jigsaw("cull_leaves_version")}")
    modCompileOnlyApi ("maven.modrinth:iris:${mod.jigsaw("iris_version")}")
    modCompileOnly ("maven.modrinth:cit-resewn:${mod.jigsaw("cit_resewn_version")}")
    modCompileOnlyApi ("maven.modrinth:continuity:${mod.jigsaw("continuity_version")}")
    modCompileOnlyApi ("maven.modrinth:animatica:${mod.jigsaw("animatica_version")}")
    modCompileOnlyApi ("maven.modrinth:colormatic:${mod.jigsaw("colormatic_version")}")
    modCompileOnlyApi ("maven.modrinth:borderless-mining:${mod.jigsaw("borderless_mining_version")}")
    modCompileOnlyApi ("maven.modrinth:dynamic-fps:${mod.jigsaw("dynamic_fps_version")}")
    modCompileOnlyApi ("com.moandjiezana.toml:toml4j:${mod.jigsaw("toml4j_version")}")
    modCompileOnlyApi ("maven.modrinth:entitytexturefeatures:${mod.jigsaw("etf_version")}")
    modCompileOnlyApi ("maven.modrinth:entity-model-features:${mod.jigsaw("emf_version")}")
    modCompileOnlyApi ("maven.modrinth:completeconfig:${mod.jigsaw("complete_config_version")}")
    //modImplementation ("maven.modrinth:exordium:${project.exordium_version}")


    modCompileOnlyApi ("maven.modrinth:lambdynamiclights:${mod.jigsaw("ldl_version")}")
    modCompileOnly("dev.lambdaurora.lambdynamiclights:lambdynamiclights-api:${mod.jigsaw("ldl_version")}")
    modCompileOnlyApi ("maven.modrinth:lambdabettergrass:${mod.jigsaw("lbg_version")}")
    modCompileOnlyApi ("dev.lambdaurora:spruceui:${mod.jigsaw("spruceui_version")}")

    // Required for Lambda's mods (DO NOT DEPEND ON OTHERWISE)
    modCompileOnly ("org.quiltmc:quilt-loader:${mod.jigsaw("quilt_loader_version")}")
    modCompileOnlyApi ("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${mod.jigsaw("quilt_fabric_api_version")}")
    modCompileOnlyApi ("org.aperlambda:lambdajcommon:1.8.1") {
        exclude(group = "com.google.code.gson")
        exclude(group = "com.google.guava")
    }

    modImplementation ("eu.midnightdust:midnightlib:${mod.dep("midnightlib_version")}+${minecraft}-${loader}")

    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        modImplementation("com.terraformersmc:modmenu:${mod.dep("modmenu_version")}")

        // Fabric API is required to load modded resources
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_version")}")
    }
    if (loader == "forge") {
        "forge"("net.minecraftforge:forge:${minecraft}-${mod.dep("forge_loader")}")
    }
    if (loader == "neoforge") {
        "neoForge"("net.neoforged:neoforge:${mod.dep("neoforge_loader")}")
    }
    mappings (loom.officialMojangMappings())
}

loom {
    accessWidenerPath = rootProject.file("src/main/resources/puzzle-models.accesswidener")

    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }
    if (loader == "forge") {
        forge.mixinConfigs("puzzle.mixins.json")
    }
}

publishMods {
    val modrinthToken = System.getenv("MODRINTH_TOKEN")
    val curseforgeToken = System.getenv("CURSEFORGE_TOKEN")
    val githubToken = System.getenv("GITHUB_TOKEN").orEmpty()

    file = project.tasks.remapJar.get().archiveFile
    dryRun = modrinthToken == null || curseforgeToken == null

    displayName = "${mod.name} ${loader.replaceFirstChar { it.uppercase() }} ${property("mod.mc_title")}-${mod.version}"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = BETA

    modLoaders.add(loader)

    val targets = property("mod.mc_targets").toString().split(' ')
    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = modrinthToken
        targets.forEach(minecraftVersions::add)
        if (loader == "fabric") {
            requires("fabric-api")
            optional("modmenu")
        }
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = curseforgeToken.toString()
        targets.forEach(minecraftVersions::add)
        if (loader == "fabric") {
            requires("fabric-api")
            optional("modmenu")
        }
    }

    github {
        accessToken = githubToken
        repository = "TeamMidnightDust/MidnightLib"
        commitish = "multiversion" // This is the branch the release tag will be created from

        tagName = "v" + properties["mod.version"]

        // Allow the release to be initially created without any files.
        allowEmptyFiles = true
    }
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5")) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener = true
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier = "dev"
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
}

tasks.processResources {
    properties(
        listOf("fabric.mod.json"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_fabric")
    )
    properties(
        listOf("META-INF/mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_forgelike")
    )
    properties(
        listOf("META-INF/neoforge.mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "minecraft" to mod.prop("mc_dep_forgelike")
    )
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}


stonecutter {
    constants {
        arrayOf("fabric", "neoforge", "forge").forEach { it -> put(it, loader == it) }
    }
}
