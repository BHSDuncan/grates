package com.xnlogic.grates;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.xnlogic.grates.datatypes.Grate;
import com.xnlogic.grates.entities.GraphDate;
import com.xnlogic.grates.util.GraphDateUtil;

public class DateCreationTests 
{
    private Graph graph = null;
    
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
    public void instantiateValidGrateTest() {
        final String calendarName = "TEST_CALENDAR";
        
        Grate g = new Grate(calendarName, this.graph);
        
        assertNotNull(g);
        
        g.init();
        
        g = null;
        
        assertEquals(1, GraphHelperFunctions.getCalendarCount(calendarName, this.graph));
    }

    @Test
    public void thereCanOnlyBeOneCalendarForAGivenNameTest() {       
        // call the test below twice to try to create the same calendar twice (calling it twice will also provide the proper assertions)
        this.instantiateValidGrateTest();
        this.instantiateValidGrateTest();                
    }
    
    @Test
    public void createValidDateTest() {
        final String calendarName = "TEST_CALENDAR";
        final int yearValue = 2014;
        final int monthValue = 4;
        final int dayValue = 30;
        
        Grate g = new Grate(calendarName, this.graph);
        
        g.init();
        
        GraphDate gd = g.findOrCreateDate(yearValue, monthValue, dayValue);
        
        this.verifyDateExistsInGraphForCalendar(yearValue, monthValue, dayValue, calendarName);
        
        Vertex v = gd.getVertex();
        
        assertNotNull(v);
    }
    
    @Test
    public void tryToCreateTheSameDateTwiceTest() {
        final String calendarName = "TEST_CALENDAR";
        final int yearValue = 2014;
        final int monthValue = 4;
        final int dayValue = 30;
        
        Grate g = new Grate(calendarName, this.graph);
        
        g.init();
        
        g.findOrCreateDate(yearValue, monthValue, dayValue);
        g.findOrCreateDate(yearValue, monthValue, dayValue);
        
        this.verifyDateExistsInGraphForCalendarExactlyOnce(yearValue, monthValue, dayValue, calendarName);               
    }
    
    @Test
    public void createEntireMonthTest() {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;
        final int MONTH = 1;
        final int NUM_DAYS = 31;

        this.createMonth(calendarName, YEAR, MONTH, NUM_DAYS);

        this.validateMonth(calendarName, YEAR, MONTH, NUM_DAYS);
    }
    
    @Test
    public void createEntireYearTest()
    {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;

        this.createYear(calendarName, YEAR);

        this.validateMonth(calendarName, YEAR, 1, 31);
        this.validateMonth(calendarName, YEAR, 2, 28);
        this.validateMonth(calendarName, YEAR, 3, 31);
        this.validateMonth(calendarName, YEAR, 4, 30);
        this.validateMonth(calendarName, YEAR, 5, 31);
        this.validateMonth(calendarName, YEAR, 6, 30);
        this.validateMonth(calendarName, YEAR, 7, 31);
        this.validateMonth(calendarName, YEAR, 8, 31);
        this.validateMonth(calendarName, YEAR, 9, 30);
        this.validateMonth(calendarName, YEAR, 10, 31);
        this.validateMonth(calendarName, YEAR, 11, 30);
        this.validateMonth(calendarName, YEAR, 12, 31);           
    } 
    
    @Test
    public void checkUnixTimestampTest() {
        final String calendarName = "TEST_CALENDAR";
        final int YEAR = 2014;
        final int MONTH = 4;
        final int DAY = 30;
        
        Grate g = new Grate(calendarName, this.graph);
        
        assertNotNull(g);
        
        g.init();
        
        GraphDate gd = g.findOrCreateDate(YEAR, MONTH, DAY);
        
        assertNotNull(gd);
        
        long unixDate = GraphDateUtil.getUnixTime(YEAR, MONTH, DAY);
        
        assertEquals(unixDate, gd.getUnixDate(), 1);
    }
    
    //
    // HELPER METHODS
    //
    
    private void verifyDateExistsInGraphForCalendar(int yearValue, int monthValue, int dayValue, String calendarName) {
        Vertex calRoot = GraphHelperFunctions.getCalendarRoot(calendarName, this.graph);
        
        assertNotNull(calRoot);
        
        // check year
        Vertex year = GraphHelperFunctions.getYearFromGraph(yearValue, calRoot);
        
        assertNotNull(year);
        assertEquals(yearValue, year.getProperty("grates_year"));
        
        // check month
        Vertex month = GraphHelperFunctions.getMonthFromGraph(monthValue, year);
        
        assertNotNull(month);
        assertEquals(monthValue, month.getProperty("grates_month"));

        // check day
        Vertex day = GraphHelperFunctions.getDayFromGraph(dayValue, month);
        
        assertNotNull(day);
        assertEquals(dayValue, day.getProperty("grates_day"));
    }   
    
    private void verifyDateExistsInGraphForCalendarExactlyOnce(int yearValue, int monthValue, int dayValue, String calendarName) {
        // check calendar root
        Iterable<Vertex> calendarRoots = this.graph.getVertices(this.CAL_ROOT_PROP, calendarName);

        assertEquals(1, this.getIterableSize(calendarRoots));
        
        Vertex calRoot = GraphHelperFunctions.getCalendarRoot(calendarName, this.graph);
        
        assertNotNull(calRoot);
        
        // check year
        List<Vertex> years = GraphHelperFunctions.getYearsFromGraph(yearValue, calRoot);
        
        assertEquals(1, years.size());
        
        Vertex year = GraphHelperFunctions.getYearFromGraph(yearValue, calRoot);
        
        assertNotNull(year);        
        assertEquals(yearValue, year.getProperty("grates_year"));
        
        // check month
        List<Vertex> months = GraphHelperFunctions.getMonthsFromGraph(monthValue, year);
        
        assertEquals(1, months.size());
        
        Vertex month = GraphHelperFunctions.getMonthFromGraph(monthValue, year);
        
        assertNotNull(month);
        assertEquals(monthValue, month.getProperty("grates_month"));

        // check day
        List<Vertex> days = GraphHelperFunctions.getDaysFromGraph(dayValue, month);
        
        assertEquals(1, days.size());

        Vertex day = GraphHelperFunctions.getDayFromGraph(dayValue, month);
        
        assertNotNull(day);
        assertEquals(dayValue, day.getProperty("grates_day"));
    }
    
    private int getIterableSize(Iterable<?> iterable) {
        if (iterable == null) {
            return 0;
        } // if
        
        int size = 0;
        
        Iterator<?> it = iterable.iterator();
        
        while (it.hasNext()) {
            size++;
            it.next();
        } // while
        
        return size;
    }
    
    private void createMonth(String calendarName, int year, int month, int numDays) {
        Grate g = new Grate(calendarName, this.graph);
        
        g.init();
        
        for (int i = 0; i < numDays; i++) {
            g.findOrCreateDate(year, month, (i+1));
        } // for
    } 
    
    private void createYear(String calendarName, int year)
    {
        this.createMonth(calendarName, year, 1, 31);
        this.createMonth(calendarName, year, 2, 28);
        this.createMonth(calendarName, year, 3, 31);
        this.createMonth(calendarName, year, 4, 30);
        this.createMonth(calendarName, year, 5, 31);
        this.createMonth(calendarName, year, 6, 30);
        this.createMonth(calendarName, year, 7, 31);
        this.createMonth(calendarName, year, 8, 31);
        this.createMonth(calendarName, year, 9, 30);
        this.createMonth(calendarName, year, 10, 31);
        this.createMonth(calendarName, year, 11, 30);
        this.createMonth(calendarName, year, 12, 31);
    } // createYear

    private void validateMonth(String calendarName, int yearValue, int monthValue, int totalDays)
    {
        Vertex calRoot = GraphHelperFunctions.getCalendarRoot(calendarName, this.graph);
        
        Vertex year = GraphHelperFunctions.getYearFromGraph(yearValue, calRoot);

        Vertex month = GraphHelperFunctions.getMonthFromGraph(monthValue, year);

        Iterable<Vertex> days = month.getVertices(Direction.OUT, "DAY");

        assertNotNull(days);

        Iterator<Vertex> itDays = days.iterator();

        assertTrue(itDays.hasNext());

        int numDays = 0;

        while (itDays.hasNext()) {
            itDays.next();
            numDays++;
        } // while

        assertEquals(totalDays, numDays);
    } // validateMonth
}
