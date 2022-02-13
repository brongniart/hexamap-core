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
import hexamap.regions.Region;

/**
 *
 */
public class Segment implements Region {

    private Direction direction;
    private Coordinate start;
    private int length;

    public Segment(Coordinate start, int length, Direction direction) {
        if (length < 0) {
            this.start = start;
            this.length = -length;
            this.direction = direction.flip();
        } else {
            this.start = start;
            this.length = length;
            this.direction = direction;
        }
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new Iterator<Coordinate>() {

            private Coordinate current = null;
            private Coordinate last = start.add(direction, length);

            @Override
            public boolean hasNext() {
                return current == null || current != last;
            }

            @Override
            public Coordinate next() {
                assert hasNext();
                if (current == null) {
                    current = start.copy();
                } else {
                    current.move(direction, 1);
                }
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
        return direction.constantCoordinate.apply(coordinate) == direction.constantCoordinate.apply(start)
                && direction.variableCoordinate.apply(coordinate) >= direction.variableCoordinate.apply(start)
                && direction.variableCoordinate.apply(coordinate) <= direction.variableCoordinate.apply(start)+length;
    }

    @Override
    public Coordinate getRandom(Random random) {
        return start.add(direction, random.nextInt(length));
    }
}
