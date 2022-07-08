package com.hueambiance.hue.domain;

import java.util.List;

public class Room
{
	private Metadata metadata;
	private List<Child> children;

	public Metadata getMetadata()
	{
		return metadata;
	}

	public List<Child> getChildren()
	{
		return children;
	}
}
