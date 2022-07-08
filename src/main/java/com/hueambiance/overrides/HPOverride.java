package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import static com.hueambiance.helpers.HueHelper.setAlert;
import static com.hueambiance.helpers.HueHelper.stopAlert;
import io.github.zeroone3010.yahueapi.Room;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;

@Singleton
public class HPOverride implements AmbianceOverride
{

	@Inject
	private HueAmbianceConfig config;

	@Inject
	private Client client;

	private boolean currentlyAlerting = false;

	@Override
	public boolean doesOverride(final Room room)
	{
		final boolean lowHP = checkLowHP();
		if (!lowHP)
		{
			if (currentlyAlerting)
			{
				stopAlert(room);
			}
			currentlyAlerting = false;
		}
		return lowHP;
	}

	@Override
	public void handleGameTick(final GameTick gameTick, final Room room)
	{
		if (!currentlyAlerting)
		{
			currentlyAlerting = true;
			setAlert(room, config.lowHpColor());
		}
	}

	private boolean checkLowHP()
	{
		return config.hpThreshold() > 0 &&
			client.getRealSkillLevel(Skill.HITPOINTS) > config.hpThreshold() &&
			client.getBoostedSkillLevel(Skill.HITPOINTS) + client.getVarbitValue(Varbits.NMZ_ABSORPTION) <= config.hpThreshold();
	}
}
