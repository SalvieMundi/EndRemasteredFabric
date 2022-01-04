package com.endremastered.endrem.world.ERStructureConfig;

import com.endremastered.endrem.EndRemastered;
import com.endremastered.endrem.world.structures.EndCastle;
import com.endremastered.endrem.world.structures.EndCastlePieces;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ERConfiguredStructure {

    public static StructureFeature<DefaultFeatureConfig> END_CASTLE = new EndCastle(DefaultFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<?, ?> END_CASTLE_CONFIGURED = END_CASTLE.configure(DefaultFeatureConfig.DEFAULT);
    public static final StructurePieceType PIECE = StructurePieceType.register(EndCastlePieces.Piece::new, "end_castle");

    public static void registerConfiguredStructures() {
        FabricStructureBuilder.create(EndRemastered.createIdentifier("end_castle"), END_CASTLE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(59, 37, 542524543)
                .adjustsSurface()
                .register();

        Registry<ConfiguredStructureFeature<?, ?>> myConfigured = (BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE);
        Registry.register(myConfigured,(EndRemastered.createIdentifier("end_castle")), END_CASTLE_CONFIGURED);

        BiomeModifications.create(EndRemastered.createIdentifier("end_castle")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(BiomeKeys.PLAINS), context -> {
            context.getGenerationSettings().addBuiltInStructure(END_CASTLE_CONFIGURED);
        });
        }
    }
