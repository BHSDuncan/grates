package com.xnlogic.sewergrates.entities;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Query;
import com.xnlogic.sewergrates.exceptions.DayCouldNotBeCreatedFromVertexException;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;
import com.xnlogic.sewergrates.helpers.DateGraphHelper;

public class Month extends DatePart
{
	private final String PROP_MONTH = "Month";
	
	private Map<Day, Edge> days = new HashMap<Day, Edge>();

	public Month(int value) throws IllegalDatePartValueException
	{	
		super(value);
	} // constructor

	public void save(KeyIndexableGraph graph)
	{
		super.save(graph, this.PROP_MONTH);

		// can set any additional properties here...

		// add in any edges that are needed to relate to any days; once this is done, it never needs to be done again
		for (Map.Entry<Day, Edge> entry : this.days.entrySet())
		{
			if (entry.getValue() == null)
			{
				Day d = entry.getKey();
				
				// if the day node has not yet been saved, we need to do so first
				if (d.getBackingVertex() == null)
				{
					d.save(graph);
				} // if
				
				// add in the edge
				Edge e = graph.addEdge(null, this.getBackingVertex(), d.getBackingVertex(), "DAY");
				this.days.put(entry.getKey(), e);
			} // if
		} // for
	} // save

	public void addDay(Day day)
	{
		this.days.put(day, null);
	} // addDay
	
	public void loadDays()
	{
		// only load days if we have a vertex to work with
		if (this.v == null)
			return;

		// clear the days
		this.days.clear();
		
		Query q = this.v.query();
		Iterable<Edge> edges = q.edges();
		
		for (Edge e : edges)
		{
			if (e.getLabel() == "DAY")
			{
				try
				{
					Day d = DateGraphHelper.getDayFromVertex(e.getVertex(Direction.IN));
					this.days.put(d, e);
				}
				catch (DayCouldNotBeCreatedFromVertexException ex)
				{
					
				} // try				
			} // if
		} // for
	} // loadDays	
} // Month
