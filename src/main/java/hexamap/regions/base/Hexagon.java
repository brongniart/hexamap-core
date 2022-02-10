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
package hexamap.regions.base;

import java.util.Iterator;

import hexamap.coordinates.Coordinate;

/**
 *
 * @param <CoordinateImpl>
 */
public class Hexagon<CoordinateImpl extends Coordinate> extends BasePolygon<CoordinateImpl> {

    private int range;

    public Hexagon(int range, CoordinateImpl center) {
        super(center);
        this.range = Math.abs(range);
    }

    @Override
    public boolean contains(CoordinateImpl coordinate) {
        return center.distance((Coordinate) coordinate) <= range;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<CoordinateImpl> iterator() {
        return new Iterator<CoordinateImpl>() {
            Iterator<CoordinateImpl> internal;
            boolean last = false;
            {
                internal = (Iterator<CoordinateImpl>) center.getAllNeigbours(range).iterator();
            }

            @Override
            public boolean hasNext() {
                return !last;
            }

            @Override
            public CoordinateImpl next() {
                assert hasNext();
                
                if (internal.hasNext()) {
                    return internal.next();
                } else {
                    if (!last) {
                        last = true;
                        return center;
                    } else {
                        throw new RuntimeException("calling next() when hasNext() is false");
                    }
                }
            }
        };
    }

    @Override
    public int size() {
        return 1 + 3 * (range * (range + 1));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object object) {
        try {
            return ((Hexagon<CoordinateImpl>) object).range == range
                    && ((Hexagon<CoordinateImpl>) object).center.isEquals(center);
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int getRange() {
        return range;
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {
        int dist = center.distance(coordinate);
        int result = 1 + 6 * (dist * (dist - 1)) / 2;
        
        int x = coordinate.getX() - center.getX();
        int y = coordinate.getY() - center.getY();
        int z = coordinate.getZ() - center.getZ();
        if (z == -dist) {
            result = result + 6 * dist - x;
        } else if (x == dist) {
            result = result + 5 * dist + y;
        } else if (y == -dist) {
            result = result + 4 * dist - z;
        } else if (z == dist) {
            result = result + 3 * dist + x;
        } else if (x ==  -dist) {
            result = result + 2 * dist - y;
        } else if (y == dist) {
            result = result + 1 * dist + z;
        } else {
            throw new RuntimeException();
        }
        return size() - result;
    }

    @Override
    public CoordinateImpl getCoordinate(int index) {
        return null;
    }
}
