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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @param <Data> some stuffs
 */
public interface Map<CoordinateImpl extends Coordinate, Data>
        extends java.util.Map<CoordinateImpl, Data>, Iterable<Entry<CoordinateImpl, Data>> {

    public class Entry<CoordinateImpl extends Coordinate, Data> implements java.util.Map.Entry<CoordinateImpl, Data> {

        private CoordinateImpl coordinate;
        private Data data;

        public Entry(CoordinateImpl coordinate, Data data) {
            this.coordinate = coordinate;
            this.data = data;
        }

        @Override
        public CoordinateImpl getKey() {
            return coordinate;
        }

        @Override
        public Data getValue() {
            return data;
        }

        @Override
        public Data setValue(Data value) {
            Data tmp = data;
            data = value;
            return tmp;
        }
    }

    @Override
    public Iterator<java.util.Map.Entry<CoordinateImpl, Data>> iterator();

    @Override
    default public boolean containsKey(Object arg0) {
        throw new ClassCastException();
    }

    public abstract boolean containsKey(CoordinateImpl coordinate);

    @Override
    default public Data get(Object arg0) {
        throw new ClassCastException();
    }

    public abstract Data get(CoordinateImpl arg0);

    @Override
    default public Data remove(Object arg0) {
        throw new ClassCastException();
    }

    default public Data remove(CoordinateImpl arg0) {
        return put(arg0, null);
    }

    @Override
    public abstract Data put(CoordinateImpl c, Data data);

    @Override
    public void putAll(java.util.Map<? extends CoordinateImpl, ? extends Data> m);

    @Override
    public abstract void clear();

    // Unsupported methods from Map:
    @Override
    default public Set<CoordinateImpl> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    default public Collection<Data> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    default public Set<java.util.Map.Entry<CoordinateImpl, Data>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    default public boolean containsValue(Object arg0) {
        throw new UnsupportedOperationException();
    }
}