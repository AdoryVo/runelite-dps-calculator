package com.dpscalculator;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "DPS Calculator"
)
public class DpsCalculatorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private DpsCalculatorConfig config;

	@Override
	protected void startUp() throws Exception
	{
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "WHAT IS UP MAN", null);
	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Provides
	DpsCalculatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DpsCalculatorConfig.class);
	}
}
