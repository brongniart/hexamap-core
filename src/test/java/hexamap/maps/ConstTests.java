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

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstTests {

    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaXXSmall;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaSmall;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaMedium;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> hexaLarge;

    private static Random rand; 
            
    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        long seed = System.currentTimeMillis();
        rand = new Random(seed);
        System.err.println("seed:"+seed);
        
        hexaXXSmall = new Hexagon<Axial>(64, new Axial(rand.nextInt(),rand.nextInt()));  //    49'537 hex
        hexaSmall = new Hexagon<Axial>(256, new Axial(rand.nextInt(),rand.nextInt())); // 
        hexaMedium = new Hexagon<Axial>(1024, new Axial(rand.nextInt(),rand.nextInt())); // 3'148'801 hex
        hexaLarge = new Hexagon<Axial>(2048, new Axial(rand.nextInt(),rand.nextInt()));  //12'589'057 hex
        
      //  Triangle<Axial> triangleLarge = new Triangle<Axial>(Direction.getRandom(rand),2048*3, new Axial());
        Triangle<Axial> triangleSmall = new Triangle<Axial>(Direction.getRandom(rand),256*3, new Axial(rand.nextInt(),rand.nextInt()));// 18'883'585
        return Arrays.asList(
                new Object[][] { { new Constant<Axial, AxialExt>(hexaSmall)},
                        { new Constant<Axial, AxialExt>(triangleSmall)} 
                });
    }

    private Constant<Coordinate, Cube> map;
    
    public ConstTests(Constant<Coordinate, Cube> map) throws Exception {
        this.map = map;
    }

    //@After
    public void cleaup() {
        map.clear();
    }
    
    @Test
    public void test_01_SetValue() {
        assert map.isEmpty();
        map.setData(new AxialExt());
        assert map.size() == map.getRegion().size();
        assert !map.isEmpty();
        for (Coordinate c : map.getRegion()) {
            assert map.containsKey(c);
            assert map.get(c).equals(new AxialExt());
        }
        map.setData(null);
        assert map.isEmpty();
    }
    
    @Test
    public void test_02_IterateMap_Full() {
        
        map.setData(new AxialExt());
        Coordinate last = new AxialExt();
        for (var entry : map) {
            assert map.containsKey(entry.getKey());
            assert entry.getValue().equals(last);
            
            entry.setValue(new AxialExt(entry.getKey()));
            last = new AxialExt(entry.getKey());
        }
        map.setData(null);
        assert map.size() == 0;
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