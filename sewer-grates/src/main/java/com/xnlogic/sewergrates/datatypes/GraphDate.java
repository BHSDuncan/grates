package com.xnlogic.sewergrates.datatypes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
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
			throw new NullPointerException("graph must be a valid Graph object.");
		
		this.year = new Year(yearValue);
		this.month = new Month(monthValue);
		this.day = new Day(dayValue);
		
		this.graph = graph;
		
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
		
		// get month
		
		// get day
	} // doDateLookup
	
	public BaseGraphDate getNextDate()
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
