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

import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;

/**
 *
 * @param <Data> some stuff
 */
public abstract class AbstractMap<CoordinateImpl extends Coordinate, Data> implements Map<CoordinateImpl, Data> {

    public static class OutOfRegion extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public OutOfRegion(Coordinate c, Region<Coordinate> region) {
            super(c + " is out of the region " + region);
        }
    }

    protected final Region<CoordinateImpl> region;

    @Override
    public Region<CoordinateImpl> getRegion() {
        return region;
    }

    public AbstractMap(Region<CoordinateImpl> region) {
        this.region = region;
    }
    
    @Override
    public boolean containsKey(CoordinateImpl coordinate) {
        return region.contains(coordinate) && safeGet(coordinate)!=null;
    }

    @SuppressWarnings("unchecked")
    private void checkCoordinate(CoordinateImpl coordinate) {
        if (!region.contains(coordinate)) {
            throw new OutOfRegion((Coordinate) coordinate, (Region<Coordinate>) region);
        }
    }

    @Override
    public Data get(CoordinateImpl coordinate) {
        checkCoordinate(coordinate);
        return safeGet(coordinate);
    }

    protected abstract Data safeGet(CoordinateImpl coordinate);

    @Override
    public Data put(CoordinateImpl coordinate, Data data) {
        checkCoordinate(coordinate);
        return safePut(coordinate, data);
    }

    @Override
    public void putAll(java.util.Map<? extends CoordinateImpl, ? extends Data> map) {
        checkCoordinates(map.keySet());
        map.entrySet().forEach(entry -> {
            safePut(entry.getKey(), entry.getValue());
        });
    }

    private void checkCoordinates(Set<? extends CoordinateImpl> keySet) {
        keySet.forEach(coordinate -> {
            assert coordinate != null;
            checkCoordinate(coordinate);
        });
    }

    protected abstract Data safePut(CoordinateImpl coordinate, Data data);
    
    public Stream<Entry<CoordinateImpl, Data>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT| Spliterator.IMMUTABLE), false);
    }
}