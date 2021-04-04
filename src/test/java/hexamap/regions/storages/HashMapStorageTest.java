/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexamap.regions.storages;

import hexamap.coordinates.Axial;
import hexamap.regions.Hexagon;
import hexamap.regions.Region;
import org.junit.Test;

/**
 *
 * @author jerome
 */
public class HashMapStorageTest {
    
    public HashMapStorageTest() {
    }

    @Test
    public void testIterator() throws Exception {
        Region<Axial> region = new Hexagon<>(128,Axial.class);
        
        HashMapStorage<Axial> storage = new HashMapStorage<>(region);
        
        assert storage.isEmpty();
        region.forEach(c -> {
            assert storage.get(c)==null;
            storage.put(c, c);
            assert storage.get(c).equals(c);
        });
        assert storage.size()==region.size();
    }
    
}
