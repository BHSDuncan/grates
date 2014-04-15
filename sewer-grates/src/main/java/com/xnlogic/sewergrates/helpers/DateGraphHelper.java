package com.xnlogic.sewergrates.helpers;

import java.util.Set;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.sewergrates.entities.Day;
import com.xnlogic.sewergrates.entities.Month;
import com.xnlogic.sewergrates.exceptions.DayCouldNotBeCreatedFromVertexException;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;
import com.xnlogic.sewergrates.exceptions.MonthCouldNotBeCreatedFromVertexException;

public class DateGraphHelper 
{
	public static void checkAndCreateIndices(KeyIndexableGraph graph)
	{
		// TODO: Move this into properties file.
		final String dateTypeKey = "dateType";
		
		// grab all indexed keys
		Set<String> keys = graph.getIndexedKeys(Vertex.class);
		
		// if the key doesn't exist in the index, create it
		if (!keys.contains(dateTypeKey))
		{
			graph.createKeyIndex(dateTypeKey, Vertex.class);
		} // if
	} // checkAndCreateIndices
	
	public static Month getMonthFromVertex(Vertex v) throws MonthCouldNotBeCreatedFromVertexException
	{
		// TODO: Move to properties file.
		final String PROP_VALUE = "dateValue";

		int monthValue = v.getProperty(PROP_VALUE);

		Month m = null;
		
		try
		{
			m = new Month(monthValue);
		}
		catch (IllegalDatePartValueException e)
		{
			throw new MonthCouldNotBeCreatedFromVertexException("Tried to create month with illegal value.");
		} // try
		
		return m;
	} // getMonthFromVertex

	public static Day getDayFromVertex(Vertex v) throws DayCouldNotBeCreatedFromVertexException
	{
		// TODO: Move to properties file.
		final String PROP_VALUE = "dateValue";

		int monthValue = v.getProperty(PROP_VALUE);

		Day d = null;
		
		try
		{
			d = new Day(monthValue);
		}
		catch (IllegalDatePartValueException e)
		{
			throw new DayCouldNotBeCreatedFromVertexException("Tried to create day with illegal vaue.");
		} // try
		
		return d;
	} // getMonthFromVertex

} // GraphHelper
