package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

public class GraphMonth extends AbstractGraphDate 
{
    private final String DAY_VERT_PROP = "grates_day";
    private final String DAY_EDGE_PROP = "value";

    private final String DAY_EDGE_LABEL = "DAY";
    
    public GraphMonth(Vertex v)
    {
        this.backingVertex = v;
    } // constructor

    public GraphDate findDay(int dayValue)
    {
    	// TODO: consider throwing exception here
    	if (this.backingVertex == null)
    		return null;
    	
    	GraphDate toReturn = null;
    	
    	// go through edges to avoid having to load all vertices
        Iterable<Edge> edges = this.backingVertex.getEdges(Direction.OUT, this.DAY_EDGE_LABEL);
        
        for (Edge e : edges)
        {
        	// only return the vertex that matches the edge
            if ((Integer)e.getProperty(this.DAY_EDGE_PROP) == dayValue)
            {
                toReturn = new GraphDate(e.getVertex(Direction.IN));
                break;
            } // if
        } // for
        
        return toReturn;
    } // findDay

    public GraphDate findOrCreateDay(int dayValue, KeyIndexableGraph graph)
    {
        GraphDate graphDate = this.findDay(dayValue);
        
        // if we've found the day in question, return it!
    	if (graphDate != null)
    	{
    	    return graphDate;
    	} // if
    	
        // can't find the day in the graph; create it!
        Vertex day = graph.addVertex(null);
        
        // set the appropriate properties
        day.setProperty(this.DAY_VERT_PROP, dayValue);
        
        // create the in-memory month
        graphDate = new GraphDate(day);
        
        return graphDate;
    } // findOrCreateDay

    @Override
	long getUnixDate() 
    {
        return 0;
    } // getUnixDate

} // GraphMonth
