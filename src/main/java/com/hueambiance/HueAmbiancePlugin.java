package com.hueambiance;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.hueambiance.hue.HueV2;
import com.hueambiance.overrides.AmbianceOverrides;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Hue Ambiance"
)
public class HueAmbiancePlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private HueAmbianceConfig config;
	@Inject
	private AmbianceOverrides ambianceOverrides;

	private HueV2 hueV2;
	private List<String> lights;
	private long lastSkyboxUpdate;

	private static final long NANO_SECOND_MULTIPLIER = 1_000_000L;

	@Override
	protected void startUp()
	{
		hueV2 = new HueV2(config.bridgeIp(), config.bridgeToken());
		lights = hueV2.getLightsForRoom(config.room());
	}

	@Override
	protected void shutDown()
	{
		hueV2 = null;
		lights = Collections.emptyList();
	}

	@Subscribe
	public void onGameTick(final GameTick tick)
	{
		if (!lights.isEmpty())
		{
//			final Optional<AmbianceOverride> override = ambianceOverrides.getAll().stream().filter(o -> o.doesOverride(lights)).findFirst();
//			if (override.isPresent())
//			{
//				override.get().handleGameTick(tick, lights);
//			}
//			else
//			{
				updateSkybox();
//			}
		}
	}

	private void updateSkybox()
	{
		if (!lights.isEmpty())
		{
			final long skyboxRefreshRate = config.skyboxRefreshRate();
			if (skyboxRefreshRate > 0)
			{
				if (System.nanoTime() - lastSkyboxUpdate > (skyboxRefreshRate * NANO_SECOND_MULTIPLIER))
				{
					lastSkyboxUpdate = System.nanoTime();
					final int skyboxColor = client.getSkyboxColor();
					lights.forEach(l -> hueV2.setColor(l, new Color(skyboxColor)));
				}
			}
		}
	}

	//	@Subscribe
//	public void onNpcSpawned(final NpcSpawned npcSpawned)
//	{
//		room.ifPresent(r -> ambianceOverrides.getAll().stream()
//			.filter(o -> o.doesOverride(r))
//			.findFirst()
//			.ifPresent(override -> override.handleNpcSpawned(npcSpawned, r)));
//	}
//
//	@Subscribe
//	public void onNpcChanged(final NpcChanged npcChanged)
//	{
//		room.ifPresent(r -> ambianceOverrides.getAll().stream()
//			.filter(o -> o.doesOverride(r))
//			.findFirst()
//			.ifPresent(override -> override.handleNpcChanged(npcChanged, r)));
//	}
//
//	@Subscribe
//	public void onChatMessage(final ChatMessage chatMessage)
//	{
//		room.ifPresent(r -> ambianceOverrides.getAll().forEach(override -> override.handleChatMessage(chatMessage, r)));
//	}
//
//	@Subscribe
//	public void onItemSpawned(final ItemSpawned itemSpawned)
//	{
//		room.ifPresent(r -> ambianceOverrides.getAll().forEach(override -> override.handleItemSpawned(itemSpawned, r)));
//	}
//
	@Subscribe
	public void onConfigChanged(final ConfigChanged configChanged)
	{
		final String key = configChanged.getKey();
		if (key.equals("ip") || key.equals("token"))
		{
			hueV2 = new HueV2(config.bridgeIp(), config.bridgeToken());
			lights = hueV2.getLightsForRoom(config.room());
		}
		else if (key.equals("room"))
		{
			lights = hueV2.getLightsForRoom(config.room());
		}
	}

	@Provides
	HueAmbianceConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(HueAmbianceConfig.class);
	}
}
