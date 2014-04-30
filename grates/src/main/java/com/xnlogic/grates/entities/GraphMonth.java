package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

public class GraphMonth extends AbstractGraphDate {
    private final String MONTH_VERT_PROP = "grates_month";
    
    private final String DAY_VERT_PROP = "grates_day";
    private final String DAY_EDGE_PROP = "value";

    private final String DAY_EDGE_LABEL = "DAY";
    
    private int monthValue = 0;
    private int yearValue = 0;
    
    public GraphMonth(Vertex v, int yearValue) {
        this.backingVertex = v;
        
        this.monthValue = v.getProperty(this.MONTH_VERT_PROP);
        this.yearValue = yearValue;
        
        this.setUnixDate(yearValue, this.monthValue, 1);        
    } // constructor

    public GraphDate findDay(int dayValue) {
    	// TODO: consider throwing exception here
    	if (this.backingVertex == null) {
    		return null;
    	} // if
    	
    	GraphDate toReturn = null;
    	
        Iterable<Edge> edges = this.backingVertex.getEdges(Direction.OUT, this.DAY_EDGE_LABEL);
        
        for (Edge e : edges)
        {
            if ((Integer)e.getProperty(this.DAY_EDGE_PROP) == dayValue)
            {
                toReturn = new GraphDate(e.getVertex(Direction.IN), this.yearValue, this.monthValue);
                break;
            } // if
        } // for
        
        return toReturn;
    } // findDay

    public GraphDate findOrCreateDay(int dayValue, KeyIndexableGraph graph) {
        GraphDate graphDate = this.findDay(dayValue);
        
    	if (graphDate != null) {
    	    return graphDate;
    	} // if
    	
        Vertex day = graph.addVertex(null);
        
        day.setProperty(this.DAY_VERT_PROP, dayValue);

        Edge e = this.backingVertex.addEdge(this.DAY_EDGE_LABEL, day);
        e.setProperty(this.DAY_EDGE_PROP, dayValue);

        graphDate = new GraphDate(day, this.yearValue, this.monthValue);
        
        return graphDate;
    } // findOrCreateDay
} // GraphMonth
