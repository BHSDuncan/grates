package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GraphDateUtil;

public class GraphYear extends AbstractGraphDate {
    private final String YEAR_VERT_PROP = "grates_year";
    
	private final String MONTH_VERT_PROP = "grates_month";
    
    private final String MONTH_EDGE_LABEL = "MONTH";
        
	public GraphYear(Vertex v) {
        super.backingVertex = v;                        
	} // constructor
    
    public GraphMonth findMonth(int monthValue) {
    	// TODO: consider throwing exception here
    	if (super.backingVertex == null) {
    		return null;
    	} // if

    	Vertex vertMonth = this.getDateVertexByOutgoingEdgeValue(monthValue, this.MONTH_EDGE_LABEL);
    	
    	GraphMonth toReturn = (vertMonth == null ? null : new GraphMonth(vertMonth));
        
        return toReturn;
    } // findMonth
    
    public GraphMonth findOrCreateMonth(int monthValue, KeyIndexableGraph graph) {
    	GraphMonth graphMonth = this.findMonth(monthValue);

    	if (graphMonth != null) {
    	    return graphMonth;
    	} // if
    	
        Vertex month = graph.addVertex(null);

        int yearValue = super.backingVertex.getProperty(this.YEAR_VERT_PROP);

        month.setProperty(this.MONTH_VERT_PROP, monthValue);
        month.setProperty(this.VERT_UNIX_DATE_PROP, GraphDateUtil.getUnixTime(yearValue, monthValue, 1));
        
        Edge e = super.backingVertex.addEdge(this.MONTH_EDGE_LABEL, month);
        e.setProperty(this.DATE_EDGE_PROP, monthValue);

        graphMonth = new GraphMonth(month);
        
        return graphMonth;
    } // findOrCreateMonth
} // GraphYear
