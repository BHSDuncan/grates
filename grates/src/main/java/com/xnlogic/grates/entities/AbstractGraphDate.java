package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractGraphDate {
    protected final String VERT_UNIX_DATE_PROP = "grates_unix_date";
    protected Vertex backingVertex;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        Long unixDate = 0L;
        
        unixDate = this.backingVertex.getProperty(this.VERT_UNIX_DATE_PROP);
        
        return unixDate.longValue();
    } // getUnixDate
} // AbstractGraphDate
