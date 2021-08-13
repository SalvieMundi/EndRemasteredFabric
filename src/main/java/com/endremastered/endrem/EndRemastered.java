package com.endremastered.endrem;

import com.endremastered.endrem.config.ConfigHandler;
import com.endremastered.endrem.registry.BlockRegistry;
import com.endremastered.endrem.registry.ItemRegistry;
import com.endremastered.endrem.util.LootInjection;
import com.endremastered.endrem.world.gen.OreSpawnHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EndRemastered implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("End Remastered Logger");
	public static final String MOD_ID = "endrem";
	public static final ItemGroup ENDREM_TAB = FabricItemGroupBuilder.build(
			createIdentifier("endrem_tab"),
			() -> new ItemStack(ItemRegistry.ROGUE_EYE));

	public static Identifier createIdentifier(String name) {
		return new Identifier(EndRemastered.MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		ItemRegistry.init();
		BlockRegistry.init();
		LootInjection.init();
		OreSpawnHandler.Init();
		ConfigHandler.load();

		/* Reloads the Configs When the Server Starts */
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			System.out.println("PREPARING FOR RELOAD");
			ConfigHandler.load();
		});
	}
}
