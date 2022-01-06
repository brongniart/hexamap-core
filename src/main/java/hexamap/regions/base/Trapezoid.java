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
import java.util.function.BiPredicate;
import java.util.function.Function;

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;
import hexamap.coordinates.Segment;

/**
 *
 * @param <CoordinateImpl>
 */
public class Trapezoid<CoordinateImpl extends Coordinate> extends Triangle<CoordinateImpl> {

    private BiPredicate<Coordinate, Integer> testContains;
    private Function<Coordinate, Integer> coordinateX;
    private Function<Coordinate, Integer> coordinateY;
    private Direction direction;
    private int length;

    public Trapezoid(Direction direction, int length, CoordinateImpl center) {
        super(direction,length,center);
        
        if (length < 0) {
            this.direction = direction.next(3);
            this.length = -length;
        } else {
            this.direction = direction;
            this.length = length;
        }
        testContains = Segment.getContainTest(direction, center)
                .and(Segment.getContainTest(direction.previous(), center));
    }
    
    @Override
    public boolean contains(CoordinateImpl coordinate) {
        return testContains.test(coordinate, length);
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
            return ((Trapezoid<CoordinateImpl>) object).direction == direction
                    && ((Trapezoid<CoordinateImpl>) object).length == length
                    && ((Trapezoid<CoordinateImpl>) object).center == center;
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public int getIndex(CoordinateImpl coordinate) {
        int a = coordinateY.apply(coordinate) - coordinateY.apply(center);
        return ((a + 1) * a) / 2 + (coordinateX.apply(coordinate) - coordinateX.apply(center));

    }

    @Override
    public CoordinateImpl getCoordinate(int index) {
        return null;
    }

    public String toString() {
        return "[" + this.getClass() + ": " + direction + "," + center + "]";
    }
}
