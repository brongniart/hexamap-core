/*
 * Copyright (c) 2021, J?r?me Brongniart
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
import hexamap.regions.Region;

/**
 *
 * @param <Data> some stuff
 */
public class ConstantMap<Data> extends AbstractMap<Data> {

    private Data data;

    public ConstantMap(Region region,Data data) {
        this(region);
        this.setData(data);
    }

    public ConstantMap(Region region) {
        super(region);
    }

    @Override
    protected Data safeGet(Coordinate coordinate) {
        return data;
    }

    @Override
    protected Data safePut(Coordinate coordinate, Data data) {
        Data oldData = this.data;
        this.data = data;
        return oldData;
    }

    @Override
    public int size() {
        return data==null? 0 : getRegion().size();
    }

    @Override
    public boolean isEmpty() {
        return data==null;
    }

    @Override
    public void clear() {
        data = null;
    }
    
    public class Entry implements java.util.Map.Entry<Coordinate, Data> {
        
        private ConstantMap<Data> map;
        private Coordinate coordinate;
        
        public Entry(ConstantMap<Data> map, Coordinate coordinate, Data data) {
            this.map = map;
            this.coordinate = coordinate;
        }

        @Override
        public Data setValue(Data data) {
            Data oldData = map.data;
            map.data = data;
            return oldData;
        }

        @Override
        public Coordinate getKey() {
            return coordinate;
        }

        @Override
        public Data getValue() {
            return map.data;
        }
    }
    
    public class ConstantIterator implements Iterator<java.util.Map.Entry<Coordinate, Data>> {

        private Iterator<Coordinate> iterator;
        private ConstantMap<Data> map;

        public ConstantIterator(ConstantMap<Data> map) {
            iterator = getRegion().iterator();
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            return data!=null && iterator.hasNext();
        }

        @Override
        public Entry next() {
            return new Entry(map, iterator.next(),data);
        }
    }
    
    @Override
    public Iterator<java.util.Map.Entry<Coordinate, Data>> iterator() {
       return new ConstantIterator(this);
    }
    
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}