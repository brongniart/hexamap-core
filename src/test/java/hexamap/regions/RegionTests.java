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
package hexamap.regions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hexamap.coordinates.Axial;
import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Cube;
import hexamap.coordinates.Direction;

/**
 *
 * 
 */
@RunWith(Parameterized.class)
public class RegionTests {

    private static Random rand; 
            
    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        long seed = System.currentTimeMillis();
        rand = new Random(seed);
        System.err.println("seed:"+seed);
        
        Set<Axial> setAxial = new Set<Axial>(new Axial(rand.nextInt(), rand.nextInt()));
        Hexagon<Axial> hexaAxial = new Hexagon<Axial>(32, new Axial(rand.nextInt(), rand.nextInt()));

        for (Axial c : hexaAxial) {
            setAxial.add(c);
        }

        Set<Cube> setCube = new Set<Cube>(new Cube(rand.nextInt(), rand.nextInt()));
        Hexagon<Cube> hexaCube = new Hexagon<Cube>(32, new Cube(rand.nextInt(), rand.nextInt()));

        for (Cube c : hexaCube) {
            setAxial.add(c);
        }

        Hexagon<Axial> hexaMax = new Hexagon<Axial>(1024, new Axial(Integer.MIN_VALUE, Integer.MAX_VALUE));
        Triangle<Cube> triangle = new Triangle<Cube>(Direction.getRandom(rand), 1024,
                new Cube(rand.nextInt(), rand.nextInt()));

        return Arrays.asList(
                new Object[][] { { hexaAxial }, { setAxial }, { hexaCube }, { setCube }, { hexaMax }, { triangle } });
    }

    private final Region<Coordinate> region;

    public RegionTests(Region<Coordinate> region) throws Exception {
        this.region = region;
    }

    @Test
    public void testContains() {
        assert region.contains(region.getCenter());

        Coordinate tmp = region.getCenter();
        region.setCenter(tmp.add(Direction.NORD, 10));
        assert region.contains(region.getCenter());

        try {
            region.clear();
        } catch (UnsupportedOperationException e) {
        }
        assert region.contains(region.getCenter());

        try {
            region.remove(region.getCenter());
            assert false;
        } catch (UnsupportedOperationException e) {
        }
        assert region.contains(region.getCenter());

        region.setCenter(tmp);

        int count = 0;
        for (Coordinate c : region) {
            assert region.contains(c);
            count++;
        }

        assert count == region.size();
    }

    @Test
    public void testGetRandom() {
        for (@SuppressWarnings("unused")
        Coordinate c : region) {
            assert region.contains(region.getRandom(rand));
        }
    }
}