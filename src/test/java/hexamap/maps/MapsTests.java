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

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hexamap.coordinates.AbstractCoordinate;
import hexamap.coordinates.Axial;
import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Cube;
import hexamap.maps.indexators.IndexedCoordinate;
import hexamap.maps.indexators.NeighboorsIndexator;
import hexamap.regions.Hexagon;

/**
 *
 * 
 */
@RunWith(Parameterized.class)
public class MapsTests {

    @SuppressWarnings("unused")
    private static Hexagon<Axial> regionXXSmall;
    @SuppressWarnings("unused")
    private static Hexagon<Axial> regionMedium;
    @SuppressWarnings("unused")
	private static Hexagon<Axial> regionLarge;
    
    @Parameters
    public static Collection<Object[]> getParameters() throws Exception {
        regionXXSmall=new Hexagon<Axial>(100,Axial.class);  // 49'537 hex
        regionMedium=new Hexagon<Axial>(1024,Axial.class);  // 3'148'801 hex
        regionLarge=new Hexagon<Axial>(2048,Axial.class);
        return Arrays.asList(new Object[][]{
            {new HashMap<Axial,AxialExt>(regionMedium),regionMedium},
            {new ArrayMap<Axial,AxialExt>(regionMedium, new NeighboorsIndexator(regionMedium),AxialExt.class),regionMedium},
            {new FileMap<Axial,AxialExt>(regionXXSmall, new NeighboorsIndexator(regionXXSmall),AxialExt.class),regionXXSmall},
        });
    }
    private Map<AbstractCoordinate,Cube> storage;
    private final Hexagon<AbstractCoordinate> region;

    public MapsTests(Map<AbstractCoordinate,Cube> storage,Hexagon<AbstractCoordinate> region) throws Exception {
        this.storage=storage;
        this.region=region;
    }
    
    @After
    public void cleaup() {
        storage.clear();
    }
    
    @Test
    public void test_Iteratif_WithCache() {
        System.out.println("hexamap.regions.storages.StorageTest.test_Iteratif_WithCache()");
        assert storage.isEmpty();
        for (AbstractCoordinate c : region) {
        	IndexedCoordinate coordinate = new IndexedCoordinate(c);
            storage.put(coordinate, new AxialExt(c));
            assert new IndexedCoordinate(c).equals(coordinate);
            storage.get(new IndexedCoordinate(coordinate));
            assert storage.get(new IndexedCoordinate(coordinate)).equals(storage.get(coordinate));
            assert storage.get(coordinate)!=null;
            assert storage.get(coordinate).equals(new AxialExt(c));
        }
        System.out.println("hexamap.regions.storages.StorageTest.test_Iteratif_WithCache() - End ("+String.format("%,d", storage.size())+" access)");
        //assert storage.size() == region.size() - 1;
    }
    
    @Test
    public void test_Iteratif_NoCache() {
        System.out.println("hexamap.regions.storages.StorageTest.testIterator_Iteratif_No_Cache()");
        assert storage.isEmpty();
        for (Coordinate c : new Axial(0,0).getAllNeigbours(region.getRange())) {
            storage.put(new Axial(c), new AxialExt(c));
            assert storage.get(new Axial(c))!=null;
            assert storage.get(new Axial(c)).equals(new AxialExt(c));
        }
        System.out.println("hexamap.regions.storages.StorageTest.testIterator_Iteratif_No_Cache() - End ("+String.format("%,d", storage.size())+" access)");
        //assert storage.size() == region.size() - 1;
    }

    @Test
    public void test_Random_WithCache() {
        System.out.println("hexamap.regions.storages.StorageTest.testIterator_Random_Cache()");
        assert storage.isEmpty();
        
        int NB_ITER = region.size();
        for (int i=0;i<NB_ITER;i++) {
        	IndexedCoordinate c = new IndexedCoordinate(region.getRandom());
            
            storage.put(c, new AxialExt(c));
            assert storage.get(c)!=null;
            assert storage.get(c).equals(new AxialExt(c));
        }
        assert storage.size() <= NB_ITER;
        System.out.println("hexamap.regions.storages.StorageTest.testIterator_Random_Cache() - End ("+String.format("%,d", NB_ITER)+" access)");
    }  
    
    @Test
   public void test_Random_NoCache() {
        System.out.println("hexamap.regions.storages.StorageTest.testIterator_Random_No_Cache()");
        assert storage.isEmpty();

        int NB_ITER = region.size();
        for (int i=0;i<NB_ITER;i++) {
        	AbstractCoordinate c = region.getRandom();
            
            storage.put(c, new AxialExt(c));
            assert storage.get(c)!=null;
            assert storage.get(c).equals(new AxialExt(c));
        }
        assert storage.size() <= NB_ITER;
        System.out.println("hexamap.regions.storages.StorageTest.testIterator_Random_No_Cache() - End ("+String.format("%,d", NB_ITER)+" access)");
    }
    
    public static class AxialExt extends Cube implements Externalizable  {

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
            setX(in.readInt());
            setY(in.readInt());
        }
    }
}
