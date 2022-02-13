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

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;
import hexamap.regions.Region.OutOfRegion;

/**
 *
 * @param <Data> some stuffs
 */
public interface Map<Data>
        extends java.util.Map<Coordinate, Data>, Iterable<Entry<Coordinate, Data>> {

    @Override
    default public boolean containsKey(Object object) {
        throw new ClassCastException();
    }
    
    public abstract boolean containsKey(Coordinate coordinate);

    @Override
    default public Data get(Object object) {
        throw new ClassCastException();
    }

    public abstract Data get(Coordinate coordinate) throws OutOfRegion;
    
    @Override
    default public Data remove(Object object) {
        throw new ClassCastException();
    }

    default public Data remove(Coordinate coordinate) {
        return put(coordinate, null);
    }
    
    public abstract Region getRegion();
    
    // Unsupported methods from Map:
    @Override
    default public Set<Coordinate> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    default public Collection<Data> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    default public Set<Entry<Coordinate, Data>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    default public boolean containsValue(Object object) {
        throw new UnsupportedOperationException();
    }

    public Stream<Entry<Coordinate, Data>> stream();

    public Stream<Entry<Coordinate, Data>> parallelStream();
}