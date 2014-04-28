package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Vertex;

public class GraphDate extends AbstractGraphDate 
{
    public GraphDate(Vertex v)
    {
        this.backingVertex = v;
    } // GraphDate
    
	@Override
    long getUnixDate() 
    {
        return 0;
    } // getUnixDate
} // GraphDate
