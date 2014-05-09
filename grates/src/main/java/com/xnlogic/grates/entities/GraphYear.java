package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.exceptions.MissingBackingVertexException;
import com.xnlogic.grates.util.GlobalStrings;
import com.xnlogic.grates.util.GraphDateUtil;
import com.xnlogic.grates.util.GraphUtil;

public class GraphYear extends AbstractGraphDate {
    public GraphYear(Vertex v) {
        this.backingVertex = v;                        
    } // constructor
    
    public GraphMonth findMonth(int monthValue) throws MissingBackingVertexException {
    	if (this.backingVertex == null) {
    		throw new MissingBackingVertexException("Missing backing vertex for year.");
    	} // if

    	Vertex vertMonth = GraphUtil.getDateVertexByOutgoingEdgeValue(monthValue, GlobalStrings.getString("month.edge_label"), this.backingVertex);
    	
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
        int yearValue = this.backingVertex.getProperty(GlobalStrings.getString("year.vertex_name_property"));
        long unixDate = GraphDateUtil.getUnixTime(yearValue, monthValue, 1);
        Vertex month = GraphUtil.createDateSegmentInGraph(monthValue, GlobalStrings.getString("month.vertex_name_property"), GlobalStrings.getString("month.edge_label"), unixDate, this.backingVertex, graph);
        
        return month;        
    } // createMonthInGraph
} // GraphYear
