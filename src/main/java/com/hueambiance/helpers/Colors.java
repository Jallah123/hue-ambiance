package com.hueambiance.helpers;

import com.google.common.collect.ImmutableSet;

import java.awt.*;
import java.util.Set;

public class Colors
{
	public static final Color RED = new Color(255, 0, 0);
	public static final Color ORANGE = new Color(255, 200, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color CYAN = new Color(0, 255, 255);
	public static final Color PINK = new Color(255, 175, 175);
	public static final Color MAGENTA = new Color(255, 0, 255);
	public static final Color YELLOW = new Color(255, 255, 0);

	public static final Set<Color> FIRE_WORKS_COLORS = ImmutableSet.of(
		CYAN, ORANGE, MAGENTA, GREEN, RED, PINK, YELLOW
	);
}
