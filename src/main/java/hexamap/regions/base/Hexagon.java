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
import java.util.Random;

import hexamap.coordinates.Axial;
import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;

public class Hexagon extends BasePolygon {

    public final int range;

    public Hexagon(int range, Coordinate center) {
        super(center);
        this.range = Math.abs(range);
    }

    @Override
    public boolean contains(Coordinate coordinate) {
        assert coordinate != null;
        return center.distance((Coordinate) coordinate) <= range;
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new Iterator<Coordinate>() {
            Iterator<Coordinate> internal;
            boolean last = false;
            {
                internal = (Iterator<Coordinate>) center.getAllNeigbours(range).iterator();
            }

            @Override
            public boolean hasNext() {
                return !last;
            }

            @Override
            public Coordinate next() {
                assert hasNext();

                if (internal.hasNext()) {
                    return internal.next();
                } else {
                    if (!last) {
                        last = true;
                        return center;
                    } else {
                        throw new RuntimeException();
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
    public boolean equals(Object object) {
        try {
            return ((Hexagon) object).range == range && ((Hexagon) object).center.isEquals(center);
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public int getIndex(Coordinate coordinate) throws OutOfRegion {
        int dist = center.distance(coordinate);
        if (dist > range) {
            throw new OutOfRegion(coordinate, this);
        }
        int result = 1 + 3 * (dist * (dist - 1));

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
        } else if (x == -dist) {
            result = result + 2 * dist - y;
        } else if (y == dist) {
            result = result + 1 * dist + z;
        } else {
            throw new RuntimeException();
        }
        return size() - result;
    }

    @Override
    public Coordinate getRandom(Random rand) {
        int x = rand.nextInt(range * 2 + 1) - range;

        int bound = range - Math.abs(x) - 1;
        int y = (x > 0) ? rand.nextInt(range + bound) - range : rand.nextInt(range + bound) - bound;
        return new Axial(center.getX() + x, center.getY() + y);
    }

    @Override
    public Coordinate getCoordinate(int index) throws OutOfRegion {
        if (index < 0 || index > size()) {
            throw new OutOfRegion(index, this);
        }
        
        int distance;
        if (index < size() / 2) {
            distance = range;
            int sum = 6 * distance;
            while (index >= sum && distance > 0) {
                distance--;
                sum += 6 * distance;
            }
            index -= (sum - 6 * distance);
        } else {
            distance = 0;
            int sum = size();
            while (index < sum && distance < size() / 2) {
                distance++;
                sum -= 6 * distance;
            }
            index -= sum;
        }

        Coordinate result = center.add(Direction.NORD, distance);
        Direction direction = Direction.NORD.next(2);
        if (index > distance) {
            result.move(direction, distance);
            direction = direction.next();
            index -= distance;

            while (index > distance && distance > 0) {
                result.move(direction, distance);
                direction = direction.next();
                index -= distance;
            }
        }
        result.move(direction, index);
        return result;
    }

    @Override
    public Iterable<Coordinate> vertices() {
        return null;
    }

    @Override
    public Iterable<Segment> edges() {
        return null;
    }
}
