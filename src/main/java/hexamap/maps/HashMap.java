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

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;

/**
 *
 * @param <Data> some stuff
 */
public class HashMap<CoordinateImpl extends Coordinate,Data> extends AbstractMap<CoordinateImpl,Data> {

    private final java.util.HashMap<CoordinateImpl, Data> map;

    public HashMap(Region<CoordinateImpl> region) {
        super(region);
        map = new java.util.HashMap<CoordinateImpl, Data>();
    }

    @Override
    protected Data safeGet(CoordinateImpl coordinate) {
        return map.get(coordinate);
    }

    @Override
    protected Data safePut(CoordinateImpl coordinate, Data data) {
        return map.put(coordinate, data);
    }

    @Override
    public int size() {
        cleanup();
        return map.size();
    }
    private void cleanup() {
        map.values().removeAll(Collections.singleton(null));
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Iterator<java.util.Map.Entry<CoordinateImpl, Data>> iterator() {
        return map.entrySet().iterator();
    }
    
    @Override
    public Stream<Entry<CoordinateImpl, Data>> stream() {
        return map.entrySet().stream();
    }
    
    @Override
    public Stream<Entry<CoordinateImpl, Data>> parallelStream() {
        return map.entrySet().parallelStream();
    }
}