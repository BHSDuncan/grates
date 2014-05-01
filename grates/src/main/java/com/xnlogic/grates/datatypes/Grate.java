package com.xnlogic.grates.datatypes;

import java.util.HashSet;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.entities.GraphDate;
import com.xnlogic.grates.entities.GraphYear;
import com.xnlogic.grates.util.GraphDateUtil;

public class Grate {
    private final String calendarName;
    private final KeyIndexableGraph graph;

    // TODO: Move this into properties file.  Maybe iterate through all keys for indices via properties file
    private final String CAL_ROOT_PROP = "grates_calendar_name";

    private final String YEAR_EDGE_LABEL = "YEAR";
    private final String YEAR_EDGE_PROP = "value";
    private final String YEAR_VERT_PROP = "grates_year";
    private final String VERT_UNIX_DATE_PROP = "grates_unix_date";
    
    private Vertex backingVertex = null;
    
    public Grate(String calendarName, KeyIndexableGraph graph) {
        this.calendarName = calendarName;
        this.graph = graph;
    } // constructor
    
    // This should always be called after instantiating and before making any calls to a Grate.
    // This method allows Grates to see if the specified calendar currently exists and, if not, creates and initializes it
    public void init() {
        this.checkOrCreateIndices();
        
        Iterable<Vertex> calendarRoots = this.graph.getVertices(this.CAL_ROOT_PROP, this.calendarName);
        
        if (calendarRoots != null && calendarRoots.iterator().hasNext())
        {
            for (Vertex calendarRoot : calendarRoots)
            {
                if (calendarRoot.getProperty(this.CAL_ROOT_PROP) == this.calendarName)
                {
                    this.backingVertex = calendarRoot;
                    break;
                } // if
            } // for
        }
        else
        {
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
        
        GraphYear toReturn = null;
        
        Iterable<Edge> edges = this.backingVertex.getEdges(Direction.OUT, this.YEAR_EDGE_LABEL);
        
        for (Edge e : edges) {
            if ((Integer)e.getProperty(this.YEAR_EDGE_PROP) == yearValue) {
                toReturn = new GraphYear(e.getVertex(Direction.IN));
                break;
            } // if
        } // for
        
        return toReturn;    
    } // findYear
    
    private GraphYear findOrCreateYear(int yearValue) {
        GraphYear graphYear = this.findYear(yearValue);

        if (graphYear != null) {
            return graphYear;
        } // if
        
        Vertex year = this.graph.addVertex(null);
        
        year.setProperty(this.YEAR_VERT_PROP, yearValue);
        year.setProperty(this.VERT_UNIX_DATE_PROP, GraphDateUtil.getUnixTime(yearValue, 1, 1));
        
        Edge e = this.backingVertex.addEdge(this.YEAR_EDGE_LABEL, year);
        e.setProperty(this.YEAR_EDGE_PROP, yearValue);
        
        graphYear = new GraphYear(year);
        
        return graphYear;
    } // findOrCreateYear

    private void checkOrCreateIndices() {
        Set<String> keyNames = new HashSet<String>();
        keyNames.add(this.CAL_ROOT_PROP);

        Set<String> keys = this.graph.getIndexedKeys(Vertex.class);

        for (String key : keyNames) {
            if (!keys.contains(key)) {
                this.graph.createKeyIndex(key, Vertex.class);
            } // if
        } // while
    } // checkOrCreateIndices
} // Grate
