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
import java.util.function.BiPredicate;
import java.util.function.Function;

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;

/**
 *
 * @param <CoordinateImpl>
 */
public class Diamond<CoordinateImpl extends Coordinate> extends IndexedRegion<CoordinateImpl> {

    private BiPredicate<Coordinate, Integer> testContains;
    private Function<Coordinate, Integer> coordinateX;
    private Function<Coordinate, Integer> coordinateY;
    private Direction direction;
    private int length;

    public Diamond(Direction direction, int length, CoordinateImpl center) {
        super(center);
        if (length < 0) {
            this.direction = direction.next(3);
            this.length = -length;
        } else {
            this.direction = direction;
            this.length = length;
        }
        testContains = Direction.getContainTest(direction, center)
                .and(Direction.getContainTest(direction.previous(), center));
    }

    @Override
    public void setCenter(CoordinateImpl center) {
        super.setCenter(center);
        testContains = Direction.getContainTest(direction, center)
                .and(Direction.getContainTest(direction.previous(), center));
    }
    
    @Override
    public boolean contains(CoordinateImpl coordinate) {
        return testContains.test(coordinate, length);
    }

    @Override
    public Iterator<CoordinateImpl> iterator() {
        return new Iterator<CoordinateImpl>() {
            CoordinateImpl next = getCenter();

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
                            next = (CoordinateImpl) getCenter().add(direction, iterLength);
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
            return ((Diamond<CoordinateImpl>) object).direction == direction
                    && ((Diamond<CoordinateImpl>) object).length == length
                    && ((Diamond<CoordinateImpl>) object).getCenter() == getCenter();
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
        int y = 1 + random.nextInt(length - x);

        switch (direction) {
        case NORD:
            return (CoordinateImpl) getCenter().createCoordinate(getCenter().getX() + x, getCenter().getY() + y);
        case NORD_EAST:
            return (CoordinateImpl) getCenter().createCoordinateYZ(getCenter().getY() - x, getCenter().getZ() - y);
        case SOUTH_EAST:
            return (CoordinateImpl) getCenter().createCoordinateXZ(getCenter().getX() + x, getCenter().getZ() + y);
        case SOUTH:
            return (CoordinateImpl) getCenter().createCoordinate(getCenter().getX() - x, getCenter().getY() - y);
        case SOUTH_WEST:
            return (CoordinateImpl) getCenter().createCoordinateYZ(getCenter().getY() + x, getCenter().getZ() + y);
        case NORD_WEST:
            return (CoordinateImpl) getCenter().createCoordinateXZ(getCenter().getX() - x, getCenter().getZ() - y);
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {
        int a = coordinateY.apply(coordinate) - coordinateY.apply(getCenter());
        return ((a + 1) * a) / 2 + (coordinateX.apply(coordinate) - coordinateX.apply(getCenter()));

    }

    @Override
    public CoordinateImpl getCoordinate(int index) {
        return null;
    }

    public String toString() {
        return "[" + this.getClass() + ": " + direction + "," + getCenter() + "]";
    }
}
