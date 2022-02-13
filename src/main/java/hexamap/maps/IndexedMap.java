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

import java.util.Iterator;

import hexamap.coordinates.Coordinate;
import hexamap.regions.IndexedRegion;
import hexamap.regions.Region.OutOfRegion;

public abstract class IndexedMap<Data> extends AbstractMap<Data> {

    private int size = 0;

    public IndexedMap(IndexedRegion region) {
        super(region);
    }
    
    public IndexedRegion getRegion() {
        return (IndexedRegion) super.getRegion();
    }

    @Override
    public Data safeGet(Coordinate coordinate) throws OutOfRegion {
        return indexGet(getRegion().getIndex(coordinate));
    }

    protected abstract Data indexGet(int index);

    @Override
    public Data safePut(Coordinate coordinate, Data data) throws OutOfRegion {
        return safePut(getRegion().getIndex(coordinate), data);
    }
    
    public Data safePut(int index, Data data) {
        Data old = indexPut(index, data);
        if (data == null && old != null) {
            size--;
        } else if (old == null) {
            size++;
        }
        return old;
    }

    @Override
    public void clear() {
        size=0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<Entry<Coordinate, Data>> iterator() {
        return new Iterator<Entry<Coordinate, Data>>() {
            boolean hasNext = false;
            Iterator<Coordinate> iterator = getRegion().iterator();
            int index = 0;
            {
                advance();
            }
            
            private void advance() {
                while (iterator.hasNext() && indexGet(index)==null) {
                    index++;
                    iterator.next();
                }
                hasNext=iterator.hasNext();
            }
            
            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Entry<Coordinate, Data> next() {
                Entry<Coordinate, Data> entry = new Entry<Coordinate, Data>(){
                    
                    private final Coordinate coordinate = (Coordinate) iterator.next().copy();
                    private final int index_iter = index;
                    
                    @Override
                    public Coordinate getKey() {
                        return coordinate ;
                    }

                    @Override
                    public Data getValue() {
                        return indexGet(index_iter);
                    }

                    @Override
                    public Data setValue(Data data) {
                        return safePut(index_iter,data);
                    }
                };
                if (iterator.hasNext()) {
                    index++;
                }
                assert entry.getValue()!=null;
                advance();
                
                return entry;
            }};
    }
    
    protected abstract Data indexPut(int index, Data data);
}