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

/**
 *
 * @param <CoordinateImpl>
 */
public class Triangle extends BasePolygon {

    private BiPredicate<Coordinate, Integer> testContains;
    private Function<Coordinate, Integer> coordinateStart;
    private Function<Coordinate, Integer> coordinateEnd;
    
    public final Direction direction;
    public final int length;

    public Triangle(Direction direction, int length, Coordinate center) {
        super(center);

        if (length < 0) {
            this.direction = direction.flip();
            this.length = -length;
        } else {
            this.direction = direction;
            this.length = length;
        }
        // TODO: make center unique
        
        testContains = BasePolygon.getContainTest(direction, center)
                .and(BasePolygon.getContainTest(direction.next(), center));
        
        coordinateStart = direction.constantCoordinate;
        coordinateEnd = direction.previous().constantCoordinate;
    }

    @Override
    public boolean contains(Coordinate coordinate) {
        return testContains.test(coordinate, length);
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new Iterator<Coordinate>() {
            Coordinate current = null;

            boolean hasNext = true;
            int iterLength = 0;
            int angle = 0;
            Direction dir_angle = direction.next(2);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Coordinate next() {
                assert hasNext();

                if (current == null) {
                    current = (Coordinate) center.add(Direction.NORD, 0);
                } else if (angle == iterLength) {

                    current.move(dir_angle.flip(), iterLength);
                    current.move(direction, 1);

                    iterLength++;
                    angle = 0;
                } else {
                    angle++;
                    current.move(dir_angle, 1);
                }

                if (iterLength == length && angle == iterLength) {
                    hasNext = false;
                }

                return current;
            }
        };
    }

    @Override
    public int size() {
        return ((length + 2) * (length + 1)) / 2;
    }

    @Override
    public boolean equals(Object object) {
        try {
            return ((Triangle) object).direction == direction && ((Triangle) object).length == length
                    && ((Triangle) object).center == center;
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
    public int getIndex(Coordinate coordinate) {
        int a = coordinateEnd.apply(coordinate) - coordinateEnd.apply(center);
        return ((a + 1) * a) / 2 + (coordinateStart.apply(coordinate) - coordinateStart.apply(center));

    }

    @Override
    public Coordinate getCoordinate(int index) {
        return null;
    }

    public String toString() {
        return "[" + this.getClass() + ": " + direction + "," + center + "]";
    }

    @Override
    public Coordinate[] vertices() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Segment[] edges() {
        // TODO Auto-generated method stub
        return null;
    }
}
