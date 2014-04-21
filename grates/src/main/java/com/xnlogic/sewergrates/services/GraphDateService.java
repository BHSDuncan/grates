package com.xnlogic.sewergrates.services;

import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.sewergrates.datatypes.GraphDate;

public interface GraphDateService<T extends Vertex>
{
	public Iterable<T> getAllEventsForDate(GraphDate graphDate);
	
	public Iterable<T> getAllEventsInDateRange(GraphDate dateStart, GraphDate dateEnd);
	
	// inclusive
	public boolean doesEventHappenWithinDateRange(T event, GraphDate dateStart, GraphDate dateEnd);

} // GraphDateService
