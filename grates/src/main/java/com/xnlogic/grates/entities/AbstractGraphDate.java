package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractGraphDate {
    protected final String VERT_UNIX_DATE_PROP = "grates_unix_date";
    protected final String DATE_EDGE_PROP = "grates_edge_value";
    
    protected Vertex backingVertex;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        Long unixDate = 0L;
        
        unixDate = this.backingVertex.getProperty(this.VERT_UNIX_DATE_PROP);
        
        return unixDate.longValue();
    } // getUnixDate
    
    protected final Vertex getDateVertexByEdgeValue(int value, String edgeLabel, Direction direction) {
        Vertex toReturn = null;
        
        Iterable<Edge> edges = this.backingVertex.getEdges(direction, edgeLabel);
        
        for (Edge e : edges)
        {
            if ((Integer)e.getProperty(this.DATE_EDGE_PROP) == value)
            {
                toReturn = e.getVertex(Direction.IN);
                break;
            } // if
        } // for
        
        return toReturn;
    } // getDateVertexByEdgeValue

    protected final Vertex getDateVertexByOutgoingEdgeValue(int value, String edgeLabel) {
        return getDateVertexByEdgeValue(value, edgeLabel, Direction.OUT);
    } // getDateVertexByOutgoingEdgeValue

    protected final Vertex getDateVertexByIncomingEdgeValue(int value, String edgeLabel) {
        return getDateVertexByEdgeValue(value, edgeLabel, Direction.IN);
    } // getDateVertexByIncomingEdgeValue

    protected final <T> Vertex findVertexFromVertices(String vertProp, T vertPropValue, Iterable<Vertex> vertices) {
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
} // AbstractGraphDate
