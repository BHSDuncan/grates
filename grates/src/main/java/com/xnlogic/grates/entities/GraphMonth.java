package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.exceptions.MissingBackingVertexException;
import com.xnlogic.grates.util.GraphDateUtil;
import com.xnlogic.grates.util.GraphUtil;

public class GraphMonth extends AbstractGraphDate {
    private final String MONTH_VERT_PROP = "grates_month";
    
    private final String DAY_VERT_PROP = "grates_day";

    private final String YEAR_VERT_PROP = "grates_year";

    private final String DAY_EDGE_LABEL = "DAY";
    private final String MONTH_EDGE_LABEL = "MONTH";
    
    public GraphMonth(Vertex v) {
        this.backingVertex = v;
    } // constructor

    public GraphDate findDay(int dayValue) throws MissingBackingVertexException {
    	if (this.backingVertex == null) {
    	    throw new MissingBackingVertexException("Missing backing vertex for month.");
    	} // if
    	
    	Vertex vertDate = GraphUtil.getDateVertexByOutgoingEdgeValue(dayValue, this.DAY_EDGE_LABEL, this.backingVertex);
    	
        GraphDate toReturn = (vertDate == null ? null : new GraphDate(vertDate));

        return toReturn;
    } // findDay

    public GraphDate findOrCreateDay(int dayValue, Graph graph) throws MissingBackingVertexException {
        GraphDate graphDate = this.findDay(dayValue);
        
    	if (graphDate != null) {
    	    return graphDate;
    	} // if
    	
        graphDate = new GraphDate(this.createDayInGraph(dayValue, graph));
        
        return graphDate;
    } // findOrCreateDay
    
    private Vertex createDayInGraph(int dayValue, Graph graph) {
        int yearValue = this.getYear();
        int monthValue = this.backingVertex.getProperty(this.MONTH_VERT_PROP);
        long unixDate = GraphDateUtil.getUnixTime(yearValue, monthValue, dayValue);
        Vertex day = GraphUtil.createDateSegmentInGraph(dayValue, this.DAY_VERT_PROP, this.DAY_EDGE_LABEL, unixDate, this.backingVertex, graph);
        
        return day;                
    } // createDayInGraph
    
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
