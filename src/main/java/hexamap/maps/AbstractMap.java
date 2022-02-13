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

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;
import hexamap.regions.Region.OutOfRegion;

/**
 *
 * @param <Data> some stuff
 */
public abstract class AbstractMap<Data> implements Map<Data> {
    
    private final Region region;

    @Override
    public Region getRegion() {
        return region;
    }

    public AbstractMap(Region region) {
        Objects.nonNull(region);
        this.region = region;
    }
    
    @Override
    public boolean containsKey(Coordinate coordinate) {
        try {
            return safeGet(coordinate)!=null;
        } catch (OutOfRegion e) {
            return false;
        }
    }

    private void checkCoordinate(Coordinate coordinate) throws OutOfRegion {
        if (!region.contains(coordinate)) {
            throw new OutOfRegion((Coordinate) coordinate, (Region) region);
        }
    }

    @Override
    public Data get(Coordinate coordinate) throws OutOfRegion {
        checkCoordinate(coordinate);
        return safeGet(coordinate);
    }

    protected abstract Data safeGet(Coordinate coordinate) throws OutOfRegion;

    @Override
    public Data put(Coordinate coordinate, Data data) {
        Objects.nonNull(data);
        try {
            checkCoordinate(coordinate);
        } catch (OutOfRegion e) {
            throw new RuntimeException(e);
        }
        try {
            return safePut(coordinate, data);
        } catch (OutOfRegion e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putAll(java.util.Map<? extends Coordinate, ? extends Data> map) {
        map.entrySet().forEach(entry -> {
            put(entry.getKey(), entry.getValue());
        });
    }

    protected abstract Data safePut(Coordinate coordinate, Data data) throws OutOfRegion;
    
    @Override
    public Stream<Entry<Coordinate, Data>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT| Spliterator.IMMUTABLE), false);
    }
    
    @Override
    public Stream<Entry<Coordinate, Data>> parallelStream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT| Spliterator.IMMUTABLE), true);
    }
}