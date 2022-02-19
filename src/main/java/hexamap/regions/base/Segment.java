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

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;
import hexamap.regions.Polygon;

/**
 *
 */
public class Segment implements Polygon {

    private Direction direction;
    private Coordinate start;
    private int length;

    public Segment(Coordinate start, int length, Direction direction) {
        this.start = start;
        if (length < 0) {
            this.length = -length;
            this.direction = direction.flip();
        } else {
            this.length = length;
            this.direction = direction;
        }
    }

    public Coordinate crossing(Segment segment) {
        return null;
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new Iterator<Coordinate>() {

            private Coordinate current = null;
            private int iter = 0;

            @Override
            public boolean hasNext() {
                return iter < length;
            }

            @Override
            public Coordinate next() {
                assert hasNext();
                if (current == null) {
                    current = start.copy();
                } else {
                    current.move(direction, 1);
                }
                iter++;
                return current;
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return length + 1;
    }

    @Override
    public boolean contains(Coordinate coordinate) {
        return direction.constantCoordinateValue.apply(coordinate) == direction.constantCoordinateValue.apply(start)
                && direction.variableCoordinateValue.apply(coordinate) >= direction.variableCoordinateValue.apply(start)
                && direction.variableCoordinateValue.apply(coordinate) <= direction.variableCoordinateValue.apply(start)
                        + length;
    }

    @Override
    public Coordinate getRandom(Random random) {
        return start.add(direction, random.nextInt(length));
    }

    @Override
    public int getIndex(Coordinate coordinate) throws OutOfRegion {
        if (!contains(coordinate)) {
            throw new OutOfRegion(coordinate, this);
        }
        return direction.constantCoordinateValue.apply(coordinate) - direction.constantCoordinateValue.apply(start);
    }

    @Override
    public Coordinate getCoordinate(int index) throws OutOfRegion {
        if (index < 0 || index >= length) {
            throw new OutOfRegion(index, this);
        }
        return start.add(direction, index);
    }

    @Override
    public Iterable<Coordinate> vertices() {
        return () -> new Iterator<Coordinate>() {

            private boolean hasNext = true;
            private Coordinate current = null;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Coordinate next() {
                if (current == null) {
                    current = start;
                } else {
                    current = start.add(direction, length);
                    hasNext = false;
                }
                return current;
            }
        };
    }

    private class SingleSegmentIterator implements Iterator<Segment> {

        private final Segment s;
        private boolean nextCalled = false;

        public SingleSegmentIterator(Segment s) {
            this.s = s;
        }

        @Override
        public boolean hasNext() {
            return !nextCalled;
        }

        @Override
        public Segment next() {
            nextCalled = true;
            return s;
        }
    }

    @Override
    public Iterable<Segment> edges() {
        return () -> new SingleSegmentIterator(this);
    }
}
