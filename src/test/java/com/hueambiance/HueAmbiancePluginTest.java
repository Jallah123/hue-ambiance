package com.hueambiance;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HueAmbiancePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HueAmbiancePlugin.class);
		RuneLite.main(args);
	}
}