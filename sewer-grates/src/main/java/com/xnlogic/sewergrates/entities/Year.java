package com.xnlogic.sewergrates.entities;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Query;
import com.xnlogic.sewergrates.exceptions.IllegalDatePartValueException;
import com.xnlogic.sewergrates.exceptions.MonthCouldNotBeCreatedFromVertexException;
import com.xnlogic.sewergrates.helpers.DateGraphHelper;

public class Year extends DatePart
{
	private final String PROP_YEAR = "Year";
	
	private Map<Month, Edge> months = new HashMap<Month, Edge>();

	public Year(int value) throws IllegalDatePartValueException
	{	
		super(value);
	} // constructor

	public void save(KeyIndexableGraph graph)
	{
		super.save(graph, this.PROP_YEAR);

		// can set any additional properties here...
		
		// add in any edges that are needed to relate to any months; once this is done, it never needs to be done again
		for (Map.Entry<Month, Edge> entry : this.months.entrySet())
		{
			if (entry.getValue() == null)
			{
				Month m = entry.getKey();
				
				// if the month node has not yet been saved, we need to do so first
				if (m.getBackingVertex() == null)
				{
					m.save(graph);
				} // if

				// add in the edge
				Edge e = graph.addEdge(null, this.getBackingVertex(), m.getBackingVertex(), "MONTH");
				this.months.put(entry.getKey(), e);
			} // if
		} // for
	} // save
	
	public void addMonth(Month month)
	{
		this.months.put(month, null);
	} // addMonth
	
	public void loadMonths()
	{
		// only load months if we have a vertex to work with
		if (this.v == null)
			return;
		
		// clear the months
		this.months.clear();
		
		Query q = this.v.query();
		Iterable<Edge> edges = q.edges();
		
		for (Edge e : edges)
		{
			if (e.getLabel() == "MONTH")
			{
				try
				{
					Month m = DateGraphHelper.getMonthFromVertex(e.getVertex(Direction.IN));
					this.months.put(m, e);
				}
				catch (MonthCouldNotBeCreatedFromVertexException ex)
				{
					
				} // try				
			} // if
		} // for
	} // loadMonths
} // Year
