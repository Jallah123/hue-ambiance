package com.hueambiance.hue.domain;

import java.io.Serializable;

public class Metadata implements Serializable
{
	private String name;
	private String archetype;

	public String getName()
	{
		return name;
	}

	public String getArchetype()
	{
		return archetype;
	}
}
