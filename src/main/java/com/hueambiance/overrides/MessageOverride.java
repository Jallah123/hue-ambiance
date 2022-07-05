package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import static com.hueambiance.helpers.Colors.CYAN;
import static com.hueambiance.helpers.Colors.PURPLE;
import static com.hueambiance.helpers.Colors.RED;
import static com.hueambiance.helpers.Colors.performFireWorks;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
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

	private boolean animating = false;

	@Override
	public boolean doesOverride()
	{
		return animating;
	}

	@Override
	@SneakyThrows
	public void handleChatMessage(final ChatMessage chatMessage, final Room room)
	{
		final String message = Text.removeTags(chatMessage.getMessage());
		if (message.contains("-static"))
		{
			animating = true;
			performFireWorks(room, Duration.ofSeconds(5), () -> animating = false);
		}
		if (chatMessage.getType() == ChatMessageType.GAMEMESSAGE)
		{
			if (message.contains("You have a funny feeling like") || message.contains("You feel something weird sneaking into your backpack"))
			{
				// do something
			}
			else if (message.contains("Oh dear, you are dead!"))
			{
				animating = true;
				room.setState(State.builder().color(CYAN).keepCurrentState());
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				room.setState(State.builder().color(RED).keepCurrentState());
				animating = false;
			}
			else if (message.contains("Congratulations, you've just advanced"))
			{
			}
			else if (client.getLocalPlayer().getName() != null && message.contains(client.getLocalPlayer().getName()))
			{
				if (message.contains("enhanced"))
				{
					room.setState(State.builder().color(PURPLE).keepCurrentState());
				}
				else if (message.contains("armour"))
				{
					room.setState(State.builder().color(CYAN).keepCurrentState());
				}
			}
		}
		else if (chatMessage.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION)
		{
			if (message.contains("Special loot"))
			{
				room.setState(State.builder().color(PURPLE).keepCurrentState());
			}
		}
	}
}
