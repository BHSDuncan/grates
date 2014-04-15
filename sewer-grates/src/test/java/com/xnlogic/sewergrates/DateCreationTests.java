package com.xnlogic.sewergrates;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.xnlogic.sewergrates.datatypes.GraphDate;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;

public class DateCreationTests 
{
	private KeyIndexableGraph graph = null;
	
	@Before
	public void setup()
	{
		this.graph = new TinkerGraph();
	} // setup
	
	@After
	public void takedown()
	{	
		this.graph.shutdown();
		this.graph = null;
	}
	
	// This library was created for all years from 1AD and on
	@Test(expected=IllegalDatePartValueException.class)
	public void testIllegalDateCreation() throws IllegalDatePartValueException
	{
		final int YEAR = 0;
		final int MONTH = 4;
		final int DAY = 15;
		
		GraphDate gd = null;
		
		gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}
	
	@Test
	public void testSingleDateCreationAndPersistence() 
	{
		final int YEAR = 2014;
		final int MONTH = 4;
		final int DAY = 15;
		
		GraphDate gd = null;
		
		try
		{
			gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
			fail("Illegal date part in year, month, or day.");
			
		} // try
		
		// persist
		gd.save();
		
		// check that the date was persisted with the correct relationships
		this.validateEntireDate(YEAR, MONTH, DAY);		
	} 
	
	// This should show idempotence.
	@Test
	public void testSingleDateCreationWithMultipleSaves()
	{
		final int YEAR = 2014;
		final int MONTH = 4;
		final int DAY = 15;
		
		GraphDate gd = null;
		
		try
		{
			gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
			fail("Illegal date part in year, month, or day.");
			
		} // try
		
		// persist
		gd.save();

		gd = null;

		// get the same date back and try to save it again
		GraphDate gdRetrieve = null;

		try
		{
			gdRetrieve = new GraphDate(YEAR, MONTH, DAY, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
			fail("Illegal date part in year, month, or day.");
			
		} // try
		
		// persist again
		gdRetrieve.save();
		
		gdRetrieve = null;
		
		// check that the date was persisted with the correct relationships (and didn't add any new entities to the graph)
		this.validateEntireDate(YEAR, MONTH, DAY);
	}
	
	@Test
	public void testFirstNewNextDate()
	{
		final int YEAR = 2014;
		final int MONTH = 4;
		final int DAY = 15;

		final int DAY_2 = 20;
		
		GraphDate gd = null;
		GraphDate gd2 = null;
		
		try
		{
			gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
			gd2 = new GraphDate(YEAR, MONTH, DAY_2, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
			fail("Illegal date part in year, month, or day.");
			
		} // try
		
		gd.setNextDate(gd2);
		
		// persist
		gd.save();
		
	}
	
	private void validateEntireDate(int yearValue, int monthValue, int dayValue)
	{
		Iterable<Vertex> years = this.graph.getVertices("dateValue", yearValue);
		
		assertNotNull(years);
		
		Iterator<Vertex> it = years.iterator();
		
		assertNotNull(it);
		assertNotNull(it.hasNext());
		
		Vertex year = null;
		
		while (it.hasNext())
		{
			year = it.next();
			
			assertEquals("Year", year.getProperty("dateType"));
			assertEquals(yearValue, year.getProperty("dateValue"));
			
			assertFalse(it.hasNext());
		} // while
				
		it = null;
		
		// make sure we have MONTH relationships
		Iterable<Edge> monthEdges = year.getEdges(Direction.OUT, "MONTH");
		
		assertNotNull(monthEdges);
		
		Iterator<Edge> itMonthEdges = monthEdges.iterator();
		
		assertNotNull(itMonthEdges);
		assertTrue(itMonthEdges.hasNext());

		List<Vertex> months = new ArrayList<Vertex>();

		int numEdges = 0;
		
		while (itMonthEdges.hasNext())
		{
			Edge e = itMonthEdges.next();
			
			numEdges++;
			
			assertEquals(e.getLabel(), "MONTH");
			
			months.add(e.getVertex(Direction.IN));
			
			assertFalse(itMonthEdges.hasNext());
		} // while

		assertEquals(1, numEdges);
		
		itMonthEdges = null;
		
		// verify month from year
		Vertex month = null;
		
		for (int i = 0; i < months.size(); i++)
		{
			month = months.get(i);
					
			assertEquals(monthValue, month.getProperty("dateValue"));
			assertEquals("Month", month.getProperty("dateType"));
		} // for
		
		// make sure we have DAY relationships
		Iterable<Edge> dayEdges = month.getEdges(Direction.OUT, "DAY");
		
		assertNotNull(dayEdges);
		
		Iterator<Edge> itDayEdges = dayEdges.iterator();
		
		assertNotNull(itDayEdges);
		assertNotNull(itDayEdges.hasNext());

		List<Vertex> days = new ArrayList<Vertex>();
		
		numEdges = 0;
		
		while (itDayEdges.hasNext())
		{
			Edge e = itDayEdges.next();
		
			numEdges++;
			
			assertEquals("DAY", e.getLabel());
			
			days.add(e.getVertex(Direction.IN));
			
			assertFalse(itDayEdges.hasNext());
		} // while

		itDayEdges = null;
		
		assertEquals(1, numEdges);
		
		// verify day from month
		
		for (Vertex day : days)
		{
			assertEquals(dayValue, day.getProperty("dateValue"));
			assertEquals("Day", day.getProperty("dateType"));
		} // for		
	} // validateEntireDate

	private int getNumberOfSpecificEdgesFromVertex(Vertex v, String edgeLabel)
	{
		int numEdges = 0;
		
		// make sure we have MONTH relationships
		Iterable<Edge> edges = v.getEdges(Direction.OUT, edgeLabel);
		
		assertNotNull(edges);
		
		Iterator<Edge> itEdges = edges.iterator();
		
		assertNotNull(itEdges);
		assertTrue(itEdges.hasNext());
		
		while (itEdges.hasNext())
		{
			Edge e = itEdges.next();
			
			numEdges++;
			
			assertEquals(e.getLabel(), edgeLabel);
		} // while

		itEdges = null;
		
		return numEdges;
	} // getNumberOfSpecificEdgesFromVertex
} // DateCreationTests
