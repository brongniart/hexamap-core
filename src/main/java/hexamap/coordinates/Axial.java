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
public class Axial extends AbstractCoordinate
{
    private int x = 0;
    private int y = 0;
    
    public Axial()
    {}
    
    public Axial(Coordinate c)
    {
        x = c.getX();
        y = c.getY();
    }
    
    public Axial(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }
    
    @Override
    public int getZ() {
        return - x - y;
    }
    
    
    @Override
    public void move(Coordinate coordinate) {
        setX(getX()+coordinate.getX());
        setY(getY()+coordinate.getX());
    }

    @Override
    public void move(Direction direction, int range) {
        setX(range*(getX()+direction.x));
        setY(range*(getY()+direction.y));
    }
    
    protected void setX(int x) {
        this.x = x;
    }
    
    protected void setY(int y) {
        this.y = y;
    }
    
    @Override
    public Coordinate getNext(Direction where) {
         return new Axial(x+where.x,y+where.y);
    }

    @Override
    public Coordinate createCoordinate(int x, int y) {
        return new Axial(x,y);
    }

	@Override
	public Coordinate rotate(Direction direction) {
		if (direction==Direction.NORD_EAST) {
			x=getY();
			y=getX();
		} else if (direction==Direction.SOUTH_EAST) {
			x=getY();
			y=getZ();
		} else if (direction==Direction.SOUTH) {
			y=getZ();
		} else if (direction==Direction.SOUTH_WEST) {
			y=getX();
			x=getZ();
		} else if (direction==Direction.NORD_WEST) {
			x=getZ();
		} else {
			// if (direction==Direction.NORD) do nothing
			assert direction==Direction.NORD;
		}
		return this;
	}
}
