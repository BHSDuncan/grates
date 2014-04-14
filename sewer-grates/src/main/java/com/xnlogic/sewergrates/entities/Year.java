package com.xnlogic.sewergrates.entities;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;

public class Year extends DatePart
{
	private final String PROP_YEAR = "Year";
	
	public Year(int value) throws IllegalDatePartValueException
	{	
		super(value);
	} // constructor

	public void save(KeyIndexableGraph graph)
	{
		super.save(graph, this.PROP_YEAR);

		// can set any additional properties here...
	} // save
} // Year
