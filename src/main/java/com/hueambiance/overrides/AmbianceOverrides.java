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
	private ZulrahOverride zulrahOverride;

	@Inject
	private MessageOverride messageOverride;

	public Set<AmbianceOverride> getAll()
	{
		return ImmutableSet.of(
			zulrahOverride,
			messageOverride
		);
	}
}
