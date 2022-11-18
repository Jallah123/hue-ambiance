package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import com.hueambiance.helpers.HueHelper;
import io.github.zeroone3010.yahueapi.Room;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;

@Singleton
public class PrayerOverride implements AmbianceOverride
{

	@Inject
	private HueAmbianceConfig config;

	@Inject
	private Client client;

	@Inject
	private HueHelper hueHelper;

	private boolean currentlyAlerting = false;

	@Override
	public boolean doesOverride(final Room room)
	{
		final boolean lowPrayer = checkLowPrayer();
		if (!lowPrayer)
		{
			if (currentlyAlerting)
			{
				hueHelper.stopAlert(room);
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
			hueHelper.setAlert(room, config.lowPrayerColor());
		}
	}

	private boolean checkLowPrayer()
	{
		return config.prayerThreshold() > 0 &&
			client.getRealSkillLevel(Skill.PRAYER) > config.prayerThreshold() &&
			client.getBoostedSkillLevel(Skill.PRAYER) <= config.prayerThreshold();
	}
}
