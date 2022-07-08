package com.hueambiance.hue.util;

import com.hueambiance.hue.domain.HueColor;
import com.hueambiance.hue.domain.HuePoint;
import com.hueambiance.hue.domain.HueXY;
import com.hueambiance.hue.domain.XAndYAndBrightness;
import java.awt.Color;

public class RgbHelper
{
	public static HuePoint toGradientPoint(final Color color)
	{
		XAndYAndBrightness xy = colorToXY(color);
		return new HuePoint(
			new HueColor(
				new HueXY(xy.getX(), xy.getY())
			)
		);
	}

	private static XAndYAndBrightness colorToXY(final Color color)
	{
		final float red = color.getRed();
		final float green = color.getGreen();
		final float blue = color.getBlue();
		final double r = gammaCorrection(red);
		final double g = gammaCorrection(green);
		final double b = gammaCorrection(blue);
		final double rgbX = r * 0.664511f + g * 0.154324f + b * 0.162028f;
		final double rgbY = r * 0.283881f + g * 0.668433f + b * 0.047685f;
		final double rgbZ = r * 0.000088f + g * 0.072310f + b * 0.986039f;
		final float x = (float) (rgbX / (rgbX + rgbY + rgbZ));
		final float y = (float) (rgbY / (rgbX + rgbY + rgbZ));
		return new XAndYAndBrightness(x, y, (int) (rgbY * 255f));
	}

	private static double gammaCorrection(float component)
	{
		return (component > 0.04045f) ? Math.pow((component + 0.055f) / (1.0f + 0.055f), 2.4f) : (component / 12.92f);
	}
}
