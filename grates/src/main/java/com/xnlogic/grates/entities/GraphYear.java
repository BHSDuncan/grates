package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

public class GraphYear extends AbstractGraphDate 
{
	private final String MONTH_VERT_PROP = "grates_month";
    private final String MONTH_EDGE_PROP = "value";

    private final String MONTH_EDGE_LABEL = "MONTH";
    
	public GraphYear(Vertex v)
	{
        this.backingVertex = v;
	} // constructor
    
    private GraphMonth findMonth(int monthValue)
    {
    	// TODO: consider throwing exception here
    	if (this.backingVertex == null)
    		return null;
    	
    	GraphMonth toReturn = null;
    	
    	// go through edges to avoid having to load all vertices
        Iterable<Edge> edges = this.backingVertex.getEdges(Direction.OUT, this.MONTH_EDGE_LABEL);
        
        for (Edge e : edges)
        {
        	// only return the vertex that matches the edge
            if ((Integer)e.getProperty(this.MONTH_EDGE_PROP) == monthValue)
            {
                toReturn = new GraphMonth(e.getVertex(Direction.IN));
                break;
            } // if
        } // for
        
        return toReturn;
    } // findMonth
    
    public GraphMonth findOrCreateMonth(int monthValue, KeyIndexableGraph graph)
    {
    	GraphMonth graphMonth = this.findMonth(monthValue);

    	// if we've found the month in question, return it!
    	if (graphMonth != null)
    	{
    	    return graphMonth;
    	} // if
    	
        // can't find the month in the graph; create it!
        Vertex month = graph.addVertex(null);
        
        // set the appropriate properties
        month.setProperty(this.MONTH_VERT_PROP, monthValue);
        
        // create the relationship (allows for both "all months" and "get specific month" without needing to load vertex
        Edge e = this.backingVertex.addEdge(this.MONTH_EDGE_LABEL, month);
        e.setProperty(this.MONTH_EDGE_PROP, monthValue);
        
        // create the in-memory month
        graphMonth = new GraphMonth(month);
        
        return graphMonth;
    } // findOrCreateMonth

    @Override
	long getUnixDate() 
    {
        return 0;
    } // getUnixDate
} // GraphYear
