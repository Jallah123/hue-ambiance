/*
 * Copyright (c) 2017, Devin French <https://github.com/devinfrench>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.hueambiance;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Units;

@ConfigGroup("Hue Ambiance")
public interface HueAmbianceConfig extends Config
{
	@ConfigSection(
		name = "Hue bridge",
		description = "Technical settings for the bridge",
		position = 0
	)
	String bridgeSection = "bridgeSection";

	@ConfigSection(
		name = "Configuration",
		description = "Functional configuration",
		position = 1
	)
	String configSection = "configSection";

	@ConfigSection(
		name = "Colors",
		description = "Color configuration",
		position = 2
	)
	String colorSection = "colorSection";

	@ConfigSection(
			name = "Raids",
			description = "Raid configuration",
			position = 3
	)
	String raidsSection = "raidsSections";

	@ConfigItem(
		keyName = "ip",
		name = "Bridge ip",
		description = "Bridge ip",
		position = 0,
		section = bridgeSection
	)
	default String bridgeIp()
	{
		return "";
	}

	@ConfigItem(
		keyName = "token",
		name = "Bridge token",
		description = "Bridge token",
		position = 1,
		section = bridgeSection
	)
	default String bridgeToken()
	{
		return "";
	}

	@ConfigItem(
		keyName = "room",
		name = "Room name",
		description = "The name of the room that needs to be controlled",
		position = 2,
		section = bridgeSection
	)
	default String room()
	{
		return "";
	}

	@ConfigItem(
			keyName = "brightness",
			name = "Brightness",
			description = "Brightness of the lamps in the room in percentages",
			position = 0,
			section = configSection
	)
	@Units(Units.PERCENT)
	default int brightness()
	{
		return 75;
	}

	@ConfigItem(
		keyName = "refreshRate",
		name = "Skybox refresh rate",
		description = "Amount of milliseconds that need to be between skybox updates. A call to the bridge will be done every refresh. A value of 0 will disable skybox refresh",
		position = 1,
		section = configSection
	)
	@Units(Units.MILLISECONDS)
	default int skyboxRefreshRate()
	{
		return 1000;
	}

	@ConfigItem(
		keyName = "hp",
		name = "HP threshold",
		description = "The amount of hp to send a notification at. A value of 0 will disable notification.",
		position = 2,
		section = configSection
	)
	default int hpThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "prayer",
		name = "Prayer threshold",
		description = "The amount of prayer points to send a notification at. A value of 0 will disable notification.",
		position = 3,
		section = configSection
	)
	default int prayerThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "itemLow",
		name = "Low item price threshold",
		description = "The price an item must be in order to trigger a notification. A value of 0 will disable notification.",
		position = 4,
		section = configSection
	)
	default int lowItemPriceThreshold()
	{
		return 250_000;
	}

	@ConfigItem(
			keyName = "itemMedium",
			name = "Medium item price threshold",
			description = "The price an item must be in order to trigger a notification. A value of 0 will disable notification.",
			position = 5,
			section = configSection
	)
	default int mediumItemPriceThreshold()
	{
		return 500_000;
	}

	@ConfigItem(
			keyName = "itemHigh",
			name = "High item price threshold",
			description = "The price an item must be in order to trigger a notification. A value of 0 will disable notification.",
			position = 6,
			section = configSection
	)
	default int highItemPriceThreshold()
	{
		return 1_000_000;
	}

	@ConfigItem(
		keyName = "levelUp",
		name = "Level up notifier",
		description = "Enable firework animation on level up",
		position = 7,
		section = configSection
	)
	default boolean levelUpEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "zulrah",
		name = "Zulrah custom ambiance",
		description = "Enables custom ambiance colors for Zulrah",
		position = 8,
		section = configSection
	)
	default boolean zulrahEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "defaultColor",
		name = "Default",
		description = "Default color to use when skybox is not enabled",
		position = 0,
		section = colorSection
	)
	default Color defaultHueColor()
	{
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
		keyName = "hpColor",
		name = "HP",
		description = "Color when low hp",
		position = 1,
		section = colorSection
	)
	default Color lowHpColor()
	{
		return Color.RED;
	}

	@Alpha
	@ConfigItem(
		keyName = "prayerColor",
		name = "Prayer",
		description = "Color when low prayer",
		position = 2,
		section = colorSection
	)
	default Color lowPrayerColor()
	{
		return Color.CYAN;
	}

	@Alpha
	@ConfigItem(
		keyName = "ItemLowColor",
		name = "Item low",
		description = "Color when low value drop received",
		position = 3,
		section = colorSection
	)
	default Color itemLowColor()
	{
		return Color.CYAN;
	}

	@Alpha
	@ConfigItem(
			keyName = "itemMediumColor",
			name = "Item medium",
			description = "Color when medium value drop received",
			position = 4,
			section = colorSection
	)
	default Color itemMediumColor()
	{
		return Color.GREEN;
	}

	@Alpha
	@ConfigItem(
			keyName = "itemHighColor",
			name = "Item high",
			description = "Color when high value drop received",
			position = 5,
			section = colorSection
	)
	default Color itemHighColor()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
		keyName = "cgEnhanced",
		name = "CG enhanced seed",
		description = "Color when enhanced weapon seed",
		position = 7,
		section = colorSection
	)
	default Color cgEnhanced()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
		keyName = "cgArmour",
		name = "CG armour seed",
		description = "Color when armour seed is received",
		position = 8,
		section = colorSection
	)
	default Color cgArmour()
	{
		return Color.CYAN;
	}
	@Alpha
	@ConfigItem(
			keyName = "coxOtherPurple",
			name = "Cox show others drop",
			description = "Also display color when it's not your drop.",
			position = 0,
			section = raidsSection
	)
	default boolean coxShowOthersPurple()
	{
		return true;
	}
	@Alpha
	@ConfigItem(
			keyName = "coxColor",
			name = "Cox drop color",
			description = "Color when drop received",
			position = 1,
			section = raidsSection
	)
	default Color coxColor()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
			keyName = "coxOthersColor",
			name = "Cox other drop color",
			description = "Color when someone else received a drop",
			position = 1,
			section = raidsSection
	)
	default Color coxOthersColor()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
			keyName = "tobOtherPurple",
			name = "Tob show others drop",
			description = "Also display color when it's not your drop.",
			position = 4,
			section = raidsSection
	)
	default boolean tobShowOthersPurple()
	{
		return true;
	}
	@Alpha
	@ConfigItem(
			keyName = "tobColor",
			name = "Tob drop color",
			description = "Color when drop received",
			position = 5,
			section = raidsSection
	)
	default Color tobColor()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
			keyName = "tobOthersColor",
			name = "Tob other drop color",
			description = "Color when someone else received a drop",
			position = 6,
			section = raidsSection
	)
	default Color tobOthersColor()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
			keyName = "toaOtherPurple",
			name = "Toa show others drop",
			description = "Also display color when it's not your drop.",
			position = 7,
			section = raidsSection
	)
	default boolean toaShowOthersPurple()
	{
		return true;
	}
	@Alpha
	@ConfigItem(
			keyName = "toaColor",
			name = "Toa drop color",
			description = "Color when drop received",
			position = 8,
			section = raidsSection
	)
	default Color toaColor()
	{
		return new Color(161, 52, 235);
	}

	@Alpha
	@ConfigItem(
			keyName = "toaOthersColor",
			name = "Toa other drop color",
			description = "Color when someone else received a drop",
			position = 9,
			section = raidsSection
	)
	default Color toaOthersColor()
	{
		return new Color(161, 52, 235);
	}
}
