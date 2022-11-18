package com.hueambiance.helpers;

import static com.hueambiance.helpers.Colors.FIRE_WORKS_COLORS;
import static com.hueambiance.helpers.ThreadHelper.executeOnBackGround;

import com.hueambiance.HueAmbianceConfig;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.State;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HueHelper
{

	private static final int MAX_BRIGHTNESS = 254;

	@Inject
	private HueAmbianceConfig config;

	public void performFireWorks(final Room room, final Runnable callback)
	{
		executeForDuration(() -> FIRE_WORKS_COLORS.forEach(color -> {
				setColor(room, color);
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

	public void setColor(final Room room, final java.awt.Color color)
	{
		executeOnBackGround(() -> room.setState(State.builder().xy(toXY(color)).brightness(getBrightness()).keepCurrentState()));
	}

	public void setAlert(final Room room, final java.awt.Color color)
	{
		executeOnBackGround(() -> {
			room.setState(State.LONG_ALERT);
			setColor(room, color);
		});
	}

	public void stopAlert(final Room room)
	{
		executeOnBackGround(() -> room.setState(State.NO_ALERT));
	}

	public void setColorForDuration(final Room room, final java.awt.Color color, final Duration duration, final Runnable callback)
	{
		executeOnBackGround(() -> {
			setColor(room, color);
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

	private void executeForDuration(final Runnable task, final Duration duration, Runnable callback)
	{
		executeOnBackGround(() -> {
			final long endTime = System.nanoTime() + duration.toNanos();
			while (endTime > System.nanoTime())
			{
				task.run();
			}
		}, callback);
	}

	private int getBrightness()
	{
		return (int) (config.brightness() / 100.0 * MAX_BRIGHTNESS);
	}

	private List<Float> toXY(final java.awt.Color color)
	{
		final double r = gammaCorrection(color.getRed());
		final double g = gammaCorrection(color.getGreen());
		final double b = gammaCorrection(color.getBlue());
		final double rgbX = r * 0.664511f + g * 0.154324f + b * 0.162028f;
		final double rgbY = r * 0.283881f + g * 0.668433f + b * 0.047685f;
		final double rgbZ = r * 0.000088f + g * 0.072310f + b * 0.986039f;
		final float x = (float) (rgbX / (rgbX + rgbY + rgbZ));
		final float y = (float) (rgbY / (rgbX + rgbY + rgbZ));
		return new ArrayList<Float>(){{
			this.add(x);
			this.add(y);
		}};
	}

	private double gammaCorrection(float component)
	{
		return (component > 0.04045f) ? Math.pow((component + 0.055f) / (1.0f + 0.055f), 2.4f) : (component / 12.92f);
	}
}
