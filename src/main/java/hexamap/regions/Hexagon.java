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

import hexamap.coordinates.Coordinate;
import static java.lang.Math.abs;
import java.util.Iterator;

/**
 *
 * @param <CoordinateImpl>
 */
public class Hexagon<CoordinateImpl extends Coordinate> extends Region<CoordinateImpl> {

    private int range;

    public Hexagon(int range, CoordinateImpl center) {
        super(center);
        this.range = Math.abs(range);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object obj) {
        try {
            return center.distance((CoordinateImpl) obj) <= range;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<CoordinateImpl> iterator() {
        return new Iterator<CoordinateImpl>() {
            Iterator<CoordinateImpl> internal;
            boolean last = false;

            {
                if (range > 0) {
                    this.internal = (Iterator<CoordinateImpl>) center.getAllNeigbours(range).iterator();
                }
            }

            @Override
            public boolean hasNext() {
                return !last;
            }

            @Override
            public CoordinateImpl next() {
                if (internal.hasNext()) {
                    return (CoordinateImpl) internal.next();
                } else {
                    if (!last) {
                        last = true;
                        return center;
                    } else {
                        throw new RuntimeException("calling next() when hasNext() is false");
                    }
                }
            }
        };
    }

    @Override
    public int size() {
        return 1 + 6 * (range * (range + 1)) / 2;
    }

    @Override
    public boolean equals(Object other) {
        try {
            @SuppressWarnings("unchecked")
            Hexagon<CoordinateImpl> region = (Hexagon<CoordinateImpl>) other;
            return region instanceof Hexagon && region.range == range && region.center.equals(center);
        } catch (Exception e) {
            return false;
        }
    }

    public int getRange() {
        return range;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CoordinateImpl getRandom() {
        int x = random.nextInt(range * 2 + 1) - range;

        int bound = range - abs(x) - 1;
        int y = (x > 0) ? random.nextInt(range + bound) - range : random.nextInt(range + bound) - bound;
        return (CoordinateImpl) center.createCoordinate(x, y);
    }
}
