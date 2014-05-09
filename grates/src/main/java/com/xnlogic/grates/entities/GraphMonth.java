package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.exceptions.MissingBackingVertexException;
import com.xnlogic.grates.util.GlobalStrings;
import com.xnlogic.grates.util.GraphDateUtil;
import com.xnlogic.grates.util.GraphUtil;

public class GraphMonth extends AbstractGraphDate {
    
    public GraphMonth(Vertex v) {
        this.backingVertex = v;
    } // constructor

    public GraphDate findDay(int dayValue) throws MissingBackingVertexException {
    	if (this.backingVertex == null) {
    	    throw new MissingBackingVertexException("Missing backing vertex for month.");
    	} // if
    	
    	Vertex vertDate = GraphUtil.getDateVertexByOutgoingEdgeValue(dayValue, GlobalStrings.getString("day.edge_label"), this.backingVertex);
    	
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
        int monthValue = this.backingVertex.getProperty(GlobalStrings.getString("month.vertex_name_property"));
        long unixDate = GraphDateUtil.getUnixTime(yearValue, monthValue, dayValue);
        Vertex day = GraphUtil.createDateSegmentInGraph(dayValue, GlobalStrings.getString("day.vertex_name_property"), GlobalStrings.getString("day.edge_label"), unixDate, this.backingVertex, graph);
        
        return day;                
    } // createDayInGraph
    
    private int getYear() {
        int yearValue = 0;
        
        Iterable<Vertex> years = super.backingVertex.getVertices(Direction.IN, GlobalStrings.getString("month.edge_label"));
        
        if (years != null && years.iterator().hasNext()) {
            Vertex year = years.iterator().next();
            
            yearValue = year.getProperty(GlobalStrings.getString("year.vertex_name_property"));
        } // if
        
        return yearValue;
    } // getYear
} // GraphMonth
