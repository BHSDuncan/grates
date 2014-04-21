package com.xnlogic.sewergrates.entities;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;

public class YearPart extends DatePart
{
	private final String PROP_YEAR = "Year";
	private final String PROP_VALUE = "dateValue";
	
	public YearPart(int value) throws IllegalDatePartValueException
	{
		super(value);
	} // constructor

	// PRE: This node has never been saved before.
	public Vertex save(KeyIndexableGraph graph)
	{
		Vertex v = super.save(graph, this.PROP_YEAR);

		// can set any additional properties here...
		return v;
	} // save	
} // YearPart
