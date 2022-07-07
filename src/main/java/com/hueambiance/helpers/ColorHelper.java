package com.hueambiance.helpers;

import com.google.common.collect.ImmutableSet;
import static com.hueambiance.helpers.ThreadHelper.executeOnBackGround;
import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import java.time.Duration;
import java.util.Set;

public class ColorHelper
{
	private ColorHelper()
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

	public static void performFireWorks(final Room room, final Runnable callback)
	{
		executeForDuration(() -> FIRE_WORKS_COLORS.forEach(color -> {
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
		), Duration.ofSeconds(5), callback);
	}

	public static void setColorForDuration(final Room room, final Color color, final Duration duration, final Runnable callback)
	{
		executeOnBackGround(() -> {
			room.setState(State.builder().color(color).keepCurrentState());
			try
			{
				Thread.sleep(duration.toMillis());
			}
			catch (InterruptedException e)
			{
				// do nothing
			}
		}, callback);
	}

	private static void executeForDuration(final Runnable task, final Duration duration, Runnable callback)
	{
		executeOnBackGround(() -> {
			final long endTime = System.nanoTime() + duration.toNanos();
			while (endTime > System.nanoTime())
			{
				task.run();
			}
		}, callback);
	}
}
