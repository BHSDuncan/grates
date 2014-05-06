package com.xnlogic.grates.datatypes;

import java.util.HashSet;
import java.util.Set;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.entities.GraphDate;
import com.xnlogic.grates.entities.GraphYear;
import com.xnlogic.grates.util.GraphDateUtil;
import com.xnlogic.grates.util.GraphUtil;

public class Grate {
    private final String calendarName;
    private final Graph graph;

    // TODO: Move this into properties file.  Maybe iterate through all keys for indices via properties file
    private final String CAL_ROOT_PROP = "grates_calendar_name";


    private final String YEAR_EDGE_LABEL = "YEAR";
    private final String YEAR_VERT_PROP = "grates_year";
    
    private Vertex backingVertex = null;
    
    public Grate(String calendarName, Graph graph) {
        this.calendarName = calendarName;
        this.graph = graph;
    } // constructor
    
    // This should always be called after instantiating and before making any calls to a Grate.
    // This method allows Grates to see if the specified calendar currently exists and, if not, creates and initializes it
    public void init() {
        this.checkOrCreateIndices();
        
        Iterable<Vertex> calendarRoots = this.graph.getVertices(this.CAL_ROOT_PROP, this.calendarName);

        this.backingVertex = GraphUtil.findVertexFromVertices(this.CAL_ROOT_PROP, this.calendarName, calendarRoots);
        
        if (this.backingVertex == null) {
            Vertex newCalendarRoot = this.graph.addVertex(null);
            newCalendarRoot.setProperty(this.CAL_ROOT_PROP, this.calendarName);
            
            this.backingVertex = newCalendarRoot;
        } // if
    } // init
    
    public GraphDate findDate(int yearValue, int monthValue, int dayValue) {
        return this.findYear(yearValue)
                   .findMonth(monthValue)
                   .findDay(dayValue);
                   
    } // findDate

    // PRE: A transaction must already be started.    
    public GraphDate findOrCreateDate(int yearValue, int monthValue, int dayValue) {
        return this.findOrCreateYear(yearValue)
                   .findOrCreateMonth(monthValue, this.graph)
                   .findOrCreateDay(dayValue, this.graph);
    } // findOrCreateDate

    private GraphYear findYear(int yearValue) {
        // TODO: consider throwing exception here
        if (this.backingVertex == null) {
            return null;
        } // if

        Vertex yearVertex = GraphUtil.getDateVertexByOutgoingEdgeValue(yearValue, this.YEAR_EDGE_LABEL, this.backingVertex);
        
        GraphYear toReturn = (yearVertex == null ? null : new GraphYear(yearVertex));

        return toReturn;    
    } // findYear
    
    private GraphYear findOrCreateYear(int yearValue) {
        GraphYear graphYear = this.findYear(yearValue);

        if (graphYear != null) {
            return graphYear;
        } // if
        
        graphYear = new GraphYear(this.createYearInGraph(yearValue));
        
        return graphYear;
    } // findOrCreateYear

    private Vertex createYearInGraph(int yearValue) {
        long unixDate = GraphDateUtil.getUnixTime(yearValue, 1, 1);
        Vertex year = GraphUtil.createDateSegmentInGraph(yearValue, this.YEAR_VERT_PROP, this.YEAR_EDGE_LABEL, unixDate, this.backingVertex, this.graph);
        
        return year;
    } // createYearInGraph
    
    private void checkOrCreateIndices() {
        if (this.graph instanceof KeyIndexableGraph) {
            Set<String> keyNames = new HashSet<String>();
            keyNames.add(this.CAL_ROOT_PROP);

            Set<String> keys = ((KeyIndexableGraph)this.graph).getIndexedKeys(Vertex.class);

            for (String key : keyNames) {
                if (!keys.contains(key)) {
                    ((KeyIndexableGraph)this.graph).createKeyIndex(key, Vertex.class);
                } // if
            } // while
        } // if
    } // checkOrCreateIndices    
} // Grate
