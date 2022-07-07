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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("Hue Ambiance")
public interface HueAmbianceConfig extends Config
{
	@ConfigSection(
		name = "Hue bridge",
		description = "Technical settings for the bridge",
		position = 1
	)
	String bridgeSection = "bridgeSection";

	@ConfigSection(
		name = "Configuration",
		description = "Functional configuration",
		position = 2
	)
	String configSection = "configSection";

	@ConfigItem(
		keyName = "enabled",
		name = "Enabled",
		description = "Enables Hue Ambiance",
		position = 0,
		section = bridgeSection
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ip",
		name = "Bridge ip",
		description = "Bridge ip",
		position = 1,
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
		position = 2,
		section = bridgeSection
	)
	default String bridgeToken()
	{
		return "";
	}

	@ConfigItem(
		keyName = "room",
		name = "Room",
		description = "The name of the room that needs to be controlled",
		position = 3,
		section = bridgeSection
	)
	default String room()
	{
		return "";
	}

	@ConfigItem(
		keyName = "prayer",
		name = "Prayer Threshold",
		description = "The amount of prayer points to send a notification at. A value of 0 will disable notification.",
		position = 1,
		section = configSection
	)
	default int getPrayerThreshold()
	{
		return 0;
	}

}
