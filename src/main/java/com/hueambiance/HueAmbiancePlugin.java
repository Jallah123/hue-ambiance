package com.hueambiance;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.hueambiance.helpers.HueHelper;
import com.hueambiance.overrides.AmbianceOverrides;
import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.Room;
import java.awt.Color;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemSpawned;
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
	@Inject
	private HueHelper hueHelper;

	private Hue hue;
	private Optional<Room> room = Optional.empty();
	private long lastSkyboxUpdate;
	private boolean colorChanged = false;

	private static final long NANO_SECOND_MULTIPLIER = 1_000_000L;

	@Override
	protected void startUp()
	{
		initHue();
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
		room.ifPresent(r -> {
			final Optional<AmbianceOverride> override = ambianceOverrides.getAll().stream().filter(o -> o.doesOverride(r)).findFirst();
			if (override.isPresent())
			{
				override.get().handleGameTick(tick, r);
				colorChanged = true;
			}
			else
			{
				updateSkybox();
			}
		});
	}

	private void updateSkybox()
	{
		room.ifPresent(r -> {
			final long skyboxRefreshRate = config.skyboxRefreshRate();
			if (skyboxRefreshRate > 0)
			{
				colorChanged = true;
				if (System.nanoTime() - lastSkyboxUpdate > (skyboxRefreshRate * NANO_SECOND_MULTIPLIER))
				{
					lastSkyboxUpdate = System.nanoTime();
					final int skyboxColor = client.getSkyboxColor();
					hueHelper.setColor(r, new Color(skyboxColor));
				}
			}
			else
			{
				if (colorChanged)
				{
					setDefaultHueColor();
				}
			}
		});

	}

	@Subscribe
	public void onNpcSpawned(final NpcSpawned npcSpawned)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().stream()
			.filter(o -> o.doesOverride(r))
			.findFirst()
			.ifPresent(override -> override.handleNpcSpawned(npcSpawned, r)));
	}

	@Subscribe
	public void onNpcChanged(final NpcChanged npcChanged)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().stream()
			.filter(o -> o.doesOverride(r))
			.findFirst()
			.ifPresent(override -> override.handleNpcChanged(npcChanged, r)));
	}

	@Subscribe
	public void onChatMessage(final ChatMessage chatMessage)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().forEach(override -> override.handleChatMessage(chatMessage, r)));
	}

	@Subscribe
	public void onItemSpawned(final ItemSpawned itemSpawned)
	{
		room.ifPresent(r -> ambianceOverrides.getAll().forEach(override -> override.handleItemSpawned(itemSpawned, r)));
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged configChanged)
	{
		final String key = configChanged.getKey();
		switch (key)
		{
			case "ip":
			case "token":
				initHue();
				break;
			case "room":
				initRoom();
				break;
			case "defaultColor":
			case "brightness":
				colorChanged = true;
				break;
		}
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (commandExecuted.getCommand().equals("testlights"))
		{
			if (room.isPresent())
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your smart lights are currently connected.", "");
			}
			else
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your smart lights are not connected.", "");

			}
		}
	}

	@Provides
	HueAmbianceConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(HueAmbianceConfig.class);
	}

	private void initHue()
	{
		if (config.bridgeIp() != null && config.bridgeToken() != null)
		{
			hue = new Hue(config.bridgeIp(), config.bridgeToken());
			initRoom();
		}
	}

	private void initRoom()
	{
		if (config.room() != null)
		{
			room = hue.getRoomByName(config.room());
			setDefaultHueColor();
		}
	}

	private void setDefaultHueColor()
	{
		colorChanged = false;
		room.ifPresent(r -> hueHelper.setColor(r, config.defaultHueColor()));
	}
}
