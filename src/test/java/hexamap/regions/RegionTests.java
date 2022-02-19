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
import hexamap.coordinates.Cubic;
import hexamap.coordinates.Direction;
import hexamap.regions.base.Hexagon;
import hexamap.regions.base.Triangle;

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
        System.err.println("seed:" + seed);

        LinkedHashSet setAxial = new LinkedHashSet();
        Hexagon hexaAxial = new Hexagon(32, new Axial(rand.nextInt(), rand.nextInt()));

        for (Coordinate c : hexaAxial) {
            setAxial.add(c);
        }

        LinkedHashSet setCube = new LinkedHashSet();
        Hexagon hexaCube = new Hexagon(32, new Cubic(rand.nextInt(), rand.nextInt()));

        for (Coordinate c : hexaCube) {
            setAxial.add(c);
        }

        Hexagon hexaMax = new Hexagon(1024, new Axial(Integer.MIN_VALUE, Integer.MAX_VALUE));
        Triangle triangle = new Triangle(Direction.getRandom(rand), 1024*3,
                new Cubic(rand.nextInt(), rand.nextInt()));

        return Arrays.asList(new Object[][] { { new Cubic() }, { hexaAxial }, { setAxial },
                { hexaCube }, { setCube }, { hexaMax }, { triangle } });
    }

    private final Region region;

    public RegionTests(Region region) throws Exception {
        this.region = region;
        System.out.println(this.getClass() + ", region:" + region.getClass() + ": " + String.format("%,d", region.size()));
    }

    @Test
    public void testSpliterators() {
        // System.out.println(region.parallelStream().isParallel());
        // System.out.println(region.stream().isParallel());
    }

    @Test
    public void testContains() {
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