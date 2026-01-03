package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.DataHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReceivesPower extends BlockModelPredicate {
    public final boolean checkEmission;

    public ReceivesPower(boolean checkEmission) {
        this.checkEmission = checkEmission;
    }

    @Override
    public boolean meetsCondition(BlockGetter _unused, BlockPos pos, BlockState state, Identifier renderContext) {
        Level world = Minecraft.getInstance().level;

        assert world != null;
        return world.hasNeighborSignal(pos) && (!checkEmission || Arrays.stream(Direction.values()).anyMatch(dir -> world.hasSignal(pos, dir)));
    }

    public static ReceivesPower parse(JsonElement arg) {
        JsonObject json = arg.getAsJsonObject();
        boolean checkIfEmitting = false;
        if (json.has("is_emitting"))
            checkIfEmitting = json.get("is_emitting").getAsBoolean();
        return new ReceivesPower(checkIfEmitting);
    }
}
