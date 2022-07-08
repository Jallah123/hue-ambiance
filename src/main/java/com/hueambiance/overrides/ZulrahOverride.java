package com.hueambiance.overrides;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import static com.hueambiance.helpers.Colors.CYAN;
import static com.hueambiance.helpers.Colors.GREEN;
import static com.hueambiance.helpers.Colors.RED;
import static com.hueambiance.helpers.HueHelper.setColor;
import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.Room;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import static net.runelite.api.NpcID.ZULRAH;
import static net.runelite.api.NpcID.ZULRAH_2043;
import static net.runelite.api.NpcID.ZULRAH_2044;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcSpawned;

@Singleton
public class ZulrahOverride implements AmbianceOverride
{
	private static final Set<Integer> ZULRAH_REGIONS = ImmutableSet.of(9007, 9008);
	private static final Map<Integer, Color> ZULRAH_COLORS = ImmutableMap.of(
		ZULRAH, GREEN,
		ZULRAH_2043, RED,
		ZULRAH_2044, CYAN
	);

	@Inject
	private Client client;

	@Inject
	private HueAmbianceConfig config;

	@Override
	public boolean doesOverride(final Room room)
	{
		return config.zulrahEnabled() && atZulrah();
	}

	@Override
	public void handleNpcSpawned(final NpcSpawned npcSpawned, final Room room)
	{
		if (config.zulrahEnabled())
		{
			final NPC npc = npcSpawned.getNpc();
			if (ZULRAH == npc.getId())
			{
				setColor(room, ZULRAH_COLORS.get(ZULRAH));
			}
		}
	}

	@Override
	public void handleNpcChanged(final NpcChanged npcChanged, final Room room)
	{
		if (config.zulrahEnabled())
		{
			if (ZULRAH_COLORS.containsKey(npcChanged.getNpc().getId()))
			{
				setColor(room, ZULRAH_COLORS.get(npcChanged.getNpc().getId()));
			}
		}
	}

	private boolean atZulrah()
	{
		return client.isInInstancedRegion() &&
			ZULRAH_REGIONS.contains(WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID());
	}
}
