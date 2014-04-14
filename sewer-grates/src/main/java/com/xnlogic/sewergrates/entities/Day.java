package com.xnlogic.sewergrates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;

public class Day extends DatePart
{
	private final String PROP_DAY = "Day";
	private final String EDGE_NEXT_DAY = "NEXT";
	
	private Day nextDay = null;
	
	public Day(int value) throws IllegalDatePartValueException
	{	
		super(value);
	} // constructor
	
	// should have only one "next" day
	public void setNextDay(Day day)
	{
		this.nextDay = day;
	} // setNextDay
	
	public void save(KeyIndexableGraph graph)
	{
		super.save(graph, this.PROP_DAY);

		// can set any additional properties, etc. here...

		// save the next day, just in case
		this.nextDay.save(graph);
		
		// update the "next" edge
		this.updateNextDayEdge(graph);
	} // save
	
	private void updateNextDayEdge(KeyIndexableGraph graph)
	{
		// save the relationship with the next day, provided it doesn't yet exist
		Iterable<Edge> edges = this.v.getEdges(Direction.OUT, this.EDGE_NEXT_DAY);
		
		// if we don't have the edge, add it
		if (edges == null || !edges.iterator().hasNext())
		{
			this.v.addEdge(this.EDGE_NEXT_DAY, this.nextDay.getBackingVertex());
		}
		else
		{
			// if there exists a "next" edge already, update it if it's new
			assert(edges.iterator().hasNext());
			
			// get the edge
			Edge e = (Edge)(edges.iterator().next());

			// make sure we don't have any other ones before we update (i.e. remove/add) the edge
			assert(!edges.iterator().hasNext());

			// only remove/add if the edge isn't already in there as-is
			if (e.getVertex(Direction.IN) != this.nextDay.getBackingVertex() && e.getVertex(Direction.OUT) != this.v)
			{
				graph.removeEdge(e);
				graph.addEdge(null, this.v, this.nextDay.getBackingVertex(), this.EDGE_NEXT_DAY);
			} // if
			
		} // if
	} // updateNextDayEdge

} // Day
