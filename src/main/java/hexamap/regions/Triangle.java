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

    private Direction direction;
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
    public boolean contains(Object obj) {
        try {
            Coordinate c = center.normalize((Coordinate) obj);
            
            switch (direction) {
            case NORD:
                return c.getX() <= length && c.getY() <= length;
            case NORD_EAST:
                return c.getY() >= -length && c.getZ() >= -length;
            case SOUTH_EAST:
                return c.getX() <= length && c.getZ() <= length;
            case SOUTH:
                return c.getX() >= -length && c.getY() >= -length;
            case SOUTH_WEST:
                return c.getY() <= length && c.getZ() <= length;
            case NORD_WEST:
                return c.getX() >= -length && c.getZ() <= length;
            default:
                throw new RuntimeException("Unexpected direction");
            }
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
            Direction dir_angle = direction.next(2);
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
                        next = (CoordinateImpl) next.add(dir_angle, 1);
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
    public Coordinate getRandom(Random random) {
        int x = random.nextInt(length);
        int y = 1+random.nextInt(length-x);
        
        switch (direction) {
        case NORD:
            return center.createCoordinate(center.getX() + x, center.getY() +  y);
        case NORD_EAST:
            return center.createCoordinateYZ(center.getY() - x, center.getZ() -  y);
        case SOUTH_EAST:
            return center.createCoordinateXZ(center.getX() + x, center.getZ() + y);
        case SOUTH:
            return center.createCoordinate(center.getX() - x, center.getY() - y);
        case SOUTH_WEST:
            return center.createCoordinateYZ(center.getY() + x, center.getZ() + y);
        case NORD_WEST:
            return center.createCoordinateXZ(center.getX() - x, center.getZ() - y);
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {
        Coordinate tmp = center.normalize(coordinate);
        switch (direction) {
        case NORD:
            return ((-tmp.getZ() + 1) * (-tmp.getZ())) / 2 + tmp.getX();
        case NORD_EAST:
            return ((tmp.getX() + 1) * (tmp.getX())) / 2 - tmp.getY();
        case SOUTH_EAST:
            return ((-tmp.getY() + 1) * (-tmp.getY())) / 2 + tmp.getZ();
        case SOUTH:
            return ((tmp.getZ() + 1) * (tmp.getZ())) / 2 - tmp.getX();
        case SOUTH_WEST:
            return ((-tmp.getX() + 1) * (-tmp.getX())) / 2 + tmp.getY();
        case NORD_WEST:
            return ((tmp.getY() + 1) * (tmp.getY())) / 2 - tmp.getZ();
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }
}
