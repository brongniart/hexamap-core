/*
 * Copyright (c) 2021, J�r�me Brongniart
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

import hexamap.coordinates.Coordinate;
import hexamap.regions.base.Segment;

/**
 *
 */
public interface Polygon extends IndexedRegion {

    default public Iterator<Coordinate> vertices(boolean outside) {
        if (outside) {
            return vertices();
        } else {
            return new Iterator<Coordinate>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Coordinate next() {
                    return null;
                }
            };
        }
    }

    public Iterator<Coordinate> vertices();

    default public Iterator<Segment> edges(boolean outside) {
        if (outside) {
            return edges();
        } else {
            return new Iterator<Segment>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Segment next() {
                    return null;
                }
            };
        }
    }

    public Iterator<Segment> edges();
}
