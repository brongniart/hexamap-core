/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexamap.regions.storages;

import hexamap.coordinates.Axial;
import hexamap.regions.indexators.NeighboorsIndexator;
import hexamap.regions.Hexagon;
import org.junit.Test;

/**
 *
 * @author jerome
 */
public class ArrayStorageTest {
    
    public ArrayStorageTest() {
    }

    @Test
    public void testIterator() throws Exception {
        System.out.println("hexamap.regions.storages.ArrayStorageTest.testIterator()");
        
        Hexagon<Axial> region = new Hexagon<>(128,Axial.class);
        NeighboorsIndexator indexator = new NeighboorsIndexator(region);
        ArrayStorage<Axial> storage = new ArrayStorage<>(region,indexator,Axial.class);
        
        assert storage.isEmpty();
        region.forEach(c -> {
            //System.out.println("Coordinate: "+c+" Index: "+indexator.index(c));
            assert storage.get(c)==null;
            storage.put(c, new Axial(c.getX(),c.getY()));
            assert storage.get(c).equals(c);
        });
        System.out.println(storage.size()+" - "+region.size());
        assert storage.size()==region.size();
    }
}
