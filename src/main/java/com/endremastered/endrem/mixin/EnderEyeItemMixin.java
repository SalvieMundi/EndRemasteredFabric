package com.endremastered.endrem.mixin;

import com.endremastered.endrem.config.ConfigHandler;
import com.endremastered.endrem.registry.BlockRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)

//TODO: Add a config option for the methods
public class EnderEyeItemMixin {
    @Inject(method = "useOnBlock",
        at = @At(value = "HEAD"),
        cancellable = true)
    private void DisableUsingEnderEyes(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (ConfigHandler.DISABLE_EYE_OF_ENDER) {
        	World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (!blockState.isOf(Registry.BLOCK.get(new Identifier("bosses_of_mass_destruction", "obsidilith_end_frame")))) {
	        	cir.setReturnValue(ActionResult.PASS);
            }
        }
    }

    @Inject(method = "use",
        at = @At(value = "HEAD"),
        cancellable = true)
    private void DisableThrowingEnderEyes(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (ConfigHandler.DISABLE_EYE_OF_ENDER) {
            HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
            ItemStack itemStack = user.getStackInHand(hand);
            
        	if (hitResult.getType() == HitResult.Type.BLOCK && (world.getBlockState(((BlockHitResult)hitResult).getBlockPos()).isOf(Blocks.END_PORTAL_FRAME) || world.getBlockState(((BlockHitResult)hitResult).getBlockPos()).isOf(BlockRegistry.ANCIENT_PORTAL_FRAME))) {
        		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        		cir.setReturnValue(TypedActionResult.pass(itemStack));
            }
        }
    }
    
    private static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        float f = player.getPitch();
        float g = player.getYaw();
        Vec3d vec3d = player.getEyePos();
        float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
        float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
        float j = -MathHelper.cos(-f * 0.017453292F);
        float k = MathHelper.sin(-f * 0.017453292F);
        float l = i * j;
        float n = h * j;
        double d = 5.0D;
        Vec3d vec3d2 = vec3d.add((double)l * 5.0D, (double)k * 5.0D, (double)n * 5.0D);
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
     }
}
