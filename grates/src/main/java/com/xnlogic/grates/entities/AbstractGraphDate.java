package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GraphUtil;

public abstract class AbstractGraphDate {    
    protected Vertex backingVertex;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        return this.backingVertex != null ? GraphUtil.getUnixTimeFromVertex(this.backingVertex) : 0;
    } // getUnixDate
    
    public boolean isGreaterThanOrEqualTo(AbstractGraphDate rhs) {
        return (isGreaterThan(rhs) || this.equals(rhs));
    } // isGreaterThanOrEqualTo
    
    public boolean isGreaterThan(AbstractGraphDate rhs) {
        return this.getUnixDate() > rhs.getUnixDate(); 
    } // isGreaterThan

    public boolean isLessThanOrEqualTo(AbstractGraphDate rhs) {
        return (isLessThan(rhs) || this.equals(rhs));
    } // isLessThanOrEqualTo
    
    public boolean isLessThan(AbstractGraphDate rhs) {
        return this.getUnixDate() < rhs.getUnixDate();
    } // isLessThan
} // AbstractGraphDate
