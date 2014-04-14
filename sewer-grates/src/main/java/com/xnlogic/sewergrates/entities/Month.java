package com.xnlogic.sewergrates.entities;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;

public class Month extends DatePart
{
	private final String PROP_MONTH = "Month";
	
	public Month(int value) throws IllegalDatePartValueException
	{	
		super(value);
	} // constructor

	public void save(KeyIndexableGraph graph)
	{
		super.save(graph, this.PROP_MONTH);

		// can set any additional properties here...
	} // save

} // Month
