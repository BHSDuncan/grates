package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GraphDateUtil;
import com.xnlogic.grates.util.GraphUtil;

public class GraphMonth extends AbstractGraphDate {
    private final String MONTH_VERT_PROP = "grates_month";
    
    private final String DAY_VERT_PROP = "grates_day";
    private final String DATE_EDGE_PROP = "grates_edge_value";

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
    	
    	Vertex vertDate = GraphUtil.getDateVertexByOutgoingEdgeValue(dayValue, this.DAY_EDGE_LABEL, this.backingVertex);
    	
        GraphDate toReturn = (vertDate == null ? null : new GraphDate(vertDate));

        return toReturn;
    } // findDay

    public GraphDate findOrCreateDay(int dayValue, Graph graph) {
        GraphDate graphDate = this.findDay(dayValue);
        
    	if (graphDate != null) {
    	    return graphDate;
    	} // if
    	
        Vertex day = graph.addVertex(null);
        
        int yearValue = this.getYear();
        int monthValue = super.backingVertex.getProperty(this.MONTH_VERT_PROP);

        day.setProperty(this.DAY_VERT_PROP, dayValue);
        day.setProperty(this.VERT_UNIX_DATE_PROP, GraphDateUtil.getUnixTime(yearValue, monthValue, dayValue));

        Edge e = super.backingVertex.addEdge(this.DAY_EDGE_LABEL, day);
        e.setProperty(this.DATE_EDGE_PROP, dayValue);

        graphDate = new GraphDate(day);
        
        return graphDate;
    } // findOrCreateDay
    
    private int getYear() {
        int yearValue = 0;
        
        Iterable<Vertex> years = super.backingVertex.getVertices(Direction.IN, this.MONTH_EDGE_LABEL);
        
        if (years != null && years.iterator().hasNext()) {
            Vertex year = years.iterator().next();
            
            yearValue = year.getProperty(this.YEAR_VERT_PROP);
        } // if
        
        return yearValue;
    } // getYear
} // GraphMonth
