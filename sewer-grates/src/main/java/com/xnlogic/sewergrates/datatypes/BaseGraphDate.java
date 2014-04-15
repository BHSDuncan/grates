package com.xnlogic.sewergrates.datatypes;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.xnlogic.sewergrates.entities.*;

// year parts of year, month, and day should be final, so no need for setters
public abstract class BaseGraphDate 
{	
	// underlying representation; clients shouldn't know/care about these
	protected Year year;
	protected Month month;
	protected Day day;

	// getters	
	public Year getYear()
	{
		return this.year;
	} // getYear
	
	public Month getMonth()
	{
		return this.month;
	} // getMonth
	
	public Day getDay()
	{
		return this.day;
	} // getDay
	
	//public abstract String getFormattedDate(String format);
	public abstract String getISODate();
			
	// persistence
	protected void save(KeyIndexableGraph graph) {};
} // GraphDate
