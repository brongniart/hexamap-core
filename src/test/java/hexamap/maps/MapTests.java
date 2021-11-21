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
import hexamap.coordinates.Direction;
import hexamap.regions.Hexagon;
import hexamap.regions.Triangle;

/**
 *
 * 
 */
@RunWith(Parameterized.class)
public class MapTests {

    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaXXSmall;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaMedium;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaLarge;

    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        hexaXXSmall = new Hexagon<Axial>(64, new Axial());  //    49'537 hex
        hexaMedium = new Hexagon<Axial>(1024, new Axial()); // 3'148'801 hex
        hexaLarge = new Hexagon<Axial>(2048, new Axial());  // 12'576'769 hex
        
        Triangle<Axial> triangleMedium = new Triangle<Axial>(Direction.getRandom(new Random()),1024, new Axial()); 
        Triangle<Axial> triangleLarge = new Triangle<Axial>(Direction.getRandom(new Random()),2048, new Axial());
        return Arrays.asList(
                new Object[][] { { new HashMap<Axial, AxialExt>(hexaXXSmall)},
                        { new Constant<Axial, AxialExt>(hexaLarge)},
                        { new ArrayMap<Axial, AxialExt>(hexaLarge,AxialExt.class)} ,
                        { new ArrayMap<Axial, AxialExt>(triangleLarge,AxialExt.class)} });
    }

    private Map<Coordinate, Cube> map;
    
    public MapTests(Map<Coordinate, Cube> map) throws Exception {
        this.map = map;
    }

    @After
    public void cleaup() {
        map.clear();
    }

    @Test
    public void test_FillMap() {
        assert map.isEmpty();
        for (Coordinate c : map.getRegion()) {
            map.put(c, new AxialExt(c));
            assert map.get(c) != null;
            assert map.get(c).equals(new AxialExt(c));
        }
        assert map.size() == map.getRegion().size();
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
        int NB_ITER = map.getRegion().size();
        for (int i = 0; i < NB_ITER; i++) {
            Coordinate c = map.getRegion().getRandom(random);

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