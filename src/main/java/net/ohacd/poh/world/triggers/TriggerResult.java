package net.ohacd.poh.world.triggers;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public record TriggerResult(boolean fired,
                            @Nullable Identifier triggerId,
                            @Nullable Identifier subjectId,
                            @Nullable BlockPos subjectPos) {
    public static TriggerResult miss() { return new TriggerResult(false, null, null, null); }
    public static TriggerResult hit(Identifier trig, Identifier subj, @Nullable BlockPos pos) {
        return new TriggerResult(true, trig, subj, pos);
    }
}