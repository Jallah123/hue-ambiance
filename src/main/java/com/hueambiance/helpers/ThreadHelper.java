package com.hueambiance.helpers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHelper
{
	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

	public static void executeOnBackGround(final Runnable runnable, final Runnable callback)
	{
		backgroundExecutor.submit(() -> {
			runnable.run();
			callback.run();
		});
	}
}
