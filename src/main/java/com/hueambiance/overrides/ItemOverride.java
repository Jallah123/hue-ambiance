package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import static com.hueambiance.helpers.HueHelper.setColorForDuration;
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

	private boolean active = false;

	@Override
	public boolean doesOverride(final Room room)
	{
		return active;
	}

	@Override
	public void handleItemSpawned(final ItemSpawned itemSpawned, final Room room)
	{
		int itemPriceThreshold = config.itemPriceThreshold();
		if (itemPriceThreshold > 0)
		{
			int price = itemManager.getItemPrice(itemSpawned.getItem().getId());
			if (price >= itemPriceThreshold)
			{
				active = true;
				setColorForDuration(room, config.itemColor(), Duration.ofSeconds(5), () -> active = false);
			}
		}
	}
}
