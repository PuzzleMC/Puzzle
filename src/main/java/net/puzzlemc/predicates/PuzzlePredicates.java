package net.puzzlemc.predicates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric {
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
//?}
public class PuzzlePredicates {
    public static final Logger LOGGER = LoggerFactory.getLogger("mbp");

    public static void init() {
        //? fabric {
        //noinspection UnstableApiUsage
        ModelLoadingPluginManager.registerPlugin(new MBPModelLoadingPlugin.ModelIdLoader(), new MBPModelLoadingPlugin());
        //?}
    }

}
