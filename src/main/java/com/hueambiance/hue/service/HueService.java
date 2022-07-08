package com.hueambiance.hue.service;

import com.google.gson.Gson;
import com.hueambiance.hue.domain.Devices;
import com.hueambiance.hue.domain.HueDimmingWrapper;
import com.hueambiance.hue.domain.HuePoint;
import com.hueambiance.hue.domain.HuePointDataWrapper;
import com.hueambiance.hue.domain.Rooms;
import static com.hueambiance.hue.util.HttpClientHelper.getUnsafeOkHttpClient;
import java.io.IOException;
import java.util.Optional;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HueService
{
	private static final String BASE_PATH = "/clip/v2/resource";

	private final OkHttpClient okHttpClient;
	private final String baseUrl;
	private final Headers defaultHeaders;

	private static final Gson GSON = new Gson();

	public HueService(final String ip, final String token)
	{
		baseUrl = "https://" + ip + BASE_PATH;
		this.okHttpClient = getUnsafeOkHttpClient();
		defaultHeaders = new Headers.Builder().add("hue-application-key", token).build();
	}

	public Rooms getRooms()
	{
		return doGetRequest(baseUrl + "/room", Rooms.class).orElse(new Rooms());
	}

	public Devices getDeviceById(final String deviceId)
	{
		return doGetRequest(baseUrl + "/device/" + deviceId, Devices.class)
			.orElse(new Devices());
	}

	public HuePoint getCurrentColor(final String lightId)
	{
		return doGetRequest(baseUrl + "/light/" + lightId, HuePointDataWrapper.class)
			.orElse(new HuePointDataWrapper())
			.getData().get(0);
	}

	public void setColor(final String lightId, final HuePoint request)
	{
		doPutRequest(baseUrl + "/light/" + lightId, GSON.toJson(request));
	}

	public void setOn(final String id, final boolean on)
	{
		doPutRequest(baseUrl + "/light/" + id, "{\"on\": {\"on\": " + on + " }}");
	}

	public void setBrightness(final String id, final double brightness)
	{
		doPutRequest(baseUrl + "/light/ " + id, "{\"dimming\": {\"brightness\": " + brightness + "}}");
	}

	public double getBrightness(final String id)
	{
		return doGetRequest(baseUrl + "/light/ " + id, HueDimmingWrapper.class)
			.orElse(new HueDimmingWrapper())
			.getData().get(0).getDimming().getBrightness();
	}

	private <T> Optional<T> doGetRequest(final String url, final Class<T> type)
	{
		try
		{
			final Request request = new Request.Builder().url(url).headers(defaultHeaders).build();
			final ResponseBody body = okHttpClient.newCall(request).execute().body();
			if (body != null)
			{
				return Optional.of(GSON.fromJson(body.string(), type));
			}
		}
		catch (IOException e)
		{
		}
		return Optional.empty();
	}

	private void doPutRequest(final String url, final String body)
	{
		try
		{
			RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
			final Request request = new Request.Builder().method("PUT", requestBody).headers(defaultHeaders).url(url).build();
			Response execute = okHttpClient.newCall(request).execute();
			execute.body();
		}
		catch (IOException e)
		{
		}
	}
}
