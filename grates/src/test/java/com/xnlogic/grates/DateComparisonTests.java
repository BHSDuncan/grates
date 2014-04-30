package com.xnlogic.grates;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.xnlogic.grates.datatypes.Grate;
import com.xnlogic.grates.entities.GraphDate;

public class DateComparisonTests {

    private KeyIndexableGraph graph = null;
    
    private final String CAL_ROOT_PROP = "grates_calendar_name";
    
    @Before
    public void setUp() throws Exception {
        this.graph = new TinkerGraph();
    }

    @After
    public void tearDown() throws Exception {
        this.graph.shutdown();
        this.graph = null;
    }

    @Test
    public void graphDateObjectEqualityTest() {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;
        final int MONTH = 4;
        final int DAY = 30;

        Grate g = new Grate(calendarName, this.graph);
        
        assertNotNull(g);
        
        g.init();
        
        GraphDate gd1 = g.findOrCreateDate(YEAR, MONTH, DAY);
        GraphDate gd2 = g.findOrCreateDate(YEAR, MONTH, DAY);
        
        assertEquals(gd1, gd2);
        
        boolean testEquals = gd1.equals(gd2);

        assertTrue(testEquals);

        testEquals = gd1.equals(null);

        assertFalse(testEquals);

        testEquals = gd1.equals(gd1);

        assertTrue(testEquals);

        testEquals = gd1.equals(new Object());

        assertFalse(testEquals);

        assertTrue(gd1.hashCode() == gd2.hashCode());
    }
    
    @Test
    public void graphDateCreationAndRetrievalTest() {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;
        final int MONTH = 4;
        final int DAY = 30;

        Grate g = new Grate(calendarName, this.graph);
        
        g.init();
        
        GraphDate gd1 = g.findOrCreateDate(YEAR, MONTH, DAY);
        
        g = null;
        
        g = new Grate(calendarName, this.graph);
        
        g.init();
        
        GraphDate gd2 = g.findDate(YEAR, MONTH, DAY);
        
        assertEquals(gd1, gd2);
    }
    
}
