package com.hueambiance.helpers;

import com.google.common.collect.ImmutableSet;
import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Colors
{
	private Colors()
	{
	}

	public static final Color RED = Color.of(255, 0, 0);
	public static final Color ORANGE = Color.of(255, 200, 0);
	public static final Color GREEN = Color.of(0, 255, 0);
	public static final Color CYAN = Color.of(0, 255, 255);
	public static final Color PURPLE = Color.of(161, 52, 235);
	public static final Color PINK = Color.of(255, 175, 175);
	public static final Color MAGENTA = Color.of(255, 0, 255);
	public static final Color YELLOW = Color.of(255, 255, 0);

	public static final Set<Color> FIRE_WORKS_COLORS = ImmutableSet.of(
		CYAN, ORANGE, MAGENTA, GREEN, RED, PINK, YELLOW
	);

	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

	public static void performFireWorks(final Room room, final Duration duration, final Runnable callback)
	{
		backgroundExecutor.submit(() -> {
			long endTime = System.currentTimeMillis() + duration.toMillis();
			while (endTime > System.currentTimeMillis())
			{
				FIRE_WORKS_COLORS.forEach(color -> {
						room.setState(State.builder().color(RED).keepCurrentState());
						try
						{
							Thread.sleep(250);
						}
						catch (InterruptedException e)
						{
							// do nothing
						}
					}
				);
			}
			callback.run();
		});
	}
}
