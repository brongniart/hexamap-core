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
public class Axial extends Coordinate {
    
    protected int x = 0;
    protected int y = 0;

    public Axial() {
    }
    
    public Axial(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Axial(Coordinate c) {
        this.x = c.getX();
        this.y = c.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return -x - y;
    }

    private static final Axial zero = new Axial();
    public Axial getZero() {
        return zero;
    }
    
    public Axial createCoordinate(int x, int y) {
        return new Axial(x,y);
    }

    public Axial createCoordinateXZ(int x, int z) {
        return new Axial(x,-x-z);
    }

    public Axial createCoordinateYZ(int y, int z) {
        return new Axial(-y-z,y);
    }
    
    public void move(Direction direction, int range) {
        x = x + range * direction.x;
        y = y + range * direction.y;
    }
    
    public Axial add(Direction direction, int range) {
        return createCoordinate(x + range*direction.x, y + range*direction.y);
    }

    @Override
    public String toString() {
        return "{"+x+","+y+"}";
    }
}
