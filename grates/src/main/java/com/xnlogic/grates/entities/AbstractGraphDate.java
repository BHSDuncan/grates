package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GlobalStrings;

public abstract class AbstractGraphDate {    
    protected Vertex backingVertex;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        Long unixDate = 0L;
        
        unixDate = this.backingVertex.getProperty(GlobalStrings.getString("unix_date_property"));
        
        return unixDate.longValue();
    } // getUnixDate    
} // AbstractGraphDate
