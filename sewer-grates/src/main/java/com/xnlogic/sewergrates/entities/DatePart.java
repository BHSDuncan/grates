package com.xnlogic.sewergrates.entities;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;

// Represents a date part in the graph.
public abstract class DatePart 
{
	private int value = 0;
	protected Vertex v = null;
		
	// TODO: Move these into .properties file.
	private final String PROP_VALUE = "dateValue";
	private final String PROP_TYPE = "dateType";
	
	public DatePart(int value) throws IllegalDatePartValueException
	{
		if (value < 1)
			throw new IllegalDatePartValueException("All date parts must be a positive integer.");
			
		this.value = value;
	} // constructor
	
	// getters
	
	public int getValue()
	{
		return value;	
	} // getValue
		
	protected Vertex save(KeyIndexableGraph graph, String datePart)
	{
		// if the vertex doesn't exist, create it and set the properties
		// TODO: Put in check to ensure the node doesn't actually exist in the graph, even though this shouldn't be called unless such a check
		// has been done before.  GraphDate currently does this (GraphDate->Year->DatePart, for example), but consider defence in depth.
		if (this.v == null)
		{
			this.v = graph.addVertex(null);
		
			// set the properties
			this.v.setProperty(this.PROP_VALUE, this.getValue());
			this.v.setProperty(this.PROP_TYPE, datePart);
		} // if
		
		return this.v;
	} // save

	// "default" modifier, so we can only use this method within the same package
	Vertex getBackingVertex()
	{
		return this.v;
	} // getBackingVertex
} // DatePart
