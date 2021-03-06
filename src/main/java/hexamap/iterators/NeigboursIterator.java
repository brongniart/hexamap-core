/*
 * Copyright (c) 2021, J?r?me Brongniart
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
package hexamap.iterators;

import java.util.Iterator;

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;

public class NeigboursIterator implements Iterator<Coordinate> {

    private final boolean all;
    private final Coordinate center;
    private int range;
    private int nbIters;
    private int nbDir;
    private Coordinate current;
    private Direction direction;

    private final Direction initDirection;

    public NeigboursIterator(Coordinate center, int range, boolean all, Direction initDirection) {

        this.all = all;
        this.range = Math.abs(range);
        this.center = center;
        this.initDirection = initDirection;
        current = null;
    }

    public NeigboursIterator(Coordinate center, int range, boolean all) {
        this(center, range, all, Direction.NORD);
    }

    @Override
    public boolean hasNext() {
        if (all) {
            return range > 1 || nbDir < 6 || nbIters < range;
        } else {
            return nbDir < 6 || nbIters < range;
        }
    }

    @Override
    public Coordinate next() {
        if (current == null) {
            current = center.add(initDirection, range);
            direction = initDirection.next(2);

            nbIters = 1;
            nbDir = 1;
        } else if (nbIters < range) {
            current.move(direction, 1);
            nbIters++;
        } else if (nbDir < 6) {
            current.move(direction, 1);
            direction = direction.next();

            nbIters = 1;
            nbDir++;
        } else if (all) {
            range--;
            nbIters = 1;
            nbDir = 1;
            direction = initDirection.next(2);
            current.move(direction, 1);
        } else {
            throw new RuntimeException("Calling next() while hasNext() is false");
        }

        return current;
    }
}