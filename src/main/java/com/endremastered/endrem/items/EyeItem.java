package com.endremastered.endrem.items;

import com.endremastered.endrem.EndRemastered;
import com.endremastered.endrem.blocks.AncientPortalFrame;
import com.endremastered.endrem.config.ConfigHandler;
import com.endremastered.endrem.mixin.accessor.EyeOfEnderEntityAccessorMixin;
import com.endremastered.endrem.mixin.accessor.PlayerEntityAccessorMixin;
import com.endremastered.endrem.properties.FrameProperties;
import com.endremastered.endrem.registry.BlockRegistry;
import com.endremastered.endrem.util.StructureLocator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.lang.ref.WeakReference;
import java.util.List;

public class EyeItem extends Item {
    public EyeItem() {
        super(new FabricItemSettings().fireproof().maxCount(16).rarity(Rarity.EPIC).group(EndRemastered.ENDREM_TAB));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText(String.format("item.endrem.%s.description", this.asItem().toString())));
    }

    /* Action when used on a frame */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        BlockState blockstate = world.getBlockState(blockpos);
        double minX = 0.0;
        double maxX = 0.0;
        double minZ = 0.0;
        double maxZ = 0.0;
        int minY = 0;

        Boolean frameHasEye = false;

        if (blockstate.isOf(BlockRegistry.ANCIENT_PORTAL_FRAME)) {
            frameHasEye = AncientPortalFrame.hasEye(blockstate);
        } else if (blockstate.isOf(Blocks.END_PORTAL_FRAME)) {
            frameHasEye = blockstate.get(EndPortalFrameBlock.EYE);
        } else {
            return ActionResult.PASS;
        }

        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (!frameHasEye) {
            FrameProperties frameProperties = FrameProperties.getFrameProperty(context.getStack().getItem());
            BlockState newBlockState = BlockRegistry.ANCIENT_PORTAL_FRAME.getDefaultState();
            newBlockState = newBlockState.with(HorizontalFacingBlock.FACING, blockstate.get(HorizontalFacingBlock.FACING));
            newBlockState = newBlockState.with(AncientPortalFrame.EYE, frameProperties);

            if (!AncientPortalFrame.IsFrameAlreadyUsed(world, newBlockState, blockpos)) {
                Block.pushEntitiesUpBeforeBlockChange(blockstate, newBlockState, world, blockpos);
                world.setBlockState(blockpos, newBlockState, 2);
                world.updateComparators(blockpos, BlockRegistry.ANCIENT_PORTAL_FRAME);
                context.getStack().decrement(1);
                world.syncWorldEvent(1503, blockpos, 0);
                BlockPattern.Result blockpattern$patternhelper = AncientPortalFrame.getCompletedFramePattern().searchAround(world, blockpos);

                if (blockpattern$patternhelper != null) {
                    BlockPos blockpos1 = blockpattern$patternhelper.getFrontTopLeft().add(-3, 0, -3);
                    minX = blockpos1.getX();
                    maxX = blockpos1.getX() + 3;
                    minZ = blockpos1.getZ();
                    maxZ = blockpos1.getZ() + 3;
                    minY = blockpos1.getY();
                    for (int i = 0; i < 3; ++i) {
                		for (int j = 0; j < 3; ++j) {
                			world.setBlockState(blockpos1.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), 2);
                		}
                	}
                    world.syncGlobalEvent(1038, blockpos1.add(1, 0, 1), 0);
                    
                    if (FabricLoader.getInstance().isModLoaded("endrem")) {
                    	ServerCommandSource playerSource = context.getPlayer().getCommandSource().withLevel(2).withSilent();
                    	CommandManager commandManager = world.getServer().getCommandManager();
                    	
                    	commandManager.execute(playerSource, "/portal make_portal 3 3 minecraft:the_end 0 160 0");
                    	commandManager.execute(playerSource, "/execute as @e[nbt={dimensionTo:\"minecraft:the_end\"},distance=0..25,limit=1] run portal set_portal_nbt {Pos:[" + (minX + ((maxX-minX)/2.0)) + "," + ((double)minY+0.1) + "," + (minZ + ((maxZ-minZ)/2.0) + "]}"));
                    	commandManager.execute(playerSource, "/execute as @e[nbt={dimensionTo:\"minecraft:the_end\"},distance=0..25,limit=1] run portal set_portal_nbt {axisWX:0.0d,axisHX:-90.0d,axisWY:0.0d,axisHY:0.0d,axisWZ:-90.0d,axisHZ:0.0d}");
                    	commandManager.execute(playerSource, "/execute as @e[nbt={dimensionTo:\"minecraft:the_end\"},distance=0..25,limit=1] run portal set_portal_nbt {commandsOnTeleported:[\"/effect give @s minecraft:slow_falling 12\"]}");
                    	commandManager.execute(playerSource, "/fill " + (int)minX + " " + minY + " " + (int)minZ + " " + (int)maxX + " " + (minY+1) + " " + (int)maxZ + " minecraft:air replace minecraft:end_portal");
                    }
                }
                
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        } else if (blockstate.isOf(Blocks.END_PORTAL_FRAME)) {
            BlockState newBlockState = blockstate.with(EndPortalFrameBlock.EYE, false);
            world.setBlockState(blockpos, newBlockState, 2);
            world.spawnEntity(new ItemEntity(world, blockpos.getX(), blockpos.getY() + 1, blockpos.getZ(), new ItemStack(Items.ENDER_EYE)));
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

    /* Locate Structures */
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        BlockHitResult hitResult = raycast(worldIn, playerIn, RaycastContext.FluidHandling.NONE);
        boolean lookingAtFrame = false;

        BlockState state = worldIn.getBlockState(hitResult.getBlockPos());  //IDK IF IT STILL WORKS
        if (state.isOf(BlockRegistry.ANCIENT_PORTAL_FRAME)) {
            lookingAtFrame = true;
        }

        if (lookingAtFrame) {
            return TypedActionResult.pass(itemstack);
        } else {
            playerIn.setCurrentHand(handIn);
            if (worldIn instanceof ServerWorld) {
                BlockPos blockpos = ((ServerWorld) worldIn).getChunkManager().getChunkGenerator().locateStructure((ServerWorld) worldIn, StructureLocator.getStructureToLocate(ConfigHandler.EYES_LOCATE_STRUCTURE.toLowerCase()), playerIn.getBlockPos(), 100, false);
                if (blockpos != null) {
                    EyeOfEnderEntity eyeOfEnderEntity = new EyeOfEnderEntity(worldIn, playerIn.getX(), playerIn.getBodyY(0.5D), playerIn.getZ());
                    eyeOfEnderEntity.setItem(itemstack);
                    eyeOfEnderEntity.initTargetPos(blockpos);
                    ((EyeOfEnderEntityAccessorMixin) eyeOfEnderEntity).setDropsItem(true);

                    worldIn.spawnEntity(eyeOfEnderEntity);
                    if (playerIn instanceof ServerPlayerEntity) {
                        Criteria.USED_ENDER_EYE.trigger((ServerPlayerEntity) playerIn, blockpos);
                    }

                    worldIn.playSound(null, playerIn.getBlockPos(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (float) (Math.random() * 0.4F + 0.8F));
                    worldIn.syncWorldEvent(null, 1003, playerIn.getBlockPos(), 0);

                    if (!((PlayerEntityAccessorMixin) playerIn).getAbilities().creativeMode) {
                        itemstack.decrement(1);
                    }

                    playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
                    playerIn.swingHand(handIn, true);
                    return TypedActionResult.success(itemstack);
                }
            }
            return TypedActionResult.consume(itemstack);
        }
    }

}
