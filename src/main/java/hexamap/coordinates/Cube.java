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
package hexamap.coordinates;

/**
 *
 */
public class Cube extends Axial {
    
    protected int z = 0;

    public Cube() {
    }

    public Cube(int x, int y) {
        super(x, y);
        this.z = super.getZ();
    }

    public Cube(Coordinate c) {
        super(c);
        this.z = c.getZ();
    }

    @Override
    public int getZ() {
        return z;
    }

    public Cube createCoordinate(int x, int y) {
        return new Cube(super.createCoordinate(x, y));
    }

    public Axial createCoordinateXZ(int x, int z) {
        return new Cube(super.createCoordinateXZ(x, z));
    }

    public Axial createCoordinateYZ(int y, int z) {
        return new Cube(super.createCoordinateYZ(y, z));
    }

    public Cube add(Direction direction, int range) {
        return new Cube(x + range * direction.x, y + range * direction.y);
    }

    public String toString() {
        return "{" + x + "," + y + "," + z + "}";
    }

    @Override
    public boolean equals(Object obj) {
       return super.equals(obj);
    }
}
