package com.hueambiance.helpers;

import static com.hueambiance.helpers.Colors.FIRE_WORKS_COLORS;
import static com.hueambiance.helpers.ThreadHelper.executeOnBackGround;
import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import java.time.Duration;

public class HueHelper
{
	private HueHelper()
	{
	}

	public static void performFireWorks(final Room room, final Runnable callback)
	{
		executeForDuration(() -> FIRE_WORKS_COLORS.forEach(color -> {
				room.setState(State.builder().color(color).keepCurrentState());
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

	public static void setColor(final Room room, final java.awt.Color color)
	{
		executeOnBackGround(() -> room.setState(State.builder().color(Color.of(color)).keepCurrentState()));
	}

	public static void setAlert(final Room room, final java.awt.Color color)
	{
		executeOnBackGround(() -> {
			room.setState(State.LONG_ALERT);
			room.setState(State.builder().color(Color.of(color)).keepCurrentState());
		});
	}

	public static void stopAlert(final Room room)
	{
		executeOnBackGround(() -> room.setState(State.NO_ALERT));
	}

	public static void setColorForDuration(final Room room, final java.awt.Color color, final Duration duration, final Runnable callback)
	{
		executeOnBackGround(() -> {
			room.setState(State.builder().color(Color.of(color)).keepCurrentState());
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
