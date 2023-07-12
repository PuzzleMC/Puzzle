package net.puzzlemc.core.util;

import com.terraformersmc.modmenu.config.ModMenuConfig;

public class ModMenuUtil {
    public static boolean hasClassicButton() {
        return ModMenuConfig.MODIFY_TITLE_SCREEN.getValue() && ModMenuConfig.MODS_BUTTON_STYLE.getValue() == ModMenuConfig.TitleMenuButtonStyle.CLASSIC;
    }
}
