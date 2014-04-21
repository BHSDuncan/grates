package com.xnlogic.grates.helpers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.entities.DayPart;
import com.xnlogic.grates.entities.MonthPart;
import com.xnlogic.grates.exceptions.DayCouldNotBeCreatedFromVertexException;
import com.xnlogic.grates.exceptions.IllegalDatePartValueException;
import com.xnlogic.grates.exceptions.MonthCouldNotBeCreatedFromVertexException;

public class DateGraphHelper 
{
	public static void checkAndCreateIndices(KeyIndexableGraph graph)
	{
		// TODO: Move this into properties file.  Maybe iterate through all keys for indices via properties file
		final String dateTypeKey = "dateType";
		final String refNodeKey = "refNode";

		Set<String> keyNames = new HashSet<String>();
		keyNames.add(dateTypeKey);
		keyNames.add(refNodeKey);
		
		// grab all indexed keys
		Set<String> keys = graph.getIndexedKeys(Vertex.class);
		
		// if the keys don't exist in the index, create then
		Iterator<String> itKeys = keyNames.iterator();
		
		while (itKeys.hasNext())
		{
			if (!keys.contains(itKeys.next()))
			{
				graph.createKeyIndex(itKeys.next(), Vertex.class);
			} // if
		} // while
	} // checkAndCreateIndices
	/*
	public static void checkAndCreateRefNodes(KeyIndexableGraph graph)
	{
		// TODO: Move to properties file as above.
		final String refNodeKey = "refNode";
		
		final String refNodeNextDate = "nextDate";
		
		Set<String> refNodeNames = new HashSet<String>();
		refNodeNames.add(refNodeNextDate);
		
		Iterator<String> itRefNodes = refNodeNames.iterator();
		
		while (itRefNodes.hasNext())
		{
			String value = itRefNodes.next();
			
			// look up the ref node
			Vertex lookup = getSingleVertexByIndexedProp(refNodeKey, value, graph);
			
			// if we don't have such a ref node, create it
			if (lookup == null)
			{
				lookup = graph.addVertex(null);				
				lookup.setProperty(refNodeKey, value);
			} // if
		} // while
	} // checkAndCreateRefNodes
	
	public static Vertex getSingleVertexByIndexedProp(String key, String value, KeyIndexableGraph graph)
	{
		Vertex toReturn = null;
		
		Iterable<Vertex> vertices = graph.getVertices(key, value);
		Iterator<Vertex> itVerts = vertices.iterator();
		
		if (itVerts.hasNext())
		{
			toReturn = itVerts.next();
		} // while

		return toReturn;
	} // getVertexByIndexedProp
	
	public static MonthPart getMonthFromVertex(Vertex v) throws MonthCouldNotBeCreatedFromVertexException
	{
		// TODO: Move to properties file.
		final String PROP_VALUE = "dateValue";

		int monthValue = v.getProperty(PROP_VALUE);

		MonthPart m = null;
		
		try
		{
			m = new MonthPart(monthValue);
		}
		catch (IllegalDatePartValueException e)
		{
			throw new MonthCouldNotBeCreatedFromVertexException("Tried to create month with illegal value.");
		} // try
		
		return m;
	} // getMonthFromVertex

	public static DayPart getDayFromVertex(Vertex v) throws DayCouldNotBeCreatedFromVertexException
	{
		// TODO: Move to properties file.
		final String PROP_VALUE = "dateValue";

		int monthValue = v.getProperty(PROP_VALUE);

		DayPart d = null;
		
		try
		{
			d = new DayPart(monthValue);
		}
		catch (IllegalDatePartValueException e)
		{
			throw new DayCouldNotBeCreatedFromVertexException("Tried to create day with illegal vaue.");
		} // try
		
		return d;
	} // getDayFromVertex

	public static Vertex getMonthVertexFromDayVertex(Vertex day) throws NullPointerException
	{
		if (day == null)
			throw new NullPointerException("day must be a valid Vertex object");
	
		Vertex month = null;
		
		Iterable<Vertex> months = day.getVertices(Direction.OUT, "DAY");
		
		Iterator<Vertex> itMonths = months.iterator();
		
		// a day should only have 1 month attached to it
		while (itMonths.hasNext())
		{
			month = itMonths.next();
			
			assert(!itMonths.hasNext());
		} // while

		itMonths = null;
		
		return month;
	} // getMonthVertexFromDayVertex

	public static Vertex getYearVertexFromMonthVertex(Vertex month) throws NullPointerException
	{
		if (month == null)
			throw new NullPointerException("month must be a valid Vertex object");
	
		Vertex year = null;
		
		Iterable<Vertex> years = month.getVertices(Direction.OUT, "DAY");
		
		Iterator<Vertex> itYears = years.iterator();
		
		// a month should only have 1 year attached to it
		while (itYears.hasNext())
		{
			year = itYears.next();
			
			assert(!itYears.hasNext());
		} // while

		itYears = null;
		
		return year;
	} // getYearVertexFromDayVertex
*/
	// TODO: Implement linked list search.
} // GraphHelper
