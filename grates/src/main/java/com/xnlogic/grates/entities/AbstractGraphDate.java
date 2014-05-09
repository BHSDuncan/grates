package com.xnlogic.grates.entities;

import com.tinkerpop.blueprints.Vertex;
import com.xnlogic.grates.util.GraphUtil;

public abstract class AbstractGraphDate implements Comparable<AbstractGraphDate> {
    private final int EQUAL = 0;
    private final int GT = 1;
    private final int LT = -1;
    
    protected Vertex backingVertex;
    
    public final Vertex getVertex() {
        return this.backingVertex;
    } // getVertex
        
    public final long getUnixDate() {
        return this.backingVertex != null ? GraphUtil.getUnixTimeFromVertex(this.backingVertex) : 0;
    } // getUnixDate
    
    public boolean isGreaterThanOrEqualTo(AbstractGraphDate rhs) {
        return (this.isGreaterThan(rhs) || this.compareTo(rhs) == EQUAL);
    } // isGreaterThanOrEqualTo
    
    public boolean isGreaterThan(AbstractGraphDate rhs) {
        return this.compareTo(rhs) == GT; 
    } // isGreaterThan

    public boolean isLessThanOrEqualTo(AbstractGraphDate rhs) {
        return (this.isLessThan(rhs) || this.compareTo(rhs) == EQUAL);
    } // isLessThanOrEqualTo
    
    public boolean isLessThan(AbstractGraphDate rhs) {
        return this.compareTo(rhs) == LT;
    } // isLessThan
    
    public int compareTo(AbstractGraphDate o) {
        if (this.equals(o)) {
            return this.EQUAL;
        } // if
        
        if (this.getUnixDate() > o.getUnixDate()) {
            return this.GT;
        } // if
            
        if (this.getUnixDate() < o.getUnixDate()) {
            return this.LT;
        } // if
        
        return this.EQUAL;
    } // compareTo
} // AbstractGraphDate
