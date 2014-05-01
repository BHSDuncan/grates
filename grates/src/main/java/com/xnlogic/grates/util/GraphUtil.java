package com.xnlogic.grates.util;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class GraphUtil {
    private static final String DATE_EDGE_PROP = "grates_edge_value";
    
    public static Vertex getDateVertexByEdgeValue(int value, String edgeLabel, Direction direction, Vertex startingVertex) {
        Vertex toReturn = null;
        
        Iterable<Edge> edges = startingVertex.getEdges(direction, edgeLabel);
        
        for (Edge e : edges)
        {
            if ((Integer)e.getProperty(DATE_EDGE_PROP) == value)
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

    public static Vertex getDateVertexByIncomingEdgeValue(int value, String edgeLabel, Vertex startingVertex) {
        return getDateVertexByEdgeValue(value, edgeLabel, Direction.IN, startingVertex);
    } // getDateVertexByIncomingEdgeValue

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
} // GraphUtil