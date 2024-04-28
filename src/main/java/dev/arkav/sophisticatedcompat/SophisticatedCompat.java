package dev.arkav.sophisticatedcompat;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("sophisticatedcompat")
public class SophisticatedCompat {
	public static final Logger LOGGER = LogUtils.getLogger();

	public SophisticatedCompat() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	public void setup(FMLClientSetupEvent e) {
		LOGGER.info("Sophisticated Etched Compat Initialized");
	}
}
