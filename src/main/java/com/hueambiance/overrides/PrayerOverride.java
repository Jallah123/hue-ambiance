package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import static com.hueambiance.helpers.ColorHelper.CYAN;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;

public class PrayerOverride implements AmbianceOverride
{

	@Inject
	private HueAmbianceConfig config;

	@Inject
	private Client client;

	private boolean currentlyAlerting = false;

	@Override
	public boolean doesOverride(final Room room)
	{
		final boolean lowPrayer = checkLowPrayer();
		if (!lowPrayer)
		{
			if (currentlyAlerting)
			{
				room.setState(State.NO_ALERT);
			}
			currentlyAlerting = false;
		}
		return lowPrayer;
	}

	@Override
	public void handleGameTick(final GameTick gameTick, final Room room)
	{
		if (!currentlyAlerting)
		{
			currentlyAlerting = true;
			room.setState(State.builder().color(CYAN).keepCurrentState());
			room.setState(State.LONG_ALERT);
		}
	}

	private boolean checkLowPrayer()
	{
		return config.getPrayerThreshold() > 0 &&
			client.getRealSkillLevel(Skill.PRAYER) > config.getPrayerThreshold() &&
			client.getBoostedSkillLevel(Skill.PRAYER) <= config.getPrayerThreshold();
	}
}
