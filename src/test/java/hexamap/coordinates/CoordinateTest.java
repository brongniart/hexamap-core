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

import org.junit.Test;

/**
 *
 * 
 */
public class CoordinateTest {
    
    Coordinate center = new Axial();
    Coordinate tmp;
    
    public CoordinateTest() {
    }
    
    @Test
    public void testBaseLogic() {
        tmp = center.getNext(Direction.NORD);
        assert tmp.equals(center.add(Direction.NORD,1));
        assert tmp.getNext(Direction.NORD).equals(center.add(Direction.NORD,2));
    }
    
    /**
     * Test of getNeigbours method, of class Coordinate.
     */
    @Test
    public void testGetNeigbours() {
        int count;
        
        count=0;
        for (Coordinate c : center.getNeigbours()) {
            count++;
            //System.out.println(c + " distance: "+center.distance(c));
        }
        assert count==6;
        count=0;
        for (Coordinate c : center.getNeigbours(2)) {
            count++;
            //System.out.println(c + " distance: "+center.distance(c));
        }
        assert count==6*2;
        
        count=0;
        for (Coordinate c : center.getNeigbours(3)) {
            count++;
            //System.out.println(c + " distance: "+center.distance(c));
        }
        assert count==6*3;
    }

    /**
     * Test of getAllNeigbours method, of class Coordinate.
     */
    //@Test
    public void testGetAllNeigbours() {
        int count;
        
        count=0;
        for (Coordinate c : center.getAllNeigbours(1)) {
            count++;
            System.out.println(c + " distance: "+center.distance(c));
        }
        assert count==6;
        
        count=0;
        for (Coordinate c : center.getAllNeigbours(2)) {
            count++;
            //System.out.println(c + " distance: "+center.distance(c));
        }
        assert count==6+6*2;
        
        count=0;
        for (Coordinate c : center.getAllNeigbours(3)) {
            count++;
            //System.out.println(c + " distance: "+center.distance(c));
        }
        assert count==6+6*2+6*3;
        
        count=0;
        for (Coordinate c : center.getAllNeigbours(1024*1)) {
            count++;
        }
        System.out.println("hexamap.coordinates.CoordinateTest.testGetNeigbours() - end: "+count);
    }
}
