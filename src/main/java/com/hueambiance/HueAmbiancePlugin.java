package com.hueambiance;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.hueambiance.overrides.AmbianceOverrides;
import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcSpawned;
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

	private Hue hue;
	private Optional<Room> room;
	private long lastSkyboxUpdate;

	@Override
	protected void startUp()
	{
		hue = new Hue(config.bridgeIp(), config.bridgeToken());
		room = hue.getRoomByName(config.room());
	}

	@Override
	protected void shutDown()
	{
		hue = null;
		room = Optional.empty();
	}

	@Subscribe
	public void onGameTick(final GameTick tick)
	{
		if (ambianceOverrides.getAll().stream().noneMatch(AmbianceOverride::doesOverride))
		{
			updateSkybox();
		}
	}

	@Subscribe
	public void onNpcSpawned(final NpcSpawned npcSpawned)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().stream()
			.filter(AmbianceOverride::doesOverride)
			.findFirst()
			.ifPresent(override -> override.handleNpcSpawned(npcSpawned, r)));
	}

	@Subscribe
	public void onNpcChanged(final NpcChanged npcChanged)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().stream()
			.filter(AmbianceOverride::doesOverride)
			.findFirst()
			.ifPresent(override -> override.handleNpcChanged(npcChanged, r)));
	}

	@Subscribe
	public void onChatMessage(final ChatMessage chatMessage)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().forEach(override -> override.handleChatMessage(chatMessage, r)));
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged configChanged)
	{
		final String key = configChanged.getKey();
		if (key.equals("ip") || key.equals("token"))
		{
			hue = new Hue(config.bridgeIp(), config.bridgeToken());
		}
		room = hue.getRoomByName(config.room());
	}

	private void updateSkybox()
	{
		room.ifPresent(r -> {
			if (System.currentTimeMillis() - lastSkyboxUpdate > 1000L)
			{
				lastSkyboxUpdate = System.currentTimeMillis();
				final int skyboxColor = client.getSkyboxColor();
				r.getLights().forEach(light -> light.setState(State.builder().color(Color.of(skyboxColor)).keepCurrentState()));
			}
		});
	}

	@Provides
	HueAmbianceConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(HueAmbianceConfig.class);
	}
}
