package com.endremastered.endrem.mixin.world;

import com.endremastered.endrem.world.ERStructureConfig.ERConfiguredStructure;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.SpringFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpringFeature.class)
public class NoSpringsInStructuresMixin {
	@Inject(method = "generate", at = @At(value = "HEAD"), cancellable = true)
    private void noSpringsInStructures(FeatureContext<SingleStateFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        ChunkSectionPos chunkPos = ChunkSectionPos.from(context.getOrigin());

        if (/*context.getWorld().getStructures(chunkPos, ERJigsawStructures.END_GATE).findAny().isPresent() || */context.getWorld().getStructures(chunkPos, ERConfiguredStructure.END_CASTLE).findAny().isPresent()) {
            cir.setReturnValue(false);
            //System.out.println("NO SPRINGS IN THE STRUCTURE");
        }
    }
}
