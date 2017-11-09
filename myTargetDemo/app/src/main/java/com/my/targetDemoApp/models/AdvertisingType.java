package com.my.targetDemoApp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: Timur Voloshin
 * Date: 4/10/15
 * Time: 18:47
 */

public class AdvertisingType implements Parcelable
{
	private int adType;
	private String name;
	private int imageResource;
	private int feedType;
	private boolean isReady;
	private String description;
	private final int slotId;

	public AdvertisingType(int adType, int slotId)
	{
		this.adType = adType;
		this.slotId = slotId;
	}

	public AdvertisingType(String s, String s1)
	{
		this.adType = Integer.parseInt(s);
		this.slotId = Integer.parseInt(s1);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getImageResource()
	{
		return imageResource;
	}

	public void setImageResource(int imageResource)
	{
		this.imageResource = imageResource;
	}

	public boolean isReady()
	{
		return isReady;
	}

	public void setReady(boolean isReady)
	{
		this.isReady = isReady;
	}

	public int getFeedType()
	{
		return feedType;
	}

	public void setType(int feedType)
	{
		this.feedType = feedType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getSlotId()
	{
		return slotId;
	}

	public int getAdType()
	{
		return adType;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(this.adType);
		dest.writeString(this.name);
		dest.writeInt(this.imageResource);
		dest.writeInt(this.feedType);
		dest.writeByte(isReady ? (byte) 1 : (byte) 0);
		dest.writeString(this.description);
		dest.writeInt(this.slotId);
	}

	protected AdvertisingType(Parcel in)
	{
		this.adType = in.readInt();
		this.name = in.readString();
		this.imageResource = in.readInt();
		this.feedType = in.readInt();
		this.isReady = in.readByte() != 0;
		this.description = in.readString();
		this.slotId = in.readInt();
	}

	public static final Creator<AdvertisingType> CREATOR = new Creator<AdvertisingType>()
	{
		public AdvertisingType createFromParcel(Parcel source)
		{
			return new AdvertisingType(source);
		}

		public AdvertisingType[] newArray(int size)
		{
			return new AdvertisingType[size];
		}
	};
}
