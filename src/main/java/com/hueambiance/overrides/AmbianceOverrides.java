package com.hueambiance.overrides;

import com.google.common.collect.ImmutableSet;
import com.hueambiance.AmbianceOverride;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmbianceOverrides
{
	@Inject
	private HpOverride hpOverride;

	@Inject
	private PrayerOverride prayerOverride;

	@Inject
	private MessageOverride messageOverride;

	@Inject
	private ItemOverride itemOverride;

	@Inject
	private ZulrahOverride zulrahOverride;

	@Inject
	private TobOverride tobOverride;

	@Inject
	private ToaOverride toaOverride;

	// the order in this set makes sure we have the right priority since only 1 override can be active at once
	public Set<AmbianceOverride> getAll()
	{
		return ImmutableSet.of(
			tobOverride,
			toaOverride,
			hpOverride,
			prayerOverride,
			messageOverride,
			itemOverride,
			zulrahOverride
		);
	}
}
