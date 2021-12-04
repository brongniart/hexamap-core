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
import java.util.Random;

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
        Random rand = new Random();
        return Arrays.asList(new Object[][]{
            {new Axial()}});
        /*,{new Cube()},
            {new Axial(rand.nextInt(),rand.nextInt())},
            {new Cube(rand.nextInt(),rand.nextInt())},
            {new Axial(Integer.MAX_VALUE,Integer.MIN_VALUE)}});
            */
    }
    private Coordinate coordinate;

    public CoordinateTests(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @After
    public void cleaup() {
        coordinate = coordinate.getZero();
    }

    @Test
    public void testBasic() {
        
        assert coordinate.equals(Direction.NORD)==false;
        
        assert coordinate.add(Direction.NORD.next(3), 2).equals(coordinate.add(Direction.NORD.previous(3), 2));
        
        assert coordinate.add(Direction.NORD.previous(), 2).equals(coordinate.add(Direction.NORD_WEST, 2));
        assert coordinate.add(Direction.NORD_WEST.next(), 2).equals(coordinate.add(Direction.NORD, 2));
        
        int dist = new Random().nextInt(100);
        
        assert coordinate.add(Direction.SOUTH.previous(), dist).equals(coordinate.add(Direction.SOUTH_EAST, dist));
        assert coordinate.add(Direction.SOUTH_EAST.previous(), dist).equals(coordinate.add(Direction.NORD_EAST, dist));
        assert coordinate.add(Direction.NORD_EAST.previous(), dist).equals(coordinate.add(Direction.NORD, dist));
        assert coordinate.add(Direction.NORD.previous(), dist).equals(coordinate.add(Direction.NORD_WEST, dist));
        assert coordinate.add(Direction.NORD_WEST.previous(), dist).equals(coordinate.add(Direction.SOUTH_WEST, dist));
        assert coordinate.add(Direction.SOUTH_WEST.previous(), dist).equals(coordinate.add(Direction.SOUTH, dist));

        
        assert coordinate.add(Direction.SOUTH.next(), dist).equals(coordinate.add(Direction.SOUTH_WEST, dist));
        assert coordinate.add(Direction.SOUTH_WEST.next(), dist).equals(coordinate.add(Direction.NORD_WEST, dist));
        assert coordinate.add(Direction.NORD_WEST.next(), dist).equals(coordinate.add(Direction.NORD, dist));
        assert coordinate.add(Direction.NORD.next(), dist).equals(coordinate.add(Direction.NORD_EAST, dist));
        assert coordinate.add(Direction.NORD_EAST.next(), dist).equals(coordinate.add(Direction.SOUTH_EAST, dist));
        assert coordinate.add(Direction.SOUTH_EAST.next(), dist).equals(coordinate.add(Direction.SOUTH, dist));
        
        Coordinate tmp = coordinate.add(Direction.NORD,1);
        assert tmp.equals(coordinate.add(Direction.NORD, 1));
        assert tmp.add(Direction.NORD,1).equals(coordinate.add(Direction.NORD, 2));
        tmp.move(Direction.NORD, 1);
        assert tmp.equals(coordinate.add(Direction.NORD, 2));
        
        assert coordinate.createCoordinate(0, 0).getClass().equals(coordinate.getClass());
        assert coordinate.createCoordinateXZ(0, 0).getClass().equals(coordinate.getClass());
        assert coordinate.createCoordinateYZ(0, 0).getClass().equals(coordinate.getClass());
    }

    @Test
    public void testGetNeigbours() {
        int count;

        count = 0;
        Direction d = Direction.NORD;
        for (Coordinate c : coordinate.getNeigbours()) {
            count++;
            assert coordinate.add(d, 1).equals(c);
            d = d.next();
        }
        assert count == 6;

        count = 0;
        int distance = 128;
        for (Coordinate c : coordinate.getNeigbours(distance)) {
            count++;
            assert coordinate.distance(c) == distance;
        }
        assert count == 6 * distance;
    }

    @Test
    public void testGetAllNeigbours() {
        int count;

        count = 0;
        Iterator<Coordinate> iter = coordinate.getNeigbours().iterator();
        for (Coordinate c : coordinate.getAllNeigbours(1)) {
            count++;
            assert c.equals(iter.next());
        }
        assert !iter.hasNext();
        
        count = 0;
        int count_iter = 0;
        int distance=128;
        int distance_origin=distance;
        for (Coordinate c : coordinate.getAllNeigbours(distance)) {
            count++;
            count_iter++;
            if (count_iter==6*distance+1) {
                distance--;
                count_iter=1;
            }
            assert coordinate.distance(c)==distance;
        }
        assert count == 3*(distance_origin*(distance_origin+1));
        
        count = 0;
        distance=1024;
        for (@SuppressWarnings("unused") Coordinate c : coordinate.getAllNeigbours(distance)) {
            count++;
        }
        assert count==3*(distance*(distance+1));
    }
}
