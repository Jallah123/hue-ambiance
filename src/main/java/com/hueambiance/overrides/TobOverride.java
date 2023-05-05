package com.hueambiance.overrides;

import com.hueambiance.AmbianceOverride;
import com.hueambiance.HueAmbianceConfig;
import com.hueambiance.helpers.HueHelper;
import io.github.zeroone3010.yahueapi.Room;
import net.runelite.api.Client;
import net.runelite.api.events.GameObjectSpawned;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Singleton
public class TobOverride implements AmbianceOverride {

    private static final List<Integer> REWARD_CHEST_IDS = Arrays.asList(33086, 33087, 33088, 33089, 33090);
    private static final int OWN_TOB_PURPLE_CHEST = 32993;
    private static final int OTHER_TOB_PURPLE_CHEST = 32991;

    private boolean active = false;

    @Inject
    private Client client;

    @Inject
    private HueAmbianceConfig config;

    @Inject
    private HueHelper hueHelper;

    @Override
    public boolean doesOverride(final Room room)
    {
        return active;
    }

    @Override
    public void handleGameObjectSpawned(final GameObjectSpawned event, final Room room)
    {
        final int objId = event.getGameObject().getId();
        if (REWARD_CHEST_IDS.contains(objId))
        {
            final int impostorId = client.getObjectDefinition(objId).getImpostor().getId();

            if (impostorId == OWN_TOB_PURPLE_CHEST)
            {
                active = true;
                hueHelper.setColorForDuration(room, config.tobColor(), Duration.ofSeconds(15), () -> active = false);
            }
            else if (impostorId == OTHER_TOB_PURPLE_CHEST)
            {
                if (config.tobShowOthersPurple())
                {
                    active = true;
                    hueHelper.setColorForDuration(room, config.tobOthersColor(), Duration.ofSeconds(15), () -> active = false);
                }
            }
        }
    }
}
