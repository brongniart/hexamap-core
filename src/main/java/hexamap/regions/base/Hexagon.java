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

import static java.lang.Math.abs;

import java.util.Iterator;
import java.util.Random;
import java.util.Spliterator;

import hexamap.coordinates.Coordinate;
import hexamap.regions.AbstractRegion;

/**
 *
 * @param <CoordinateImpl>
 */
public class Hexagon<CoordinateImpl extends Coordinate> extends BaseRegion<CoordinateImpl> {

    private int range;

    public Hexagon(int range, CoordinateImpl center) {
        super(center);
        this.range = Math.abs(range);
    }

    @Override
    public boolean contains(CoordinateImpl coordinate) {
        return getCenter().distance((Coordinate) coordinate) <= range;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<CoordinateImpl> iterator() {
        return new Iterator<CoordinateImpl>() {
            Iterator<CoordinateImpl> internal;
            boolean last = false;
            {
                internal = (Iterator<CoordinateImpl>) getCenter().getAllNeigbours(range).iterator();
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
                        return getCenter();
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
                    && ((Hexagon<CoordinateImpl>) object).getCenter().isEquals(getCenter());
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
    @SuppressWarnings("unchecked")
    public CoordinateImpl getRandom(Random random) {
        int x = random.nextInt(range * 2 + 1) - range;

        int bound = range - abs(x) - 1;
        int y = (x > 0) ? random.nextInt(range + bound) - range : random.nextInt(range + bound) - bound;
        return (CoordinateImpl) getCenter().createCoordinate(getCenter().getX() + x, getCenter().getY() + y);
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {
        int dist = getCenter().distance(coordinate);
        int result = 1 + 6 * (dist * (dist - 1)) / 2;
        
        int x = coordinate.getX() - getCenter().getX();
        int y = coordinate.getY() - getCenter().getY();
        int z = coordinate.getZ() - getCenter().getZ();
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

    @Override
    public AbstractRegion<CoordinateImpl> intersection(BaseRegion<CoordinateImpl> region) {
        return new AbstractRegion<CoordinateImpl>() {
            @Override
            public boolean contains(CoordinateImpl coordinate) {
                return false;
            }
            @Override
            public CoordinateImpl getRandom(Random random) {
                return null;
            }
            @Override
            public Iterator<CoordinateImpl> iterator() {
                return new Iterator<CoordinateImpl>() {

                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public CoordinateImpl next() {
                        return null;
                    }
                };
            }
            @Override
            public int size() {
                return 0;
            }
            @Override
            public Spliterator<CoordinateImpl> spliterator() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }
}
