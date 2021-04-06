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
package hexamap.regions.storages;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Region;
import hexamap.storage.indexators.Indexator;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @param <Data> some stuff
 */
public class ArrayStorage<Data> extends AbstractIndexatorStorage<Data> {

    private final Data[] array;

    public ArrayStorage(Region region, Indexator indexator, Class<Data> dataClass) {
        super(region, indexator);
        this.array = (Data[]) Array.newInstance(dataClass, region.size());
    }

    @Override
    protected Data indexGet(int index) {
        assert index < array.length;
        return array[index];
    }

    @Override
    protected Data indexPut(int index, Data data) {
        assert index < array.length;
        Data old = array[index];
        array[index] = data;
        return old;
    }

    @Override
    public void indexClear() {
        Arrays.fill(array, null);
    }

    @Override
    public Iterator<Entry<Coordinate, Data>> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
