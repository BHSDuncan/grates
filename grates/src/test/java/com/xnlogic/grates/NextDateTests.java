package com.xnlogic.grates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

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

// TODO: Implement these properly. These are a relic from earlier thinking and incomplete, and will thereforefail.
public class NextDateTests 
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
/*
	@Test
	public void testFirstNewNextDateSameYearAndMonth()
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
		}
		catch (DataIntegrityException die)
		{
			fail(die.getMessage());
			
		} // try
		
//		gd.setNextDate(gd2);
		
		// persist
//		gd.save();
		
		gd = null;
		
		// check graph representation
		Vertex day = this.getDayVertexFromYear(YEAR, MONTH, DAY);
		
		Iterable<Vertex> nextDays = day.getVertices(Direction.OUT, "NEXT");
		
		assertTrue(nextDays.iterator().hasNext());
		
		Iterator<Vertex> itVertex = nextDays.iterator();
		
		while (itVertex.hasNext())
		{
			Vertex v = itVertex.next();
			
			assertEquals(DAY_2, v.getProperty("dateValue"));
			
			// should only be 1 here
			assertFalse(itVertex.hasNext());
		} // while
	}
	
	@Test
	public void testNewMiddleNextDateSameYearAndMonth()
	{
		final int YEAR = 2014;
		final int MONTH = 4;
		final int DAY = 15;

		final int DAY_2 = 20;

		final int DAY_3 = 17;
		
		// setup the first next date
		this.testFirstNewNextDateSameYearAndMonth();
		
		// insert a new next date
		GraphDate gd = null;
		GraphDate gd2 = null;
		
		try
		{
			gd = new GraphDate(YEAR, MONTH, DAY, this.graph);
			gd2 = new GraphDate(YEAR, MONTH, DAY_3, this.graph);
		}
		catch (IllegalDatePartValueException e)
		{
			fail("Illegal date part in year, month, or day.");
		}
		catch (DataIntegrityException die)
		{
			fail(die.getMessage());
			
		} // try
		
//		gd.setNextDate(gd2);
		
		// persist
//		gd.save();
		
		gd = null;

		// check graph representation
		Vertex day = this.getDayVertexFromYear(YEAR, MONTH, DAY);
		Vertex day2 = null;
		
		Iterable<Vertex> nextDays = day.getVertices(Direction.OUT, "NEXT");
		
		assertTrue(nextDays.iterator().hasNext());
		
		Iterator<Vertex> itVertex = nextDays.iterator();
		
		// check newly created "next" day is next in line
		while (itVertex.hasNext())
		{
			day2 = itVertex.next();
			
			assertEquals(DAY_3, day2.getProperty("dateValue"));
			
			// should only be 1 here
			assertFalse(itVertex.hasNext());
		} // while

		itVertex = null;
		
		// ...and now check that the "old" next day is still in the right order
		nextDays = null;
		
		nextDays = day2.getVertices(Direction.OUT, "NEXT");
		
		assertTrue(nextDays.iterator().hasNext());
		
		itVertex = nextDays.iterator();
		
		while (itVertex.hasNext())
		{
			Vertex v = itVertex.next();
			
			assertEquals(DAY_2, v.getProperty("dateValue"));
			
			// should only be 1 here
			assertFalse(itVertex.hasNext());
		} // while

	}

	private Vertex getDayVertexFromYear(int yearValue, int monthValue, int dayValue)
	{
		Iterable<Vertex> years = this.graph.getVertices("dateValue", yearValue);
		
		Iterator<Vertex> it = years.iterator();
		Vertex year = null;
		
		while (it.hasNext())
		{
			year = it.next();			
		} // while
				
		it = null;
		
		if ((Integer)year.getProperty("dateValue") != yearValue)
		{
			return null;
		} // if
		
		// make sure we have MONTH relationships
		Iterable<Edge> monthEdges = year.getEdges(Direction.OUT, "MONTH");
		
		Iterator<Edge> itMonthEdges = monthEdges.iterator();

		Vertex month = null;
		Vertex monthTemp = null;
		
		while (itMonthEdges.hasNext())
		{
			Edge e = itMonthEdges.next();
			
			monthTemp = e.getVertex(Direction.IN);
			
			if ((Integer)monthTemp.getProperty("dateValue") == monthValue)
			{
				month = monthTemp;
				break;
			} // if
		} // while
		
		itMonthEdges = null;

		if (month == null)
		{
			return null;
		} // if
		
		// make sure we have DAY relationships
		Iterable<Edge> dayEdges = month.getEdges(Direction.OUT, "DAY");
		
		Iterator<Edge> itDayEdges = dayEdges.iterator();
		
		Vertex day = null;
		Vertex dayTemp = null;
		
		while (itDayEdges.hasNext())
		{
			Edge e = itDayEdges.next();
		
			dayTemp = e.getVertex(Direction.IN);
			
			if ((Integer)dayTemp.getProperty("dateValue") == dayValue)
			{
				day = dayTemp;
				break;
			} // if
		} // while

		itDayEdges = null;

		return day;
	}
*/
} // NextDateTests
