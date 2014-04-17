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
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}
	
	// This test simply creates one date in the graph and verifies it's persisted as expected.
	@Test
	public void testSingleDateCreationAndPersistence() 
	{
		final int YEAR = 2014;
		final int MONTH = 4;
		final int DAY = 15;
		
		GraphDate gd = null;
		
		try
		{
			// this should create and persist a new date
			gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
			fail("Illegal date part in year, month, or day.");
			
		} // try
		
		// check that the date was persisted with the correct relationships
		this.validateEntireDate(YEAR, MONTH, DAY);		
	} 
	
	// This test should show idempotence.
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
		
		gdRetrieve = null;
		
		// check that the date was persisted with the correct relationships (and didn't add any new entities to the graph)
		this.validateEntireDate(YEAR, MONTH, DAY);
	}
		
	@Test
	public void testCreateEntireMonth()
	{
		final int YEAR = 2014;
		final int MONTH = 1;
		final int NUM_DAYS = 31;
		
		// create the month
		this.createMonth(YEAR, MONTH, NUM_DAYS);
		
		// validate the entire month
		this.validateMonth(YEAR, MONTH, NUM_DAYS);
	} // testCreateEntireMonth

	@Test
	public void testCreateEntireYear()
	{
		final int YEAR = 2014;
		
		// create the year (assume not a leap year!)
		this.createYear(YEAR);
		
		// validate the months
		this.validateMonth(YEAR, 1, 31);
		this.validateMonth(YEAR, 2, 28);
		this.validateMonth(YEAR, 3, 31);
		this.validateMonth(YEAR, 4, 30);
		this.validateMonth(YEAR, 5, 31);
		this.validateMonth(YEAR, 6, 30);
		this.validateMonth(YEAR, 7, 31);
		this.validateMonth(YEAR, 8, 31);
		this.validateMonth(YEAR, 9, 30);
		this.validateMonth(YEAR, 10, 31);
		this.validateMonth(YEAR, 11, 30);
		this.validateMonth(YEAR, 12, 31);			
	} // testCreateEntireYear

	private void validateMonth(int yearValue, int monthValue, int totalDays)
	{
		Vertex year = this.getAndValidateYearFromGraph(yearValue);

		Vertex month = this.getAndValidateMonthFromGraph(year, monthValue);
		
		Iterable<Vertex> days = month.getVertices(Direction.OUT, "DAY");
		
		assertNotNull(days);
		
		Iterator<Vertex> itDays = days.iterator();
		
		assertTrue(itDays.hasNext());
		
		int numDays = 0;
		
		while (itDays.hasNext())
		{
			Vertex v = itDays.next();
			//System.out.println("Month " + monthValue + " has day: " + v.getProperty("dateValue"));
			numDays++;
		} // while
		
		assertEquals(totalDays, numDays);
	} // validateMonth
	
	private void createMonth(int year, int month, int numDays)
	{		
		// create an entire month!
		for (int i = 0; i < numDays; i++)
		{
			GraphDate gd = null;

			try
			{
				// this should create and persist a new date
				gd = new GraphDate(year, month, (i + 1), this.graph);
			}
			catch (IllegalDatePartValueException e)
			{
				fail("Illegal date part in year, month, or day.");
			}
			finally
			{
				// just to be safe
				gd = null;
			} // try			
		} // for
	} // createMonth

	private void createYear(int year)
	{
		this.createMonth(year, 1, 31);
		this.createMonth(year, 2, 28);
		this.createMonth(year, 3, 31);
		this.createMonth(year, 4, 30);
		this.createMonth(year, 5, 31);
		this.createMonth(year, 6, 30);
		this.createMonth(year, 7, 31);
		this.createMonth(year, 8, 31);
		this.createMonth(year, 9, 30);
		this.createMonth(year, 10, 31);
		this.createMonth(year, 11, 30);
		this.createMonth(year, 12, 31);
	} // createYear
	
	private Vertex getAndValidateYearFromGraph(int yearValue)
	{
		Iterable<Vertex> years = this.graph.getVertices("dateValue", yearValue);
		
		assertNotNull(years);
		
		Iterator<Vertex> it = years.iterator();
		
		assertNotNull(it);
		assertNotNull(it.hasNext());
		
		Vertex year = null;
		int numYearsFound = 0;
		
		while (it.hasNext())
		{
			Vertex v = it.next();
			
			if ((Integer)v.getProperty("dateValue") == yearValue)
			{	
				year = v;
				numYearsFound++;
				assertEquals("Year", year.getProperty("dateType"));
			} 
			
			// verify we only have the one year
			assertFalse(it.hasNext());
		} // while
				
		assertEquals(1, numYearsFound);
		
		it = null;		
		
		return year;
	} // getAndValidateYear

	private Vertex getAndValidateMonthFromGraph(Vertex year, int monthValue)
	{
		Iterable<Vertex> months = year.getVertices(Direction.OUT, "MONTH");
		
		assertNotNull(months);
		
		Iterator<Vertex> itMonths = months.iterator();
		
		assertNotNull(itMonths);
		assertTrue(itMonths.hasNext());
		
		int numMonthsFound = 0;
		
		Vertex month = null;
		
		while (itMonths.hasNext())
		{
			Vertex v = itMonths.next();
			
			if ((Integer)v.getProperty("dateValue") == monthValue)
			{
				month = v;
				numMonthsFound++;			

				assertEquals("Month", month.getProperty("dateType"));
			} // if
		} // while

		assertEquals(1, numMonthsFound);
		
		itMonths = null;
		
		return month;
	} // getAndValidateMonth

	// this should validate that there is exactly ONE of the triple (y, m, d) in the DB, e.g. we don't have two July 1, 1980's
	private void validateEntireDate(int yearValue, int monthValue, int dayValue)
	{
		Vertex year = this.getAndValidateYearFromGraph(yearValue);
		
		// make sure we have a related MONTH
		Vertex month = this.getAndValidateMonthFromGraph(year, monthValue);
		
		// make sure we have a related DAY
		Iterable<Vertex> days = month.getVertices(Direction.OUT, "DAY");
		
		assertNotNull(days);
		
		Iterator<Vertex> itDays = days.iterator();
		
		assertNotNull(itDays);
		assertNotNull(itDays.hasNext());
		
		int numDaysFound = 0;
		
		Vertex day = null;
		
		while (itDays.hasNext())
		{
			day = itDays.next();
			
			if ((Integer)day.getProperty("dateValue") == dayValue)
			{
				assertEquals("Day", day.getProperty("dateType"));
				numDaysFound++;			
			} // if
		} // while

		itDays = null;
		
		assertEquals(1, numDaysFound);
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
