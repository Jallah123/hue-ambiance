package com.hueambiance.hue.domain;

import java.util.List;

public class Device
{
	private Metadata metadata;
	private List<Services> services;

	public Metadata getMetadata()
	{
		return metadata;
	}

	public List<Services> getServices()
	{
		return services;
	}
}
