package com.xnlogic.grates;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.datatypes.Grate;

public class GraphHelperFunctions {
    private static final String CAL_ROOT_PROP = "grates_calendar_name";
       
    public static Vertex getCalendarRoot(String calendarName, KeyIndexableGraph graph) {
        Vertex root = null;
        
        Iterable<Vertex> calendarRoots = graph.getVertices(CAL_ROOT_PROP, calendarName);
        
        if (calendarRoots != null && calendarRoots.iterator().hasNext())
        {
            for (Vertex calendarRoot : calendarRoots)
            {
                if (calendarRoot.getProperty(CAL_ROOT_PROP) == calendarName)
                {
                    root = calendarRoot;
                    break;
                } // if
            } // for
        }
        
        return root;
    }
    
    public static Vertex getYearFromGraph(int yearValue, Vertex calendarRoot) {
        if (calendarRoot == null)
            return null;
        
        Vertex toReturn = null;
        
        // go through edges to avoid having to load all vertices
        Iterable<Edge> edges = calendarRoot.getEdges(Direction.OUT, "YEAR");
        
        for (Edge e : edges)
        {
            // only return the vertex that matches the edge
            if ((Integer)e.getProperty("value") == yearValue)
            {
                toReturn = e.getVertex(Direction.IN);
                break;
            } // if
        } // for
        
        return toReturn;            
    }

    public static List<Vertex> getYearsFromGraph(int yearValue, Vertex calendarRoot) {
        if (calendarRoot == null)
            return null;
        
        List<Vertex> toReturn = new ArrayList<>();
        
        // go through edges to avoid having to load all vertices
        Iterable<Edge> edges = calendarRoot.getEdges(Direction.OUT, "YEAR");
        
        for (Edge e : edges)
        {
            // only return the vertex that matches the edge
            if ((Integer)e.getProperty("value") == yearValue)
            {
                toReturn.add(e.getVertex(Direction.IN));
            } // if
        } // for
        
        return toReturn;            
    }

    public static Vertex getMonthFromGraph(int monthValue, Vertex year) {
        if (year == null)
            return null;
        
        Vertex toReturn = null;
        
        // go through edges to avoid having to load all vertices
        Iterable<Edge> edges = year.getEdges(Direction.OUT, "MONTH");
        
        for (Edge e : edges)
        {
            // only return the vertex that matches the edge
            if ((Integer)e.getProperty("value") == monthValue)
            {
                toReturn = e.getVertex(Direction.IN);
                break;
            } // if
        } // for
        
        return toReturn;            
    }
    
    public static List<Vertex> getMonthsFromGraph(int monthValue, Vertex year) {
        if (year == null)
            return null;
        
        List<Vertex> toReturn = new ArrayList<>();
        
        // go through edges to avoid having to load all vertices
        Iterable<Edge> edges = year.getEdges(Direction.OUT, "MONTH");
        
        for (Edge e : edges)
        {
            // only return the vertex that matches the edge
            if ((Integer)e.getProperty("value") == monthValue)
            {
                toReturn.add(e.getVertex(Direction.IN));
            } // if
        } // for
        
        return toReturn;            
    }
    
    public static Vertex getDayFromGraph(int dayValue, Vertex month) {
        if (month == null)
            return null;
        
        Vertex toReturn = null;
        
        Iterable<Edge> edges = month.getEdges(Direction.OUT, "DAY");
        
        for (Edge e : edges)
        {
            if ((Integer)e.getProperty("value") == dayValue)
            {
                toReturn = e.getVertex(Direction.IN);
                break;
            } // if
        } // for
        
        return toReturn;            
    }
    
    public static List<Vertex> getDaysFromGraph(int dayValue, Vertex month) {
        if (month == null)
            return null;
        
        List<Vertex> toReturn = new ArrayList<>();
        
        Iterable<Edge> edges = month.getEdges(Direction.OUT, "DAY");
        
        for (Edge e : edges)
        {
            if ((Integer)e.getProperty("value") == dayValue)
            {
                toReturn.add(e.getVertex(Direction.IN));
            } // if
        } // for
        
        return toReturn;            
    }
        
    public static int getCalendarCount(String calendarName, KeyIndexableGraph graph) {
        int numCalendars = 0;
        
        Iterable<Vertex> calendarRoots = graph.getVertices(CAL_ROOT_PROP, calendarName);
        
        if (calendarRoots != null && calendarRoots.iterator().hasNext())
        {
            for (Vertex calendarRoot : calendarRoots)
            {
                if (calendarRoot.getProperty(CAL_ROOT_PROP) == calendarName)
                {
                    numCalendars++;
                } // if
            } // for
        }
        
        return numCalendars;
    }    
}
