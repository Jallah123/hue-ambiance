package com.hueambiance.hue;

import com.hueambiance.hue.domain.Child;
import com.hueambiance.hue.domain.Device;
import com.hueambiance.hue.domain.HuePoint;
import com.hueambiance.hue.domain.Services;
import com.hueambiance.hue.service.HueService;
import static com.hueambiance.hue.util.Constants.FIRE_WORKS_COLORS;
import static com.hueambiance.hue.util.Constants.LIGHT;
import static com.hueambiance.hue.util.RgbHelper.toGradientPoint;
import java.awt.Color;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;

public class HueV2
{
	private final HueService hueService;

	public HueV2(final String ip, final String token)
	{
		hueService = new HueService(ip, token);
	}

	public List<String> getLightsForRoom(final String roomName)
	{
		final List<String> deviceIds = getDeviceIdsByRoomName(roomName);

		return deviceIds.stream()
			.map(this::getLightByDevice)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}

	private List<String> getDeviceIdsByRoomName(final String roomName)
	{
		return hueService.getRooms()
			.getData()
			.stream()
			.filter(room -> room.getMetadata().getName().equalsIgnoreCase(roomName))
			.flatMap(room -> room.getChildren().stream().map(Child::getRid))
			.collect(Collectors.toList());
	}

	private Optional<String> getLightByDevice(final String deviceId)
	{
		return hueService.getDeviceById(deviceId).getData()
			.stream()
			.map(Device::getServices)
			.map(services -> services.stream().filter(service -> service.getRtype().equals(LIGHT)).findAny())
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(Services::getRid)
			.findFirst();
	}

	public void setColor(final String id, final Color color, final Duration duration)
	{
		try
		{
			HuePoint currentColor = hueService.getCurrentColor(id);
			hueService.setColor(id, toGradientPoint(color));
			Thread.sleep(duration.toMillis());
			hueService.setColor(id, currentColor);
		}
		catch (Exception e)
		{
		}
	}

	public void setColor(final String id, final Color color)
	{
		hueService.setColor(id, toGradientPoint(color));
	}

	public void turnOff(final String id)
	{
		hueService.setOn(id, false);
	}

	public void turnOn(final String id)
	{
		hueService.setOn(id, true);
	}

	public void setBrightness(final String id, final int brightness)
	{
		hueService.setBrightness(id, brightness);
	}

	public void alert(final String id, final Duration duration)
	{
		try
		{
			double currentBrightness = hueService.getBrightness(id);
			long endTime = System.currentTimeMillis() + duration.toMillis();
			while (endTime > System.currentTimeMillis())
			{
				hueService.setBrightness(id, 25);
				Thread.sleep(400);
				hueService.setBrightness(id, 100);
				Thread.sleep(400);
			}
			hueService.setBrightness(id, currentBrightness);
		}
		catch (Exception e)
		{

		}
	}

	public void fireWorks(final List<String> gradientLights, final List<String> normalLights, final Duration duration)
	{
		try
		{
			Map<String, HuePoint> currentNormalColors = normalLights.stream().collect(toMap(id -> id, hueService::getCurrentColor));
			Map<String, HuePoint> currentGradientColors = gradientLights.stream().collect(toMap(id -> id, hueService::getCurrentColor));
			long endTime = System.currentTimeMillis() + duration.toMillis();
			while (endTime > System.currentTimeMillis())
			{
				for (Color color : FIRE_WORKS_COLORS)
				{
					normalLights.forEach(asd -> setColor(asd, color));
					Thread.sleep(250);
				}
			}
			currentNormalColors.forEach(hueService::setColor);
			currentGradientColors.forEach(hueService::setColor);
		}
		catch (Exception e)
		{

		}
	}
}
