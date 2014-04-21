package com.xnlogic.grates.datatypes;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.xnlogic.grates.entities.*;

// year parts of year, month, and day should be final, so no need for setters
public abstract class BaseGraphDate 
{	
	// underlying representation; clients shouldn't know/care about these
	protected YearPart year;
	protected MonthPart month;
	protected DayPart day;

	// getters	
	public YearPart getYear()
	{
		return this.year;
	} // getYear
	
	public MonthPart getMonth()
	{
		return this.month;
	} // getMonth
	
	public DayPart getDay()
	{
		return this.day;
	} // getDay
	
	//public abstract String getFormattedDate(String format);
	public abstract String getISODate();
			
	// persistence
	protected void save(KeyIndexableGraph graph) {};
} // GraphDate
