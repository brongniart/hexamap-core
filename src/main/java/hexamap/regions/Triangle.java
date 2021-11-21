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

import java.util.Iterator;
import java.util.Random;

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;

/**
 *
 * @param <CoordinateImpl>
 */
public class Triangle<CoordinateImpl extends Coordinate> extends IndexedRegion<CoordinateImpl> {

    Direction direction;
    private int length;

    public Triangle(Direction direction, int length, CoordinateImpl center) {
        super(center);
        if (length < 0) {
            this.direction = direction.next(3);
            this.length = -length;
        } else {
            this.direction = direction;
            this.length = length;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object obj) {
        try {
            CoordinateImpl c = ((CoordinateImpl) obj);

            int direction_z = -direction.x - direction.y;

            int factor_x = direction.x == 1 ? 1 : 0;
            int factor_y = direction.y == 1 ? 1 : 0;
            int factor_z = direction_z == 1 ? 1 : 0;

            return direction.x * Math.abs(c.getX() - center.getX()) <= factor_x * (length + 1)
                    && direction.y * Math.abs(c.getY() - center.getY()) <= factor_y * (length + 1)
                    && direction_z * Math.abs(c.getZ() - center.getZ()) <= factor_z * (length + 1);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Iterator<CoordinateImpl> iterator() {
        return new Iterator<CoordinateImpl>() {
            CoordinateImpl next = center;

            boolean hasNext = true;
            int iterLength = 0;
            int angle = 0;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            @SuppressWarnings("unchecked")
            public CoordinateImpl next() {
                if (hasNext) {
                    CoordinateImpl current = next;

                    if (angle == iterLength) {
                        if (iterLength == length) {
                            hasNext = false;
                        } else {
                            iterLength++;
                            angle = 0;
                            next = (CoordinateImpl) center.add(direction, iterLength);
                        }
                    } else {
                        angle++;
                        next = (CoordinateImpl) next.add(direction.next(2), 1);
                    }

                    return current;
                } else {
                    throw new RuntimeException("calling next() when hasNext() is false");
                }
            }
        };
    }

    @Override
    public int size() {
        return ((length + 2) * (length + 1)) / 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object object) {
        try {
            return ((Triangle<CoordinateImpl>) object).direction == direction
                    && ((Triangle<CoordinateImpl>) object).length == length
                    && ((Triangle<CoordinateImpl>) object).center == center;
        } catch (Exception e) {
            return false;
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CoordinateImpl getRandom(Random random) {
        int x = random.nextInt(length);
        int y = random.nextInt(length);

        return (CoordinateImpl) center.createCoordinate(center.getX() + direction.x * x,
                center.getY() + direction.y * y);
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {
        int dist = center.distance(coordinate);
        int result = ((dist + 1) * (dist)) / 2;
        
        Coordinate tmp = center.normalize(coordinate);
        switch (direction) {
        case NORD:
            return result + tmp.getX();
        case NORD_EAST:
            return result - tmp.getY();
        case SOUTH_EAST:
            return result + tmp.getZ();
        case SOUTH:
            return result - tmp.getX();
        case SOUTH_WEST:
            return result + tmp.getY();
        case NORD_WEST:
            return result - tmp.getZ();
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }
}
