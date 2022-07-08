package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import static com.hueambiance.helpers.Colors.CYAN;
import static com.hueambiance.helpers.Colors.PURPLE;
import static com.hueambiance.helpers.HueHelper.performFireWorks;
import static com.hueambiance.helpers.HueHelper.setColorForDuration;
import io.github.zeroone3010.yahueapi.Room;
import java.time.Duration;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;

@Singleton
public class MessageOverride implements AmbianceOverride
{
	@Inject
	private Client client;

	@Inject
	private HueAmbianceConfig config;

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
		switch (chatMessage.getType())
		{
			case GAMEMESSAGE:
				handleGameMessage(Text.removeTags(chatMessage.getMessage()), room);
				break;
			case FRIENDSCHATNOTIFICATION:
				handleFriendsChatNotification(Text.removeTags(chatMessage.getMessage()), room);
				break;
		}
	}

	private void handleGameMessage(final String message, final Room room)
	{
		if (message.contains("Congratulations, you've just advanced"))
		{
			if (config.levelUpEnabled())
			{
				startAction();
				performFireWorks(room, stopAction());
			}
		}
		else if (client.getLocalPlayer().getName() != null && message.contains(client.getLocalPlayer().getName()))
		{
			if (message.contains("Enhanced crystal weapon seed"))
			{
				startAction();
				setColorForDuration(room, PURPLE, Duration.ofSeconds(5), stopAction());
			}
			else if (message.contains("Crystal armour seed"))
			{
				startAction();
				setColorForDuration(room, CYAN, Duration.ofSeconds(5), stopAction());
			}
		}
	}

	private void handleFriendsChatNotification(final String message, final Room room)
	{
		if (message.contains("Special loot"))
		{
			startAction();
			setColorForDuration(room, PURPLE, Duration.ofSeconds(15), stopAction());
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
