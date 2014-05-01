package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GraphDateUtil;

public class GraphMonth extends AbstractGraphDate {
    private final String MONTH_VERT_PROP = "grates_month";
    
    private final String DAY_VERT_PROP = "grates_day";
    private final String DAY_EDGE_PROP = "value";

    private final String YEAR_VERT_PROP = "grates_year";

    private final String DAY_EDGE_LABEL = "DAY";
    private final String MONTH_EDGE_LABEL = "MONTH";
    
    public GraphMonth(Vertex v) {
        this.backingVertex = v;
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
                toReturn = new GraphDate(e.getVertex(Direction.IN));
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
        
        int yearValue = this.getYear();
        int monthValue = this.backingVertex.getProperty(this.MONTH_VERT_PROP);

        day.setProperty(this.DAY_VERT_PROP, dayValue);
        day.setProperty(this.VERT_UNIX_DATE_PROP, GraphDateUtil.getUnixTime(yearValue, monthValue, dayValue));

        Edge e = this.backingVertex.addEdge(this.DAY_EDGE_LABEL, day);
        e.setProperty(this.DAY_EDGE_PROP, dayValue);

        graphDate = new GraphDate(day);
        
        return graphDate;
    } // findOrCreateDay
    
    private int getYear() {
        int yearValue = 0;
        
        Iterable<Vertex> years = this.backingVertex.getVertices(Direction.IN, this.MONTH_EDGE_LABEL);
        
        if (years != null && years.iterator().hasNext()) {
            Vertex year = years.iterator().next();
            
            yearValue = year.getProperty(this.YEAR_VERT_PROP);
        } // if
        
        return yearValue;
    } // getYear
} // GraphMonth
