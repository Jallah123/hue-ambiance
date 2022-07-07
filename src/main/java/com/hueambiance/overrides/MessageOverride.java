package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import static com.hueambiance.helpers.ColorHelper.CYAN;
import static com.hueambiance.helpers.ColorHelper.PURPLE;
import static com.hueambiance.helpers.ColorHelper.performFireWorks;
import static com.hueambiance.helpers.ColorHelper.setColorForDuration;
import io.github.zeroone3010.yahueapi.Room;
import java.time.Duration;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.SneakyThrows;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;

@Singleton
public class MessageOverride implements AmbianceOverride
{
	@Inject
	private Client client;

	private boolean active = false;

	@Override
	public boolean doesOverride(final Room room)
	{
		return active;
	}

	@Override
	@SneakyThrows
	public void handleChatMessage(final ChatMessage chatMessage, final Room room)
	{
		final String message = Text.removeTags(chatMessage.getMessage());
		if (chatMessage.getType() == ChatMessageType.GAMEMESSAGE)
		{
			if (message.contains("You have a funny feeling like") || message.contains("You feel something weird sneaking into your backpack"))
			{
				// do something
			}
			else if (message.contains("Congratulations, you've just advanced"))
			{
				startAction();
				performFireWorks(room, stopAction());
			}
			else if (client.getLocalPlayer().getName() != null && message.contains(client.getLocalPlayer().getName()))
			{
				if (message.contains("enhanced"))
				{
					startAction();
					setColorForDuration(room, PURPLE, Duration.ofSeconds(5), stopAction());
				}
				else if (message.contains("armour"))
				{
					startAction();
					setColorForDuration(room, CYAN, Duration.ofSeconds(5), stopAction());
				}
			}
		}
		else if (chatMessage.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION)
		{
			if (message.contains("Special loot"))
			{
				startAction();
				setColorForDuration(room, PURPLE, Duration.ofSeconds(15), stopAction());
			}
		}
	}

	private void startAction()
	{
		active = true;
	}

	private Runnable stopAction()
	{
		return () -> active = false;
	}
}
