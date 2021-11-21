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
package hexamap;

import javax.xml.crypto.Data;

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.streams.CoordinateStream;
import hexamap.maps.Map;
import hexamap.maps.streams.MapStream;
import hexamap.regions.Region;
import hexamap.regions.streams.RegionStream;

/**
 *
 */
public interface Hexamap<CoordinateImpl extends Coordinate> {

    public void addRegion(Region<CoordinateImpl> region);

    public boolean removeRegion(Region<CoordinateImpl> region);

    public void addMap(Region<CoordinateImpl> region, Map<CoordinateImpl, Data> map);

    public boolean removeMap(Region<CoordinateImpl> region, Map<CoordinateImpl, Data> map);

    public RegionStream<CoordinateImpl> regions();

    public MapStream<CoordinateImpl> maps();

    public MapStream<CoordinateImpl> maps(Class<?> klass);

    public MapStream<CoordinateImpl> maps(Region<CoordinateImpl> region);

    public MapStream<CoordinateImpl> maps(Region<CoordinateImpl> region, Class<?> klass);

    public CoordinateStream<CoordinateImpl> coordinates();

    public CoordinateStream<CoordinateImpl> coordinates(Region<CoordinateImpl> region);

    public CoordinateStream<CoordinateImpl> coordinates(Map<CoordinateImpl, Data> map);

    public CoordinateStream<CoordinateImpl> coordinates(Region<CoordinateImpl> region, Map<CoordinateImpl, Data> map, Class<?> klass);
}
