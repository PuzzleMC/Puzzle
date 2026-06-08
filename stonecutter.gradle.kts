import org.gradle.kotlin.dsl.replace

plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "0.8.4" apply false
}
stonecutter active "26.1-fabric" /* [SC] DO NOT EDIT */

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["mod_version"] = "\"" + property("mod.version") + "\";"
    swaps["minecraft"] = "\"" + node.metadata.version + "\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_version") as String

    replacements {
        string {
            direction = eval(current.version, ">=1.21.8")
            replace("context.renderComponentTooltip(", "context.setComponentTooltipForNextFrame(")
        }
        string {
            direction = eval(current.version, ">=1.21.4")
            replace("getTextureImage", "loadContents")
        }
        string {
            direction = eval(current.version, ">=1.21.4")
            replace("TextureImage", "TextureContents")
        }
        string {
            direction = eval(current.version, ">=1.21.4")
            replace("SimpleTexture", "ReloadableTexture")
        }
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
        }
        string {
            direction = eval(current.version, ">=1.21")
            replace("new ResourceLocation", "ResourceLocation.fromNamespaceAndPath")
        }
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("net.minecraft.Util", "net.minecraft.util.Util")
        }
        string {
            direction = eval(current.version, ">=26.1-pre.1")
            replace("render(", "extractRenderState(")
        }
        string {
            direction = eval(current.version, ">=26.1-pre.1")
            replace("GuiGraphics", "GuiGraphicsExtractor")
        }
        string {
            direction = eval(current.version, ">=26.1-pre.1")
            replace("renderListSeparators", "extractListSeparators")
        }
        string {
            direction = eval(current.version, ">=26.1-pre.1")
            replace("renderContent", "extractContent")
        }
        string {
            direction = eval(current.version, ">=26.1-pre.1")
            replace("drawProgressBar", "extractProgressBar")
        }
        string {
            direction = eval(current.version, ">=1.21.5")
            replace("net.minecraft.client.resources.model.BakedModel", "net.minecraft.client.renderer.block.model.BlockStateModel")
        }
        string {
            direction = eval(current.version, ">=1.21.5")
            replace("net/minecraft/client/resources/model/BakedModel", "net/minecraft/client/renderer/block/model/BlockStateModel")
        }
        string {
            direction = eval(current.version, ">=1.21.5")
            replace("BakedModel", "BlockStateModel")
        }
    }
}
