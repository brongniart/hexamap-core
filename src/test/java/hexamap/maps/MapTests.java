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
import java.util.Locale;
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
import hexamap.regions.Region.OutOfRegion;
import hexamap.regions.base.Hexagon;
import hexamap.regions.base.Triangle;

/**
 *
 * 
 */
@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapTests {

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
        hexaLarge = new Hexagon<Axial>(2048*2, new Axial(rand.nextInt(),rand.nextInt()));  //12'589'057 hex

        Triangle<Axial> triangleSmall = new Triangle<Axial>(Direction.getRandom(rand),256*3, new Axial(rand.nextInt(),rand.nextInt()));// 18'883'585
        Triangle<Axial> triangleMedium = new Triangle<Axial>(Direction.getRandom(rand),1024*2, new Axial(rand.nextInt(),rand.nextInt()));// 18'883'585
        Triangle<Axial> triangleLarge = new Triangle<Axial>(Direction.getRandom(rand),2048*4, new Axial()); // 75'515'905
        return Arrays.asList(
                new Object[][] { 
                        { new HashMap<Axial, AxialExt>(hexaXXSmall)},
                        { new HashMap<Axial, AxialExt>(triangleSmall)},
                        { new ArrayMap<Axial, AxialExt>(hexaSmall,AxialExt.class)} ,
                        { new ArrayMap<Axial, AxialExt>(triangleSmall,AxialExt.class)},
                        { new ArrayMap<Axial, AxialExt>(hexaMedium,AxialExt.class)} ,
                        { new ArrayMap<Axial, AxialExt>(triangleMedium,AxialExt.class)} 
                });
    }

    private Map<Coordinate, Cube> map;
    
    public MapTests(Map<Coordinate, Cube> map) throws Exception {
        this.map = map;
        System.out.println(this.getClass() + ", map:" + map.getClass() + ": " + String.format(Locale.US,"%,d", map.getRegion().size()));
    }

    @Test
    public void test_01_FillMap() throws OutOfRegion {
        assert map.isEmpty();
        for (Coordinate c : map.getRegion()) {
            assert map.getRegion().contains(c);
            assert !map.containsKey(c);
            map.put(c, new AxialExt(c));
            assert map.containsKey(c);
            
            assert map.get(c).equals(new AxialExt(c));
            
            map.put(c, null);
            assert !map.containsKey(c);
            
            map.put(c, new AxialExt(c));
        }
        assert map.size() == map.getRegion().size();
    }
    
    //@Test
    public void testSpliterators() {
        //System.out.println(map.parallelStream().isParallel());
        //System.out.println(map.stream().isParallel());
    }

    @Test
    public void test_02_IterateMap_Full() throws OutOfRegion {

        assert map.size() == map.getRegion().size();
        for (var entry : map) {
            assert map.containsKey(entry.getKey());
            assert entry.getValue() != null;
            assert entry.getValue().equals(map.get(entry.getKey()));
            
            entry.setValue(new AxialExt());
            assert entry.getValue().equals(new AxialExt());
            assert map.get(entry.getKey()).equals(new AxialExt());
            
            entry.setValue(null);
            assert !map.containsKey(entry.getKey());
        }
        assert map.size() == 0;
    }

    @Test
    public void test_04_FillMap_Random() throws OutOfRegion {
        assert map.isEmpty();
        int NB_ITER = map.getRegion().size();
        for (int i = 0; i < NB_ITER; i++) {
            Coordinate c = map.getRegion().getRandom(rand);
            assert map.getRegion().contains(c);
            
            map.put(c, new AxialExt(c));
            assert map.containsKey(c);
            
            assert map.get(c).equals(new AxialExt(c));
        }
    }
    
    @Test
    public void test_05_IterateMap_Partial() throws OutOfRegion {

        assert map.size() >= 0;
        for (var entry : map) {
            assert entry.getKey() != null;
            assert map.containsKey(entry.getKey());
            assert entry.getValue() != null;
            
            assert entry.getValue().equals(map.get(entry.getKey()));
            
            entry.setValue(new AxialExt());
            assert entry.getValue().equals(new AxialExt());
            assert map.get(entry.getKey()).equals(new AxialExt());
            
            entry.setValue(null);
            assert !map.containsKey(entry.getKey());
        }
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
            x = (short) in.readInt();
            y = (short) in.readInt();
            z = -x - y;
        }
    }
}