package com.xnlogic.sewergrates.datatypes;

import java.util.Iterator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.sewergrates.entities.*;
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
		
		this.year = new Year(yearValue);
		this.month = new Month(monthValue);
		this.day = new Day(dayValue);
		
		this.graph = graph;
		
		// add in relationships
		this.year.addMonth(this.month);
		this.month.addDay(this.day);
		
		// need to see if there is already a representation of this date in the graph; however note that we need ALL of the date parts above in order
		// to identify whether or not they exist (actually, we just need the year for the year, the year and the month for the month, and 
		// the year, the month and the day for the day)
		this.getDateNodeBacking();
	} // constructor

	private void getDateNodeBacking()
	{
		// TODO: Move this into JAR "onload"
		DateGraphHelper.checkAndCreateIndices(this.graph);
		
		// get year
		
		// try to lookup the exact year via the index; if no such year exists, there's no point in continuing
		Iterable<Vertex> vYears = this.graph.getVertices("dateValue", this.year.getValue());
		Vertex vYear = null;
		
		if (vYears != null)
		{
			Iterator<Vertex> iterYears = vYears.iterator();
			
			if (iterYears.hasNext())
			{
				vYear = (Vertex)iterYears.next();
			
				// make sure we only have one match for the year
				assert(!iterYears.hasNext());
			
				// set the backing vertex
				this.year.setVertex(vYear);
			
				// load any associated months
				this.year.loadMonths();
			}
			else
			{
				// no year found; get outta here
				return;
			} // if
		}
		else
		{
			// don't bother continuing since not even the year matches
			return;
		} // if

		// get month
		Iterable<Vertex> vMonths = vYear.getVertices(Direction.OUT, "MONTH");
		Vertex vMonth = null;
		
		if (vMonths != null)
		{
			Iterator<Vertex> iterMonths = vMonths.iterator();
			
			while (iterMonths.hasNext())
			{
				Vertex v = (Vertex)iterMonths.next();
				
				if ((Integer)v.getProperty("dateValue") == this.month.getValue())
				{
					vMonth = v;
					this.month.setVertex(vMonth);
					
					// load any associated days
					this.month.loadDays();
					
					break;
				} // if
			} // while
			
			if (iterMonths.hasNext())
			{
				return;
			} // if
		} // if
		
		// get day
		Iterable<Vertex> vDays = vMonth.getVertices(Direction.OUT, "DAY");
		Vertex vDay = null;
		
		if (vDays != null)
		{
			Iterator<Vertex> iterDays = vDays.iterator();
			
			while (iterDays.hasNext())
			{
				Vertex v = (Vertex)iterDays.next();
				
				if ((Integer)v.getProperty("dateValue") == this.day.getValue())
				{
					vDay = v;
					this.day.setVertex(vDay);
					break;
				} // if
			} // while
			
			if (iterDays.hasNext())
			{
				return;
			} // if
		} // if
	} // doDateLookup
	
	public GraphDate getNextDate()
	{
		return this.nextDate;
	} // getNextDate
	
	public String getISODate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setNextDate(GraphDate nextDate)
	{
		this.nextDate = nextDate;
	} // setNextDate

	// questions
	public boolean isLaterThan(GraphDate otherDate)
	{
		
		return true;
	} // isLaterThan

	// persist to the DB
	public void save()
	{
		// if there's a "next" day set, make sure it's set before persisting (via "day")
		if (this.nextDate != null)
		{
			this.day.setNextDay(this.nextDate.getDay());
		} // if

		this.year.save(this.graph);
		this.month.save(this.graph);
		this.day.save(this.graph);		
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
