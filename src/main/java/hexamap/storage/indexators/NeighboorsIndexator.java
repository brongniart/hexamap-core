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
package hexamap.storage.indexators;

import hexamap.coordinates.Coordinate;
import hexamap.regions.Hexagon;
import hexamap.regions.Region;
import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 *
 */
public class NeighboorsIndexator implements Indexator {

    private final Hexagon region;

    public NeighboorsIndexator(Hexagon region) throws Exception {
        this.region = region;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public int index(Coordinate coordinate) {
        assert region.contains(coordinate);

        if (coordinate.getClass() == IndexedCoordinate.class && ((IndexedCoordinate) coordinate).index >= 0) {
            return ((IndexedCoordinate) coordinate).index;
        }

        int dist = max(abs(coordinate.getX()), max(abs(coordinate.getY()), abs(coordinate.getZ())));

        if (dist == 0) {
            return 0;
        } else {
            int result = 1;
            int mult = 6;
            for (int i = 1; i < dist; i++) {
                result += mult;
                mult += 6;
            }

            if (coordinate.getZ() == -dist) {
                assert coordinate.getX() >= 0;
                result = result + coordinate.getX();
            } else if (coordinate.getX() == dist) {
                assert coordinate.getY() <= 0;
                result = result + 1 * dist - coordinate.getY();
            } else if (coordinate.getY() == -dist) {
                assert coordinate.getX() >= 0;
                result = result + 2 * dist + coordinate.getZ();
            } else if (coordinate.getZ() == dist) {
                assert coordinate.getX() <= 0;
                result = result + 3 * dist - coordinate.getX();
            } else if (coordinate.getX() == -dist) {
                assert coordinate.getY() >= 0;
                result = result + 4 * dist + coordinate.getY();
            } else if (coordinate.getY() == dist) {
                assert coordinate.getX() <= 0;
                result = result + 5 * dist - coordinate.getZ();
            } else {
                assert false;
            }
            assert result > 0;
            if (coordinate.getClass() == IndexedCoordinate.class) {
                ((IndexedCoordinate) coordinate).index = result;
            }
            return result;
        }
    }

    @Override
    public Coordinate deindex(int index) {
        assert 0 <= index;
        assert index < region.size();
        return null;
    }
}
