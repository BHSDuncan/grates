package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractGraphDate 
{
    protected Vertex backingVertex;
    
    public final Vertex getVertex()
    {
        return this.backingVertex;
    } // getVertex
    
    abstract long getUnixDate();
} // AbstractGraphDate
