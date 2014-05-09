package com.xnlogic.grates.util;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class GraphUtil {
    public static Vertex getDateVertexByEdgeValue(int value, String edgeLabel, Direction direction, Vertex startingVertex) {
        Vertex toReturn = null;
        
        Iterable<Edge> edges = startingVertex.getEdges(direction, edgeLabel);
        
        for (Edge e : edges)
        {
            if ((Integer)e.getProperty(GlobalStrings.getString("date_segment_edge_property_name")) == value)
            {
                toReturn = e.getVertex(Direction.IN);
                break;
            } // if
        } // for
        
        return toReturn;
    } // getDateVertexByEdgeValue

    public static Vertex getDateVertexByOutgoingEdgeValue(int value, String edgeLabel, Vertex startingVertex) {
        return getDateVertexByEdgeValue(value, edgeLabel, Direction.OUT, startingVertex);
    } // getDateVertexByOutgoingEdgeValue

    public static <T> Vertex findVertexFromVertices(String vertProp, T vertPropValue, Iterable<Vertex> vertices) {
        Vertex toReturn = null;
        
        if (vertices != null && vertices.iterator().hasNext()) {
            for (Vertex vertex : vertices) {
                if (vertex.getProperty(vertProp) == vertPropValue) {
                    toReturn = vertex;
                    break;
                } // if
            } // for
        } // if
        
        return toReturn;
    } // findVertexFromVertices
    
    public static Vertex createDateSegmentInGraph(int value, String segmentPropKey, String segmentEdgeLabel, long unixDate, Vertex sourceVertex, Graph graph) {
        Vertex dateSegmentVertex = graph.addVertex(null);
                
        dateSegmentVertex.setProperty(segmentPropKey, value);
        dateSegmentVertex.setProperty(GlobalStrings.getString("unix_date_property"), unixDate);
        
        Edge e = sourceVertex.addEdge(segmentEdgeLabel, dateSegmentVertex);
        e.setProperty(GlobalStrings.getString("date_segment_edge_property_name"), value);
        
        return dateSegmentVertex;
    } // createDateSegmentInGraph
    
    public static long getUnixTimeFromVertex(Vertex v) {
        if (v == null) {
            return 0;
        } // if
        
        return v.getProperty(GlobalStrings.getString("unix_date_property"));
    } // getUnixTimeFromVertex
} // GraphUtil
