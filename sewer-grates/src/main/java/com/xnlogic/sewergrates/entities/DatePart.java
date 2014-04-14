package com.xnlogic.sewergrates.entities;

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
		if (value < 0)
			throw new IllegalDatePartValueException();
			
		this.value = value;
	} // constructor
	
	// getters
	
	public int getValue()
	{
		return value;	
	} // getValue
	
	// setters
	
	public void setValue(int value) throws IllegalDatePartValueException
	{
		if (value < 0)
			throw new IllegalDatePartValueException();
		
		this.value = value;
	}
	
	// only set a vertex if it hasn't been set already (i.e. this is coming from the outside)
	public void setVertex(Vertex v)
	{
		if (this.v == null)
			this.v = v;
	} // setVertex
	
	public void save(KeyIndexableGraph graph, String datePart)
	{
		// if the vertex doesn't exist, create it and set the properties
		if (v == null)
		{
			v = graph.addVertex(null);
		
			// set the properties
			v.setProperty(this.PROP_VALUE, this.getValue());
			v.setProperty(this.PROP_TYPE, datePart);
		} // if
	} // save

	// "default" modifier, so we can only use this method within the same package
	Vertex getBackingVertex()
	{
		return this.v;
	} // getRawVertex
} // DatePart
