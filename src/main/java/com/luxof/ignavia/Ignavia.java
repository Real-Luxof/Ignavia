package com.luxof.ignavia;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ignavia implements ModInitializer {
	public static final String MOD_ID = "ignavia";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Patterns.ezilaitini();
		LOGGER.info("!dlrow cirbaF olleH");
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}