package net.puzzlemc.predicates.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.BlockPos;
import net.minecraft.util.InclusiveRange;
import org.jetbrains.annotations.NotNull;

public final class DataHelper {

    public static InclusiveRange<@NotNull Integer> parseIntRange(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();

        boolean hasMin = object.has("min"), hasMax = object.has("max");
        if (hasMin && hasMax) {
            return new InclusiveRange<>(object.get("min").getAsInt(), object.get("max").getAsInt());
        } else if (hasMin) {
            return new InclusiveRange<>(object.get("min").getAsInt(), Integer.MAX_VALUE);
        } else if (hasMax) {
            return new InclusiveRange<>(Integer.MIN_VALUE, object.get("max").getAsInt());
        } else {
            // none?!?!??!?!?!?!?!?!
            throw new JsonParseException("No min or max defined for range!");
        }
    }

    public static BlockPos parseBlockPos(JsonObject offset) {
        int x = offset.get("x").getAsInt();
        int y = offset.get("y").getAsInt();
        int z = offset.get("z").getAsInt();
        return new BlockPos(x, y, z);
    }
}
