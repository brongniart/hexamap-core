/*
 * Copyright (c) 2021, J�r�me Brongniart
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import hexamap.coordinates.Coordinate;
import hexamap.regions.base.BaseRegion;

/**
 *
 * @param <Data> some stuff
 */
public class ArrayMap<CoordinateImpl extends Coordinate, Data> extends IndexedMap<CoordinateImpl, Data> {

    private Data[] array;

    @SuppressWarnings("unchecked")
    public ArrayMap(BaseRegion<CoordinateImpl> region,Class<?> dataClass) {
        super(region);
        array = (Data[]) Array.newInstance(dataClass, region.size());
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(array, null);
    }

    @Override
    protected Data indexGet(int index) {
        return array[index];
    }

    @Override
    protected Data indexPut(int index, Data data) {
        Data old = array[index];
        array[index] = data;
        return old;
    }
    
    @Override
    public Stream<Entry<CoordinateImpl, Data>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(array, 0, size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT| Spliterator.IMMUTABLE), false);
    }
    
    @Override
    public Stream<Entry<CoordinateImpl, Data>> parallelStream() {
        return StreamSupport.stream(Spliterators.spliterator(array, 0, size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT| Spliterator.IMMUTABLE), true);
    }
}