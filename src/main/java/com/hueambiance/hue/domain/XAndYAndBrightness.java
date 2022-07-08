package com.hueambiance.hue.domain;

import java.util.ArrayList;
import java.util.List;

public final class XAndYAndBrightness
{
	final float x;
	final float y;
	final int brightness;

	public XAndYAndBrightness(final float x, final float y, final int brightness)
	{
		this.x = x;
		this.y = y;
		this.brightness = brightness;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public List<Float> getXY()
	{
		final List<Float> xyColor = new ArrayList<>();
		xyColor.add(this.x);
		xyColor.add(this.y);
		return xyColor;
	}

	public int getBrightness()
	{
		return brightness;
	}

}
