package eirb.pg203;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatePairTest {

    @Test
    public void testDatePairValues() {
        String start = "20240101";
        String end = "20241231";

        DatePair pair = new DatePair(start, end);

        Assertions.assertEquals(start, pair.getStartDate());
        Assertions.assertEquals(end, pair.getEndDate());
    }

    @Test
    public void testDatePairWithNulls() {
        DatePair pair = new DatePair("20240101", null);

        Assertions.assertEquals("20240101", pair.getStartDate());
        Assertions.assertNull(pair.getEndDate());
    }
}
