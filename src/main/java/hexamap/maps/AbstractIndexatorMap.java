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

import hexamap.coordinates.Coordinate;
import hexamap.maps.indexators.Indexator;
import hexamap.regions.Region;

/**
 *
 * @param <Data>
 */
public abstract class AbstractIndexatorMap<CoordinateImpl extends Coordinate,Data> extends AbstractMap<CoordinateImpl,Data> {

    protected final Indexator indexator;
    private int size = 0;

    @SuppressWarnings("unchecked")
	public AbstractIndexatorMap(Region<CoordinateImpl> region, Indexator indexator) {
        super(region);
        assert indexator.getRegion().equals(region);
        this.indexator = indexator;
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int getIndex(Coordinate coordinate) {
        int index = indexator.index(coordinate);
        assert index >= 0 && index <= region.size();
        return index;
    }

    @Override
    public void clear() {
        size = 0;
        indexClear();
    }

    protected abstract void indexClear();

    @Override
    public Data safeGet(CoordinateImpl coordinate) {
        return indexGet(getIndex(coordinate));
    }

    protected abstract Data indexGet(int index);

    @Override
    public Data safePut(CoordinateImpl coordinate, Data data) {
        Data old = indexPut(getIndex(coordinate), data);
        if (data == null && old != null) {
            size--;
        } else if (old == null) {
            size++;
        }
        return old;
    }

    protected abstract Data indexPut(int index, Data data);

    //public abstract Data[] getBulk(Coordinate c, int numbers);
}
