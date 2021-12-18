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
package hexamap.regions.base;

import java.util.Spliterator;
import java.util.function.Consumer;

import hexamap.coordinates.Coordinate;
import hexamap.regions.AbstractRegion;

/**
 *
 * @param <CoordinateImpl>
 */
public abstract class BaseRegion<CoordinateImpl extends Coordinate> extends AbstractRegion<CoordinateImpl> {

    public BaseRegion(CoordinateImpl center) {
        super(center);
    }
    
    public abstract int getIndex(CoordinateImpl coordinate);
    public abstract CoordinateImpl getCoordinate(int index);
    
    public abstract AbstractRegion<CoordinateImpl> intersection(BaseRegion<CoordinateImpl> region);
        
    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Spliterator<CoordinateImpl> spliterator() {
        return new Spliterator<CoordinateImpl>() {
            
            @Override
            public boolean tryAdvance(Consumer<? super CoordinateImpl> action) {
                return false;
            }

            @Override
            public Spliterator<CoordinateImpl> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };
    }
}
