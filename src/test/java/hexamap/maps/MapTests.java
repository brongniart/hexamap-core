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
package hexamap.maps;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hexamap.coordinates.Axial;
import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Cube;
import hexamap.regions.Hexagon;

/**
 *
 * 
 */
@RunWith(Parameterized.class)
public class MapTests {

    @SuppressWarnings("unused")
    private static Hexagon<Axial> regionXXSmall;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> regionMedium;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> regionLarge;

    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        regionXXSmall = new Hexagon<Axial>(100, new Axial()); // 49'537 hex
        regionMedium = new Hexagon<Axial>(1024, new Axial()); // 3'148'801 hex
        regionLarge = new Hexagon<Axial>(2048, new Axial());
        return Arrays.asList(
                new Object[][] { { new HashMap<Axial, AxialExt>(regionXXSmall), regionXXSmall },
                        { new Constant<Axial, AxialExt>(regionXXSmall), regionXXSmall } });
    }

    private Map<Coordinate, Cube> map;
    private final Hexagon<Coordinate> region;

    public MapTests(Map<Coordinate, Cube> map, Hexagon<Coordinate> region) throws Exception {
        this.map = map;
        this.region = region;
    }

    @After
    public void cleaup() {
        map.clear();
    }

    @Test
    public void test_FillMap() {
        assert map.isEmpty();
        for (Coordinate c : region) {
            map.put(c, new AxialExt(c));
            assert map.get(c) != null;
            assert map.get(c).equals(new AxialExt(c));
        }
        assert map.size() == region.size();
        for (var entry : map) {
            assert entry.getKey() != null;
            assert entry.getValue() != null;
            entry.setValue(new AxialExt());
            assert entry.getValue().equals(new AxialExt());
        }
    }

    @Test
    public void test_FillMap_NewCoordinate() {
        Random random = new Random();
        assert map.isEmpty();
        int NB_ITER = region.size();
        for (int i = 0; i < NB_ITER; i++) {
            Coordinate c = region.getRandom(random);

            map.put(new Axial(c), new AxialExt(c));
            assert map.get(c) != null;
            assert map.get(new Axial(c)).equals(new AxialExt(c));
        }
    }

    public static class AxialExt extends Cube implements Externalizable {

        public AxialExt() {
            super();
        }

        public AxialExt(Coordinate c) {
            super(c);
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(getX());
            out.writeInt(getY());
            out.flush();
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            x = in.readInt();
            y = in.readInt();
            z = -x - y;
        }
    }
}