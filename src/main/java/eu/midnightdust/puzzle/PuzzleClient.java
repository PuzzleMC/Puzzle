package eu.midnightdust.puzzle;

import eu.midnightdust.cullleaves.config.CullLeavesConfig;
import eu.midnightdust.puzzle.screen.widget.PuzzleWidget;
import me.lambdaurora.lambdabettergrass.LBGConfig;
import me.lambdaurora.lambdabettergrass.LambdaBetterGrass;
import me.lambdaurora.lambdynlights.DynamicLightsConfig;
import me.lambdaurora.lambdynlights.LambDynLights;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import team.chisel.ctm.client.CTMClient;
import team.chisel.ctm.client.config.ConfigManager;

public class PuzzleClient implements ClientModInitializer {

    public static final Text YES = new TranslatableText("gui.yes").formatted(Formatting.GREEN);
    public static final Text NO = new TranslatableText("gui.no").formatted(Formatting.RED);

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("cullleaves")) {
            PuzzleApi.addToPerformanceOptions(new PuzzleWidget(Text.of("Cull Leaves"), (button) -> button.setMessage(CullLeavesConfig.enabled ? YES : NO), (button) -> {
                CullLeavesConfig.enabled = !CullLeavesConfig.enabled;
                CullLeavesConfig.write();
                MinecraftClient.getInstance().worldRenderer.reload();
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("lambdynlights")) {
            DynamicLightsConfig ldlConfig = LambDynLights.get().config;
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode"), (button) -> button.setMessage(ldlConfig.getDynamicLightsMode().getTranslatedText()), (button) -> ldlConfig.setDynamicLightsMode(ldlConfig.getDynamicLightsMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode").append(": ").append(new TranslatableText("lambdynlights.option.entities")), (button) -> button.setMessage(ldlConfig.hasEntitiesLightSource() ? YES : NO), (button) -> ldlConfig.setEntitiesLightSource(!ldlConfig.hasEntitiesLightSource())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode").append(": ").append(new TranslatableText("lambdynlights.option.block_entities")), (button) -> button.setMessage(ldlConfig.hasBlockEntitiesLightSource() ? YES : NO), (button) -> ldlConfig.setBlockEntitiesLightSource(!ldlConfig.hasBlockEntitiesLightSource())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode").append(": ").append(new TranslatableText("entity.minecraft.creeper")), (button) -> button.setMessage(ldlConfig.getCreeperLightingMode().getTranslatedText()), (button) -> ldlConfig.setCreeperLightingMode(ldlConfig.getCreeperLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode").append(": ").append(new TranslatableText("block.minecraft.tnt")), (button) -> button.setMessage(ldlConfig.getTntLightingMode().getTranslatedText()), (button) -> ldlConfig.setTntLightingMode(ldlConfig.getTntLightingMode().next())));
            PuzzleApi.addToGraphicsOptions(new PuzzleWidget(new TranslatableText("lambdynlights.option.mode").append(": ").append(new TranslatableText("lambdynlights.option.water_sensitive")), (button) -> button.setMessage(ldlConfig.hasWaterSensitiveCheck() ? YES : NO), (button) -> ldlConfig.setWaterSensitiveCheck(!ldlConfig.hasWaterSensitiveCheck())));
        }
        if (FabricLoader.getInstance().isModLoaded("ctm")) {
            ConfigManager ctmfConfigManager = CTMClient.getConfigManager();
            ConfigManager.Config ctmfConfig = CTMClient.getConfigManager().getConfig();
            PuzzleApi.addToTextureOptions(new PuzzleWidget(new TranslatableText("puzzle.option.ctm"), (button) -> button.setMessage(ctmfConfig.disableCTM ? NO : YES), (button) -> {
                ctmfConfig.disableCTM = !ctmfConfig.disableCTM;
                ctmfConfigManager.onConfigChange();
            }));
            PuzzleApi.addToTextureOptions(new PuzzleWidget(new TranslatableText("puzzle.option.inside_ctm"), (button) -> button.setMessage(ctmfConfig.connectInsideCTM ? YES : NO), (button) -> {
                ctmfConfig.connectInsideCTM = !ctmfConfig.connectInsideCTM;
                ctmfConfigManager.onConfigChange();
            }));
        }
        if (FabricLoader.getInstance().isModLoaded("lambdabettergrass")) {
            LBGConfig lbgConfig = LambdaBetterGrass.get().config;
            PuzzleApi.addToTextureOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.mode"), (button) -> button.setMessage(lbgConfig.getMode().getTranslatedText()), (button) -> lbgConfig.setMode(lbgConfig.getMode().next())));
            PuzzleApi.addToTextureOptions(new PuzzleWidget(new TranslatableText("lambdabettergrass.option.better_snow"), (button) -> button.setMessage(lbgConfig.hasBetterLayer() ? YES : NO), (button) -> lbgConfig.setBetterLayer(!lbgConfig.hasBetterLayer())));
        }
    }
}
