package com.endremastered.endrem.world.gen;

import com.endremastered.endrem.EndRemastered;
import com.endremastered.endrem.registry.BlockRegistry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public class OreSpawnHandler {
    
    private static ConfiguredFeature<?, ?> BLACKSTONE = Feature.ORE
            .configure(new OreFeatureConfig(
                    new BlockMatchRuleTest(Blocks.BLACKSTONE),
                    BlockRegistry.END_CRYSTAL_ORE.getDefaultState(),
                    3))
            .range(new RangeDecoratorConfig(
                    UniformHeightProvider.create(YOffset.fixed(0), YOffset.fixed(100))))
            .spreadHorizontally()
            .repeat(15);

    private static ConfiguredFeature<?, ?> POLISHED_BLACKSTONE_BRICKS = Feature.ORE
            .configure(new OreFeatureConfig(
                    new BlockMatchRuleTest(Blocks.POLISHED_BLACKSTONE_BRICKS),
                    BlockRegistry.END_CRYSTAL_ORE.getDefaultState(),
                    3))
            .range(new RangeDecoratorConfig(
                    UniformHeightProvider.create(YOffset.fixed(0), YOffset.fixed(100))))
            .spreadHorizontally()
            .repeat(25);

    private static ConfiguredFeature<?, ?> CRACKED_POLISHED_BLACKSTONE_BRICKS = Feature.ORE
            .configure(new OreFeatureConfig(
                    new BlockMatchRuleTest(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS),
                    BlockRegistry.END_CRYSTAL_ORE.getDefaultState(),
                    3))
            .range(new RangeDecoratorConfig(
                    UniformHeightProvider.create(YOffset.fixed(0), YOffset.fixed(100))))
            .spreadHorizontally()
            .repeat(30);

    public static void Init() {
        RegistryKey<ConfiguredFeature<?, ?>> oreBlackstoneNether = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                EndRemastered.createIdentifier("blackstone_ore"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreBlackstoneNether.getValue(), BLACKSTONE);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, oreBlackstoneNether);

        RegistryKey<ConfiguredFeature<?, ?>> orePBlackstoneNether = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                EndRemastered.createIdentifier("pblackstone_ore"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, orePBlackstoneNether.getValue(), POLISHED_BLACKSTONE_BRICKS);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, orePBlackstoneNether);

        RegistryKey<ConfiguredFeature<?, ?>> oreCPBlackstoneNether = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                EndRemastered.createIdentifier("cpblackstone_ore"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreCPBlackstoneNether.getValue(), CRACKED_POLISHED_BLACKSTONE_BRICKS);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, oreCPBlackstoneNether);
    }

}
