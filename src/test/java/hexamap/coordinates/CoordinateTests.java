/*
 * Copyright (c) 2021, Jérôme Brongniart
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without 
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package hexamap.coordinates;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 *
 */
@RunWith(Parameterized.class)
public class CoordinateTests {

    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        return Arrays.asList(new Object[][]{
            {new Axial()},
            {new Cube()},
            {new hexamap.storage.indexators.IndexedCoordinate()},});
    }
    private Coordinate center;

    public CoordinateTests(Coordinate coordinate) {
        center = coordinate;
    }

    @After
    public void cleaup() {
        center = center.createCoordinate(0, 0);
    }
    Coordinate tmp;

    @Test
    public void testBasic() {
        tmp = center.getNext(Direction.NORD);
        assert tmp.equals(center.add(Direction.NORD, 1));
        assert tmp.getNext(Direction.NORD).equals(center.add(Direction.NORD, 2));
    }

    @Test
    public void testGetNeigbours() {
        int count;

        count = 0;
        Direction d = Direction.NORD;
        for (Coordinate c : center.getNeigbours()) {
            count++;
            assert center.add(d, 1).equals(c);
            d = d.next();
        }
        assert count == 6;

        count = 0;
        int distance = 128;
        for (Coordinate c : center.getNeigbours(distance)) {
            count++;
            assert center.distance(c) == distance;
        }
        assert count == 6 * distance;
    }

    @Test
    public void testGetAllNeigbours() {
        int count;

        count = 0;
        Iterator<Coordinate> iter = center.getNeigbours().iterator();
        for (Coordinate c : center.getAllNeigbours(1)) {
            count++;
            assert c.equals(iter.next());
        }
        assert !iter.hasNext();
        
        count = 0;
        int d=2;
        for (Coordinate c : center.getAllNeigbours(2)) {
            count++;
            if (count==13) {
                d--;
            }
            assert center.distance(c)==d;
        }
        assert count == 6 + 6 * 2;
        
        count = 0;
        for (Coordinate c : center.getAllNeigbours(1024)) {
            count++;
        }
        assert count==3148800;
    }
}
