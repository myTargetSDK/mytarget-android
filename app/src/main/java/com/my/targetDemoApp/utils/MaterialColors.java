package com.my.targetDemoApp.utils;

import java.util.ArrayList;

public class MaterialColors
{
	private ArrayList<Integer> colorArrayList;

	public MaterialColors()
	{
		colorArrayList = new ArrayList<>();
		colorArrayList.add(0xFF3F51B5); // INDIGO
		colorArrayList.add(0xFF009688); // TEAL
		colorArrayList.add(0xFFF44336); // RED
		colorArrayList.add(0xFF4CAF50); // GREEN
		colorArrayList.add(0xFF9C27B0); // PURPLE
	}

	public int get(int index)
	{
		if (index > colorArrayList.size() - 1)
		{
			index %= colorArrayList.size();
		}
		return colorArrayList.get(index);
	}
}
