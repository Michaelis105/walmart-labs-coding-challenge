package Venue;

import org.junit.Assert;
import org.junit.Test;

public class VenueTest {

    @Test
    public void Basic() {
        Venue v1 = new Venue(-1, -1);
        Venue v2 = new Venue(0, 0);
        Venue v3 = new Venue(20, 10);

        Assert.assertNotNull(v1);
        Assert.assertNotNull(v2);
        Assert.assertNotNull(v3);
    }

}
