package com.hueambiance.helpers;

import com.google.common.collect.ImmutableSet;
import io.github.zeroone3010.yahueapi.Color;
import java.util.Set;

public class Colors
{
	public static final Color RED = Color.of(255, 0, 0);
	public static final Color ORANGE = Color.of(255, 200, 0);
	public static final Color GREEN = Color.of(0, 255, 0);
	public static final Color CYAN = Color.of(0, 255, 255);
	public static final Color PINK = Color.of(255, 175, 175);
	public static final Color MAGENTA = Color.of(255, 0, 255);
	public static final Color YELLOW = Color.of(255, 255, 0);

	public static final Set<Color> FIRE_WORKS_COLORS = ImmutableSet.of(
		CYAN, ORANGE, MAGENTA, GREEN, RED, PINK, YELLOW
	);
}
