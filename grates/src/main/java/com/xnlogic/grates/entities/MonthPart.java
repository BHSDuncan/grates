package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.exceptions.IllegalDatePartValueException;

public class MonthPart extends DatePart
{
	private final String PROP_MONTH = "Month";
	private final String PROP_VALUE = "dateValue";
	
	public MonthPart(int value) throws IllegalDatePartValueException
	{	
		super(value);
		
		if (value > 12)
			throw new IllegalDatePartValueException("Illegal month value: " + value);

	} // constructor

	public Vertex save(KeyIndexableGraph graph)
	{
		Vertex v = super.save(graph, this.PROP_MONTH);

		// can set any additional properties here...

		return v;
	} // save
} // MonthPart
