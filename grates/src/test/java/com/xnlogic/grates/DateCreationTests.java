package com.xnlogic.grates;

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
import com.xnlogic.grates.datatypes.GraphDate;
import com.xnlogic.grates.exceptions.DataIntegrityException;
import com.xnlogic.grates.exceptions.IllegalDatePartValueException;

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
	public void testIllegalDateCreation() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 0;
		final int MONTH = 4;
		final int DAY = 15;
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}
	
	@Test(expected=IllegalDatePartValueException.class)
	public void testNonLeapYearDateException() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2013;
		final int MONTH = 2;
		final int DAY = 29;
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test(expected=IllegalDatePartValueException.class)
	public void testLeapYearDateException() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2012;
		final int MONTH = 2;
		final int DAY = 30;
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test(expected=IllegalDatePartValueException.class)
	public void testIllegalDay() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2011;
		final int MONTH = 5;
		final int DAY = 40;
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test(expected=IllegalDatePartValueException.class)
	public void testIllegalMonth() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2011;
		final int MONTH = 15;
		final int DAY = 20;
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test(expected=NullPointerException.class)
	public void testNewGraphDateWithNullGraph() throws IllegalDatePartValueException, NullPointerException, DataIntegrityException
	{
		final int YEAR = 2012;
		final int MONTH = 2;
		final int DAY = 30;
		
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, null);
	}

	@Test(expected=DataIntegrityException.class)
	public void testTwoYearsInGraphWithSameValue() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2014;
		final int MONTH = 3;
		final int DAY = 15;
		final String DATE_TYPE = "Year";
		
		// create duplicate years
		Vertex v1 = this.graph.addVertex(null);
		v1.setProperty("dateValue", YEAR);
		v1.setProperty("dateType", DATE_TYPE);

		Vertex v2 = this.graph.addVertex(null);
		v2.setProperty("dateValue", YEAR);
		v2.setProperty("dateType", DATE_TYPE);

		// attempt to instantiate a GraphDate object for the offending year
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test(expected=DataIntegrityException.class)
	public void testTwoMonthsInGraphWithSameYear() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2014;
		final int MONTH = 3;
		final int DAY = 15;
		final String YEAR_TYPE = "Year";
		final String MONTH_TYPE = "Month";
		final String MONTH_LABEL = "MONTH";
		
		// create duplicate months for a year
		Vertex v1 = this.graph.addVertex(null);
		v1.setProperty("dateValue", YEAR);
		v1.setProperty("dateType", YEAR_TYPE);

		Vertex v2 = this.graph.addVertex(null);
		v2.setProperty("dateValue", MONTH);
		v2.setProperty("dateType", MONTH_TYPE);

		Vertex v3 = this.graph.addVertex(null);
		v3.setProperty("dateValue", MONTH);
		v3.setProperty("dateType", MONTH_TYPE);

		this.graph.addEdge(null, v1, v2, MONTH_LABEL);
		this.graph.addEdge(null, v1, v3, MONTH_LABEL);
		
		// attempt to instantiate a GraphDate object for the offending month
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test(expected=DataIntegrityException.class)
	public void testTwoDaysInGraphWithSameMonth() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2014;
		final int MONTH = 3;
		final int DAY = 15;
		final String YEAR_TYPE = "Year";
		final String MONTH_TYPE = "Month";
		final String DAY_TYPE = "Day";
		final String MONTH_LABEL = "MONTH";
		final String DAY_LABEL = "DAY";
		
		// create duplicate months for a year
		Vertex v1 = this.graph.addVertex(null);
		v1.setProperty("dateValue", YEAR);
		v1.setProperty("dateType", YEAR_TYPE);

		Vertex v2 = this.graph.addVertex(null);
		v2.setProperty("dateValue", MONTH);
		v2.setProperty("dateType", MONTH_TYPE);

		Vertex v3 = this.graph.addVertex(null);
		v3.setProperty("dateValue", DAY);
		v3.setProperty("dateType", DAY_TYPE);

		Vertex v4 = this.graph.addVertex(null);
		v4.setProperty("dateValue", DAY);
		v4.setProperty("dateType", DAY_TYPE);

		this.graph.addEdge(null, v1, v2, MONTH_LABEL);
		this.graph.addEdge(null, v2, v3, DAY_LABEL);
		this.graph.addEdge(null, v2, v4, DAY_LABEL);
		
		// attempt to instantiate a GraphDate object for the offending month
		GraphDate gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
	}

	@Test
	public void testGraphDateComparison()
	{
		final int YEAR = 2012;
		final int MONTH = 2;
		final int DAY = 20;
		
		GraphDate gd1 = null;
		GraphDate gd2 = null;
		
		try
		{
			gd1 = new GraphDate(YEAR, MONTH, DAY, this.graph);
			gd2 = new GraphDate(YEAR, MONTH, DAY, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
		}
		catch (DataIntegrityException die)
		{
			fail(die.getMessage());
			
		} // try
		
		assertEquals(gd1, gd2);
		
		boolean testEquals = gd1.equals(gd2);
		
		assertTrue(testEquals);
		
		testEquals = gd1.equals(null);
		
		assertFalse(testEquals);
		
		testEquals = gd1.equals(gd1);
		
		assertTrue(testEquals);
		
		testEquals = gd1.equals(new Object());
		
		assertFalse(testEquals);
		
		assertTrue(gd1.hashCode() == gd2.hashCode());
	}
	
	@Test
	public void testLeapYearDateLegal() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2012;
		final int MONTH = 2;
		final int DAY = 29;
		
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
		}
		catch (DataIntegrityException die)
		{
			fail(die.getMessage());			
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
		}
		catch (DataIntegrityException die)
		{
			fail(die.getMessage());			
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
		}
		catch (DataIntegrityException die)
		{
			fail(die.getMessage());
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

	@Test
	public void testGetISODate() throws IllegalDatePartValueException, DataIntegrityException
	{
		final int YEAR = 2014;
		final int MONTH_1 = 3;
		final int DAY_1 = 2;
		final String EXPECTED_DATE_1 = "2014-03-02";
		
		GraphDate gd = new GraphDate(YEAR, MONTH_1, DAY_1, this.graph);
				
		assertEquals(EXPECTED_DATE_1, gd.getISODate());
		
		final int MONTH_2 = 10;
		final String EXPECTED_DATE_2 = "2014-10-02";
		
		GraphDate gd2 = new GraphDate(YEAR, MONTH_2, DAY_1, this.graph);
				
		assertEquals(EXPECTED_DATE_2, gd2.getISODate());

		final int DAY_2 = 20;
		final String EXPECTED_DATE_3 = "2014-10-20";
		
		GraphDate gd3 = new GraphDate(YEAR, MONTH_2, DAY_2, this.graph);
				
		assertEquals(EXPECTED_DATE_3, gd3.getISODate());

	}
	
	//
	// HELPER METHODS
	//
	
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
			catch (DataIntegrityException die)
			{
				fail(die.getMessage());
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
		// look-up the year from the graph
		Iterable<Vertex> years = this.graph.getVertices("dateValue", yearValue);
		
		assertNotNull(years);
		
		Iterator<Vertex> it = years.iterator();
		
		// make sure we've found at least one
		assertNotNull(it);
		assertNotNull(it.hasNext());
		
		Vertex year = null;
		int numYearsFound = 0;
		
		// now make sure we only have one of the year we're after
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
		
		// send back the year we found
		return year;
	} // getAndValidateYear

	// Based on the year vertex passed in, grab the specified month vertex while making sure there are no duplicate months for that year
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
} // DateCreationTests
