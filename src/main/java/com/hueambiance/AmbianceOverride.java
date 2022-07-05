package com.hueambiance;

import io.github.zeroone3010.yahueapi.Room;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcSpawned;

public interface AmbianceOverride
{
	boolean doesOverride();

	default void handleNpcSpawned(final NpcSpawned npcSpawned, final Room room)
	{
	}

	default void handleNpcChanged(final NpcChanged npcChanged, final Room room)
	{
	}

	default void handleChatMessage(final ChatMessage chatMessage, final Room room)
	{
	}
}
