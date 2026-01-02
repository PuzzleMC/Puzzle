plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("me.modmuss50.mod-publish-plugin")
    id("com.github.johnrengelman.shadow")
    `maven-publish`
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
    maven("https://maven.midnightdust.eu/releases/")

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

val mappingsAttribute = Attribute.of("net.minecraft.mappings", String::class.java)

dependencies {
    attributesSchema {
        attribute(mappingsAttribute)
    }
    minecraft("com.mojang:minecraft:$minecraft")

    // Jigsaw modules (GUI compat mods)
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

    modCompileOnlyApi ("maven.modrinth:lambdabettergrass:${mod.jigsaw("lbg_version")}")
    modCompileOnlyApi ("dev.lambdaurora:spruceui:${mod.jigsaw("spruceui_version")}")

    // Required for Lambda's mods (DO NOT DEPEND ON OTHERWISE)
    modCompileOnly ("dev.yumi.mc.core:yumi-mc-foundation:1.0.0-alpha.15+1.21.1")
    modCompileOnlyApi ("org.aperlambda:lambdajcommon:1.8.1") {
        exclude(group = "com.google.code.gson")
        exclude(group = "com.google.guava")
    }

    // MidnightLib
    val midnightlib = if (mod.dep("midnightlib_version").contains("+")) "eu.midnightdust:midnightlib:${mod.dep("midnightlib_version")}"
                      else "eu.midnightdust:midnightlib:${mod.dep("midnightlib_version")}+${minecraft}-${loader}"
    modImplementation(midnightlib) {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "com.terraformersmc")
    }
    include(midnightlib)

    if (loader == "fabric") {
        modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        modImplementation("com.terraformersmc:modmenu:${mod.dep("modmenu_version")}")

        // Fabric API is required to load modded resources
        modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_version")}")

        modCompileOnly ("dev.lambdaurora.lambdynamiclights:lambdynamiclights-api:${mod.jigsaw("ldl_version")}")
        modCompileOnly ("dev.lambdaurora.lambdynamiclights:lambdynamiclights-runtime:${mod.jigsaw("ldl_version")}")
    }
    if (loader == "neoforge") {
        "neoForge"("net.neoforged:neoforge:${mod.dep("neoforge_loader")}")

        modCompileOnly ("dev.lambdaurora.lambdynamiclights:lambdynamiclights-api:${mod.jigsaw("ldl_version")}") {
            attributes {
                attribute(mappingsAttribute, "mojmap")
            }
        }
        modCompileOnly ("dev.lambdaurora.lambdynamiclights:lambdynamiclights-runtime:${mod.jigsaw("ldl_version")}") {
            attributes {
                attribute(mappingsAttribute, "mojmap")
            }
        }
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

    displayName = "${mod.name} ${mod.version} - ${loader.replaceFirstChar { it.uppercase() }} ${property("mod.mc_title")}"
    version = "${mod.version}+${property("mod.mc_title")}-${loader}"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE

    modLoaders.add(loader)
    if (loader == "fabric") {
        modLoaders.add("quilt")
    }

    val targets = property("mod.mc_targets").toString().split(' ')
    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = modrinthToken
        targets.forEach(minecraftVersions::add)
        requires("midnightlib")
        if (loader == "fabric") {
            requires("fabric-api")
        }
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = curseforgeToken.toString()
        targets.forEach(minecraftVersions::add)
        requires("midnightlib")
        if (loader == "fabric") {
            requires("fabric-api")
        }
    }

//    github {
//        accessToken = githubToken
//        repository = "TeamMidnightDust/MidnightLib"
//        commitish = "multiversion" // This is the branch the release tag will be created from
//
//        tagName = "v" + properties["mod.version"]
//
//        // Allow the release to be initially created without any files.
//        allowEmptyFiles = true
//    }
}
publishing {
    repositories {
        maven {
            name = "MidnightDust"
            url = uri("https://maven.midnightdust.eu/releases")
            credentials(PasswordCredentials::class)
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                groupId = "eu.midnightdust"
                artifactId = project.mod.id
                version = "${project.version}-${loader}"

                from(components["java"])
            }
        }
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
        arrayOf("fabric", "neoforge").forEach { it -> put(it, loader == it) }
    }

    replacements.string {
        direction = eval(current.version, ">=1.21.8")
        replace("context.renderComponentTooltip(", "context.setComponentTooltipForNextFrame(")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21.4")
        replace("getTextureImage", "loadContents")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21.4")
        replace("TextureImage", "TextureContents")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21.4")
        replace("SimpleTexture", "ReloadableTexture")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21.11")
        replace("ResourceLocation", "Identifier")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21")
        replace("new ResourceLocation", "ResourceLocation.fromNamespaceAndPath")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21.11")
        replace("new Identifier", "Identifier.fromNamespaceAndPath")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21.11")
        replace("net.minecraft.Util", "net.minecraft.util.Util")
    }
    replacements.string {
        direction = eval(current.version, ">=1.21")
        replace(".getDeltaFrameTime()", ".getTimer().getGameTimeDeltaTicks()")
    }
}
