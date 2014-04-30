package com.xnlogic.grates.datatypes;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.entities.GraphDate;
import com.xnlogic.grates.entities.GraphYear;

public class Grate {
    private final String calendarName;
    private final KeyIndexableGraph graph;
        
    private final String CAL_ROOT_PROP = "grates_calendar_root";
    private final String YEAR_EDGE_LABEL = "YEAR";
    private final String YEAR_EDGE_PROP = "value";
    private final String YEAR_VERT_PROP = "grates_year";
    
    private Vertex backingVertex = null;
    
    public Grate(String calendarName, KeyIndexableGraph graph) {
        this.calendarName = calendarName;
        this.graph = graph;
    } // constructor
    
    // This should always be called after instantiating and before making any calls to a Grate.
    // This method allows Grates to see if the specified calendar currently exists and, if not, creates and initializes it
    public void init() {
        // try and find the specified calendar root
        Iterable<Vertex> calendarRoots = this.graph.getVertices(this.CAL_ROOT_PROP, this.calendarName);
        
        // if we can find the root vertex, let's remember it
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
            // if we can't find the root, create it            
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
        if (this.backingVertex == null)
            return null;
        
        GraphYear toReturn = null;
        
        // go through edges to avoid having to load all vertices
        Iterable<Edge> edges = this.backingVertex.getEdges(Direction.OUT, this.YEAR_EDGE_LABEL);
        
        for (Edge e : edges)
        {
            // only return the vertex that matches the edge
            if ((Integer)e.getProperty(this.YEAR_EDGE_PROP) == yearValue)
            {
                toReturn = new GraphYear(e.getVertex(Direction.IN));
                break;
            } // if
        } // for
        
        return toReturn;    
    } // findYear
    
    private GraphYear findOrCreateYear(int yearValue) {
        GraphYear graphYear = this.findYear(yearValue);

        // if we've found the year in question, return it!
        if (graphYear != null)
        {
            return graphYear;
        } // if
        
        // can't find the year in the graph; create it!
        Vertex year = this.graph.addVertex(null);
        
        // set the appropriate properties
        year.setProperty(this.YEAR_VERT_PROP, yearValue);
        
        // create the relationship (allows for both "all years" and "get specific year" without needing to load vertex
        Edge e = this.backingVertex.addEdge(this.YEAR_EDGE_LABEL, year);
        e.setProperty(this.YEAR_EDGE_PROP, yearValue);
        
        // create the in-memory month
        graphYear = new GraphYear(year);
        
        return graphYear;
    } // findOrCreateYear

} // Grate
