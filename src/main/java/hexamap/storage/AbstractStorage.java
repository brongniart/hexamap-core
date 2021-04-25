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
package hexamap.storage;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;
import java.util.Map;
import java.util.Set;

/**
 *
 * @param <Data> some stuff
 */
public abstract class AbstractStorage<Data> implements Storage<Data> {

    public static class OutOfRegion extends RuntimeException {

        public OutOfRegion(Coordinate c, Region region) {
            super(c + " is out of the region " + region);
        }
    }

    protected final Region region;

    public AbstractStorage(Region region) {
        assert region != null;
        this.region = region;
    }

    @Override
    public boolean containsKey(Coordinate coordinate) {
        assert coordinate != null;
        return region.contains(coordinate);
    }

    private void checkCoordinate(Coordinate coordinate) {
        assert coordinate != null;
        if (!containsKey(coordinate)) {
            throw new OutOfRegion(coordinate, region);
        }
    }

    private void checkCoordinates(Set<? extends Coordinate> keySet) {
        keySet.forEach(coordinate -> {
            assert coordinate != null;
            checkCoordinate(coordinate);
        });
    }

    @Override
    public Data get(Coordinate coordinate) {
        checkCoordinate(coordinate);
        return safeGet(coordinate);
    }

    protected abstract Data safeGet(Coordinate coordinate);

    @Override
    public Data put(Coordinate coordinate, Data data) {
        checkCoordinate(coordinate);
        return safePut(coordinate, data);
    }

    @Override
    public void putAll(Map<? extends Coordinate, ? extends Data> map) {
        checkCoordinates(map.keySet());
        map.entrySet().forEach(entry -> {
            safePut(entry.getKey(), entry.getValue());
        });
    }

    protected abstract Data safePut(Coordinate coordinate, Data data);
}
