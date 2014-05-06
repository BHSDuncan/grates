package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.exceptions.MissingBackingVertexException;
import com.xnlogic.grates.util.GraphDateUtil;
import com.xnlogic.grates.util.GraphUtil;

public class GraphYear extends AbstractGraphDate {
    private final String YEAR_VERT_PROP = "grates_year";
    
    private final String MONTH_VERT_PROP = "grates_month";
    
    private final String MONTH_EDGE_LABEL = "MONTH";

    public GraphYear(Vertex v) {
        this.backingVertex = v;                        
    } // constructor
    
    public GraphMonth findMonth(int monthValue) throws MissingBackingVertexException {
    	if (this.backingVertex == null) {
    		throw new MissingBackingVertexException("Missing backing vertex for year.");
    	} // if

    	Vertex vertMonth = GraphUtil.getDateVertexByOutgoingEdgeValue(monthValue, this.MONTH_EDGE_LABEL, this.backingVertex);
    	
    	GraphMonth toReturn = (vertMonth == null ? null : new GraphMonth(vertMonth));
        
        return toReturn;
    } // findMonth
    
    public GraphMonth findOrCreateMonth(int monthValue, Graph graph) throws MissingBackingVertexException {
    	GraphMonth graphMonth = this.findMonth(monthValue);

    	if (graphMonth != null) {
    	    return graphMonth;
    	} // if
    	
        graphMonth = new GraphMonth(this.createMonthInGraph(monthValue, graph));
        
        return graphMonth;
    } // findOrCreateMonth
    
    private Vertex createMonthInGraph(int monthValue, Graph graph) {
        int yearValue = this.backingVertex.getProperty(this.YEAR_VERT_PROP);
        long unixDate = GraphDateUtil.getUnixTime(yearValue, monthValue, 1);
        Vertex month = GraphUtil.createDateSegmentInGraph(monthValue, this.MONTH_VERT_PROP, this.MONTH_EDGE_LABEL, unixDate, this.backingVertex, graph);
        
        return month;        
    } // createMonthInGraph
} // GraphYear
