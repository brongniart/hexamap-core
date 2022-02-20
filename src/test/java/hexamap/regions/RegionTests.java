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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hexamap.coordinates.Axial;
import hexamap.coordinates.Coordinate;
import hexamap.regions.base.Hexagon;

@RunWith(Parameterized.class)
public class RegionTests {

    private static Random rand;

    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {

        long seed = System.currentTimeMillis();
        rand = new Random(seed);
        System.err.println("seed:" + seed);

        ArrayList<Object[]> result = new ArrayList<Object[]>(Arrays.asList(IndexedRegionTests.getParameters(rand)));

        CoordinateArrayList list = new CoordinateArrayList();
        Hexagon hexaAxial = new Hexagon(2048, new Axial(rand.nextInt(), rand.nextInt()));

        for (Coordinate c : hexaAxial) {
            list.add(c);
        }
        result.add(new Object[] { list });

        return result;
    }

    private Region region = null;

    public RegionTests(Region region) throws Exception {
        if (this.region != region) {
            System.out.println(
                    this.getClass() + ", region:" + region.getClass() + ": " + String.format("%,d", region.size()));
            this.region = region;
        }
    }

    @Test
    public void testContains() {
        int count = 0;
        for (Coordinate c : region) {
            assert region.contains(new Axial(c));
            count++;
        }

        assert count == region.size();
    }

    @Test
    @SuppressWarnings("unused")
    public void testGetRandom() {
        for (Coordinate unused : region) {
            Coordinate c = region.getRandom(rand);
            if (!region.contains(c)) {
                System.err.println(c);
            }
            assert region.contains(new Axial(c));
        }
    }
}