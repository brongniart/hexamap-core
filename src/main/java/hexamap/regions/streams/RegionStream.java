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
package hexamap.regions.streams;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

import hexamap.coordinates.Coordinate;

/**
 *
 */
public class RegionStream<CoordinateImpl extends Coordinate> implements BaseStream<CoordinateImpl,RegionStream<CoordinateImpl>> {
    
    @Override
    public Iterator<CoordinateImpl> iterator() {
        return null;
    }

    @Override
    public Spliterator<CoordinateImpl> spliterator() {
        return null;
    }

    @Override
    public boolean isParallel() {
        return false;
    }

    @Override
    public RegionStream<CoordinateImpl> sequential() {
        return null;
    }

    @Override
    public RegionStream<CoordinateImpl> parallel() {
        return null;
    }

    @Override
    public RegionStream<CoordinateImpl> unordered() {
        return null;
    }

    @Override
    public RegionStream<CoordinateImpl> onClose(Runnable closeHandler) {
        return null;
    }

    @Override
    public void close() {
    }

    public RegionStream<CoordinateImpl> touch(RegionStream<CoordinateImpl> region) {
        return null;
    }
}
