package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import com.hueambiance.helpers.HueHelper;
import io.github.zeroone3010.yahueapi.Room;
import java.time.Duration;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.game.ItemManager;

@Singleton
public class ItemOverride implements AmbianceOverride
{
	@Inject
	private HueAmbianceConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private HueHelper hueHelper;

	private boolean active = false;

	@Override
	public boolean doesOverride(final Room room)
	{
		return active;
	}

	@Override
	public void handleItemSpawned(final ItemSpawned itemSpawned, final Room room)
	{
		if (config.lowItemPriceThreshold() > 0 || config.mediumItemPriceThreshold() > 0 || config.highItemPriceThreshold() > 0)
		{
			final int price = itemManager.getItemPrice(itemSpawned.getItem().getId());
			if(config.highItemPriceThreshold() > 0 && price >= config.highItemPriceThreshold())
			{
				active = true;
				hueHelper.setColorForDuration(room, config.itemHighColor(), Duration.ofSeconds(5), () -> active = false);
			} else if(config.mediumItemPriceThreshold() > 0 && price >= config.mediumItemPriceThreshold())
			{
				active = true;
				hueHelper.setColorForDuration(room, config.itemMediumColor(), Duration.ofSeconds(5), () -> active = false);
			} else if(config.lowItemPriceThreshold() > 0 && price >= config.lowItemPriceThreshold())
			{
				active = true;
				hueHelper.setColorForDuration(room, config.itemLowColor(), Duration.ofSeconds(5), () -> active = false);
			}
		}
	}
}
