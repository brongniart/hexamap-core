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
package hexamap.regions;

import static java.lang.Math.abs;

import java.util.Iterator;
import java.util.Random;

import hexamap.coordinates.Coordinate;

/**
 *
 * @param <CoordinateImpl>
 */
public class Hexagon<CoordinateImpl extends Coordinate> extends IndexedRegion<CoordinateImpl> {

    private int range;

    public Hexagon(int range, CoordinateImpl center) {
        super(center);
        this.range = Math.abs(range);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object obj) {
        try {
            return center.distance((CoordinateImpl) obj) <= range;
        } catch (Exception e) {
            return false;
        }
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
                if (internal.hasNext()) {
                    return (CoordinateImpl) internal.next();
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
        return 1 + 6 * (range * (range + 1)) / 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object object) {
        try {
            return ((Hexagon<CoordinateImpl>) object).range == range
                    && ((Hexagon<CoordinateImpl>) object).center.equals(center);
        } catch (Exception e) {
            return false;
        }
    }

    public int getRange() {
        return range;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CoordinateImpl getRandom(Random random) {
        int x = random.nextInt(range * 2 + 1) - range;

        int bound = range - abs(x) - 1;
        int y = (x > 0) ? random.nextInt(range + bound) - range : random.nextInt(range + bound) - bound;
        return (CoordinateImpl) center.createCoordinate(center.getX() + x, center.getY() + y);
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {

        if (center.equals(coordinate)) {
            return size()-1;
        }
        
        int dist = center.distance(coordinate);
        @SuppressWarnings("unchecked")
        CoordinateImpl tmp = (CoordinateImpl) center.normalize(coordinate);
        int result = new Hexagon<Coordinate>(dist - 1, center).size();

        if (tmp.getZ() == -dist) {
            result = result + 6 * dist - tmp.getX();
        } else if (tmp.getX() == dist) {
            result = result + 5 * dist + tmp.getY();
        } else if (tmp.getY() == -dist) {
            result = result + 4 * dist - tmp.getZ();
        } else if (tmp.getZ() == dist) {
            result = result + 3 * dist + tmp.getX();
        } else if (tmp.getX() ==  -dist) {
            result = result + 2 * dist - tmp.getY();
        } else if (tmp.getY() == dist) {
            result = result + 1 * dist + tmp.getZ();
        } else {
            throw new RuntimeException();
        }

        return size() - result;
    }
}
