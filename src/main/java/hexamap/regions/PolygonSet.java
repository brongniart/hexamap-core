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
package hexamap.regions;

import java.util.Iterator;

import hexamap.coordinates.Coordinate;

/**
 *
 * @param <CoordinateImpl>
 */
public class PolygonSet implements IndexedRegion {

    private GenericPolygon[] list;
    private int size = -1;

    @Override
    public int getIndex(Coordinate coordinate) throws OutOfRegion {
        if (list == null) {
            throw new OutOfRegion(coordinate, this);
        }

        int size = 0;
        for (GenericPolygon base : list) {
            if (base.contains(coordinate)) {
                return size + base.getIndex(coordinate);
            } else {
                size += base.size();
            }
        }
        throw new OutOfRegion(coordinate, this);
    }

    @Override
    public Coordinate getCoordinate(int index) throws OutOfRegion {
        if (list == null) {
            throw new OutOfRegion(index, this);
        }

        for (GenericPolygon base : list) {
            if (index < base.size()) {
                return base.getCoordinate(index);
            } else {
                index -= base.size();
            }
        }
        throw new OutOfRegion(index, this);
    }

    @Override
    public boolean isEmpty() {
        return list == null;
    }

    @Override
    public int size() {
        if (list == null) {
            return 0;
        }
        if (size == -1) {
            size = 0;
            for (GenericPolygon base : list) {
                size += base.size();
            }
        }
        return size;
    }

    @Override
    public boolean contains(Coordinate coordinate) {
        if (list == null) {
            return false;
        }

        for (GenericPolygon base : list) {
            if (base.contains(coordinate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new Iterator<Coordinate>() {

            Iterator<Coordinate> internal = (list == null) ? null : list[0].iterator();
            int index = 0;
            Coordinate current;

            @Override
            public boolean hasNext() {
                return internal != null && internal.hasNext();
            }

            @Override
            public Coordinate next() {
                assert internal.hasNext();

                current = internal.next();
                if (!internal.hasNext()) {
                    index++;
                    if (list[index] == null) {
                        internal = null;
                    } else {
                        internal = list[index].iterator();
                    }
                }
                return current;
            }
        };
    }
}
