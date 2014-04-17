package com.xnlogic.sewergrates.datatypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.sewergrates.entities.*;
import com.xnlogic.sewergrates.exceptions.DataIntegrityException;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;
import com.xnlogic.sewergrates.helpers.DateGraphHelper;

public class GraphDate extends BaseGraphDate 
{	
	private GraphDate nextDate = null;
	
	private final KeyIndexableGraph graph;
	
	public GraphDate(int yearValue, int monthValue, int dayValue, KeyIndexableGraph graph) throws IllegalDatePartValueException, NullPointerException
	{
		if (graph == null)
			throw new NullPointerException("graph must be a valid KeyIndexableGraph object.");
		
		// validate the date (valiDate!)
		this.valiDate(yearValue, monthValue, dayValue);
		
		this.year = new YearPart(yearValue);
		this.month = new MonthPart(monthValue);
		this.day = new DayPart(dayValue);
		
		this.graph = graph;
				
		// need to see if there is already a representation of this date in the graph; however note that we need ALL of the date parts above in order
		// to identify whether or not they exist (actually, we just need the year for the year, the year and the month for the month, and 
		// the year, the month and the day for the day)
		try
		{
			this.syncDateNodeBackings();
			//System.out.println("Wrote out year: " + yearValue + ", month: " + monthValue + ", day: " + dayValue);
		}
		catch (DataIntegrityException e)
		{
			
		} // try
	} // constructor

	// We do any graph persistence here as whether this graph date is "transient" or not to the user/client, we'll still need this date at some point
	private void syncDateNodeBackings() throws DataIntegrityException
	{
		// TODO: Move this into JAR "onload"		
		DateGraphHelper.checkAndCreateIndices(this.graph);

		boolean createdNewDay = false; 	// use this to determine if we need to update the "next date" linked list, i.e. whenever we create a new day
		
		// flags to create relationships
		boolean createYearToMonth = false;
		boolean createMonthToDay = false;
		
		int numNodes = 0; // for data integrity checking

		// get year
		
		// try to lookup the exact year via the index; if no such year exists, we need to create all of y, m, d
		Iterable<Vertex> vYears = this.graph.getVertices("dateType", "Year");
		Vertex vYear = null;
		
		if (vYears != null && vYears.iterator().hasNext())
		{
			Iterator<Vertex> iterYears = vYears.iterator();
			
			while (iterYears.hasNext())
			{
				Vertex v = (Vertex)iterYears.next();
				
				if ((Integer)v.getProperty("dateValue") == this.year.getValue())
				{
					vYear = v;
					numNodes++;
					
					// could break, but, should check data integrity
				} // if
			} // while
			
			if (numNodes > 1)
			{
				// found more than one year with the same value; we have a problem...
				throw new DataIntegrityException("More than one year of the same value found!");
			}
			else if (numNodes == 0)
			{
				// we couldn't find the month for the year, so, create it!
				vYear= this.year.save(this.graph);
			} // if
		}
		else
		{
			// save the year
			vYear = this.year.save(this.graph);
		} // if
		
		// check month
		Iterable<Vertex> vMonths = vYear.getVertices(Direction.OUT, "MONTH");
		Vertex vMonth = null;
		
		numNodes = 0; 
		
		if (vMonths != null && vMonths.iterator().hasNext())
		{
			Iterator<Vertex> iterMonths = vMonths.iterator();
			
			while (iterMonths.hasNext())
			{
				Vertex v = (Vertex)iterMonths.next();
				
				if ((Integer)v.getProperty("dateValue") == this.month.getValue())
				{
					vMonth = v;
					numNodes++;
					
					// could break, but, should check data integrity
				} // if
			} // while
			
			if (numNodes > 1)
			{
				// found more than one month with the same year and same value; we have a problem...
				throw new DataIntegrityException("More than one month of the same value for the same year found!");
			}
			else if (numNodes == 0)
			{
				// we couldn't find the month for the year, so, create it!
				vMonth = this.month.save(this.graph);
				
				createYearToMonth = true;
			} // if
		}
		else
		{
			// save the month
			vMonth = this.month.save(this.graph);
			
			createYearToMonth = true;
		} // if

		numNodes = 0;
		
		// check day
		Iterable<Vertex> vDays = vMonth.getVertices(Direction.OUT, "DAY");
		Vertex vDay = null;
		
		if (vDays != null && vDays.iterator().hasNext())
		{
			Iterator<Vertex> iterDays = vDays.iterator();
			
			while (iterDays.hasNext())
			{
				Vertex v = (Vertex)iterDays.next();
				
				if ((Integer)v.getProperty("dateValue") == this.day.getValue())
				{
					vDay = v;
					numNodes++;

					// could break, but, should check data integrity
				} // if
			} // while
			
			if (numNodes > 1)
			{
				// found more than one month with the same year and same value; we have a problem...
				throw new DataIntegrityException("More than one day of the same value for the same month and year found!");
			}
			else if (numNodes == 0)
			{
				// we couldn't find the day for the month, so, create it!
				vDay = this.day.save(this.graph);

				createMonthToDay = true;				

				createdNewDay = true;
			} // if
		}
		else
		{
			// create (save) the day! ...because saving the day sounds much cooler.
			
			// also, set the vertex for the day to the local variable so we can use this later in the "next date" feature
			vDay = this.day.save(this.graph);
			createMonthToDay = true;				

			createdNewDay = true;
		} // if
		
		// create any necessary edges
		if (createYearToMonth)
		{
			this.graph.addEdge(null, vYear, vMonth, "MONTH");
		} // if
		
		if (createMonthToDay)
		{
			this.graph.addEdge(null, vMonth, vDay, "DAY");
			//System.out.println("Wrote out edge (" + vMonth.getProperty("dateValue") + ")->(" + vDay.getProperty("dateValue") + ")");
		} // if
		
		return;
		//
		// TODO: Implement the "next day" structure.
		//
		/*
		// we only need to look at the "next date" pointer if we're creating a new day
		if (createdNewDay)
		{
			// check the next date that's being pointed to			

			// get reference node first (should only be one for "nextDate")
			Iterable<Vertex> nextDays = this.graph.getVertices("refNode", "nextDate");
			Iterator<Vertex> itVertex = nextDays.iterator();

			if (itVertex.hasNext())
			{
				Vertex nextDayRefNode = itVertex.next();
				Iterable<Edge> edges = nextDayRefNode.getEdges(Direction.OUT, "NEXT");
				Iterator<Edge> edge = edges.iterator();
				
				// Case 1: No days have existed in the graph up until now (should only happen for first date inserted). Need to create the relationship.
				// (i.e. the nextDate ref node has no edges)
				if (edge == null || !edge.hasNext())
				{
					// add the edge from the ref node to the current day
					graph.addEdge(null, nextDayRefNode, vDay, "NEXT");
				}				
				// Case 2: A linked list of days exists; we need to find out whether this new day needs to be inserted at the tail, the head, or 
				// somewhere else in between.
				else
				{
					
				} // if
				
				
				
				// -------- REVIEW BELOW HERE ----------
			
			int nextDayValue = day.getProperty("dateValue");
			
			Vertex month = DateGraphHelper.getMonthVertexFromDayVertex(day);
			Vertex year = DateGraphHelper.getYearVertexFromMonthVertex(month);
			
			try
			{
				GraphDate gdNext = new GraphDate(nextDayValue, (Integer)month.getProperty("dateValue"), (Integer)year.getProperty("dateValue"), this.graph);
				this.setNextDate(gdNext);
			}
			catch (IllegalDatePartValueException e)
			{
				
			} // try
			
			} // if
		} // if
		*/
	} // getDateNodeBacking
	
	public GraphDate getNextDate()
	{
		return this.nextDate;
	} // getNextDate
	
	public String getISODate() {
		throw new NotImplementedException();
	}

	private void setNextDate(GraphDate nextDate)
	{
		this.nextDate = nextDate;
	} // setNextDate

	private void valiDate(int yearValue, int monthValue, int dayValue) throws IllegalDatePartValueException
	{
		// SPECIFIC RULES FOR DATES
		
		// MONTHS
		
		// handle Feb first (leap year silliness)
		if (monthValue == 2)
		{
			// check for leap year
			boolean isLeapYear = ((yearValue & 3) == 0 && ((yearValue % 25) != 0 || (yearValue & 15) == 0));
			
			if (!isLeapYear)
			{
				if (dayValue > 28)
				{
					throw new IllegalDatePartValueException("Illegal day value \"" + dayValue + "\" for month " + monthValue + " in year " + yearValue);
				} // if
			}
			else
			{
				if (dayValue > 29)
				{
					throw new IllegalDatePartValueException("Illegal day value \"" + dayValue + "\" for month " + monthValue + " in year " + yearValue);
				} // if				
			} // if
			
			return;
		} // if
		
		final int JAN = 1;
		final int FEB = 2;
		final int MAR = 3;
		final int APR = 4;
		final int MAY = 5;
		final int JUN = 6;
		final int JUL = 7;
		final int AUG = 8;
		final int SEP = 9;
		final int OCT = 10;
		final int NOV = 11;
		final int DEC = 12;
		
		Map<Set<Integer>, Integer> rules = new HashMap<Set<Integer>, Integer>();

		// # of days in a given month
		Set<Integer> months30 = new HashSet<Integer>();
		months30.add(APR);
		months30.add(JUN);
		months30.add(SEP);
		months30.add(NOV);
		
		int numDays = 30;
		
		rules.put(months30, numDays);
		
		Set<Integer> months31 = new HashSet<Integer>();
		months31.add(JAN);
		months31.add(MAR);
		months31.add(MAY);
		months31.add(JUL);
		months31.add(AUG);
		months31.add(OCT);
		months31.add(DEC);
		
		numDays = 31;
		
		rules.put(months31, numDays);
		
		// iterate through the rules and evaluate
		Set<Entry<Set<Integer>, Integer> > rulesSet = rules.entrySet();
		
		Iterator<Entry<Set<Integer>, Integer> > it = rulesSet.iterator();

		while (it.hasNext())
		{
			Entry<Set<Integer>, Integer> ruleEntry = it.next();
			Set<Integer> rule = ruleEntry.getKey();
			int ruleNumDays = ruleEntry.getValue();
			
			if (rule.contains(monthValue) && dayValue > ruleNumDays)
			{
				throw new IllegalDatePartValueException("Illegal day value \"" + dayValue + "\" for month " + monthValue);
			} // if			
		} // while
	} // valiDate
	
	// questions
	public boolean isLaterThan(GraphDate otherDate)
	{
		throw new NotImplementedException();
	} // isLaterThan

	// persist to the DB
	public void saveNotImplemented()
	{
		// if there's a "next" day set, make sure it's set before persisting (via "day")
		if (this.nextDate != null)
		{
//			this.day.setNextDay(this.nextDate.getDay());
		} // if
	} // save
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (!(obj instanceof GraphDate))
			return false;
		
		GraphDate otherDate = (GraphDate)obj;

		return new EqualsBuilder()
			.append(this.year.getValue(), otherDate.getYear().getValue())
			.append(this.month.getValue(), otherDate.getMonth().getValue())
			.append(this.day.getValue(), otherDate.getDay().getValue())
			.isEquals();
	} // equals
	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(29, 37)
			.append(this.year.getValue())
			.append(this.month.getValue())
			.append(this.day.getValue())
			.toHashCode();
	} // hashCode
} // GraphDateImpl
