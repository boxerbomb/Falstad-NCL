/*    
    Copyright (C) Paul Falstad and Iain Sharp
    
    This file is part of CircuitJS1.

    CircuitJS1 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    CircuitJS1 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CircuitJS1.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lushprojects.circuitjs1.client;

class nclOutputDisplayElm extends ChipElm {
    int bitCount;
    
    public nclOutputDisplayElm(int xx, int yy) {
	super(xx, yy);
	bitCount = 2;
	setupPins();
    }
    public nclOutputDisplayElm(int xa, int ya, int xb, int yb, int f,
		    StringTokenizer st) {
	super(xa, ya, xb, yb, f, st);
	bitCount = 2;
	try {
	    bitCount = Integer.parseInt(st.nextToken());
	} catch (Exception e) {}
	setupPins();
    }
    String getChipName() { return "NCL Display"; }
    
    void draw(Graphics g) {
        drawChip(g);
        int xl = x+cspc + flippedSizeX*cspc;
        int yl = y-cspc + flippedSizeY*cspc;
	if (isFlippedXY())
	    yl += ((flags & FLAG_FLIP_Y) != 0) ? -cspc/2 : cspc/2;
        g.save();
        g.setFont(new Font("SansSerif", 0, 15*csize));
        g.setColor(whiteColor);
        g.context.setTextBaseline("middle");
        
        String str = "";
        if(pins[0].value==false && pins[1].value==false){
          str = "N";  
        }else if(pins[0].value==true && pins[1].value==true){
            str = "X"; 
        }else if(pins[0].value==true && pins[1].value==false){
            str = "1";
        }else if(pins[0].value==false && pins[1].value==true){
            str = "0";
        }
        
        /*
        for (i = 0; i != bitCount; i++)
            if (pins[i].value)
        	value |= 1<<i;
        String str = String.valueOf(value);
        */
        int w=(int)g.context.measureText(str).getWidth();
        g.drawString(str, xl+5*csize-w/2, yl);
        g.restore();
    }
    
    String dump() { return super.dump() + " " + bitCount; }
    
    void setupPins() {
	sizeX = 3;
	sizeY = bitCount;
	pins = new Pin[bitCount];
	int i;
	pins[0] = new Pin(bitCount-1-0, SIDE_W, "D1");
	pins[1] = new Pin(bitCount-1-1, SIDE_W, "D0");
	allocNodes();
    }
    int getPostCount() { return bitCount; }
    int getDumpType() { return 450; }
    int getVoltageSourceCount() { return 0; }
    public EditInfo getChipEditInfo(int n) {
        if (n == 0)
            return new EditInfo("# of Bits", bitCount, 1, 8).
                setDimensionless();
        return super.getChipEditInfo(n);
    }
    public void setChipEditValue(int n, EditInfo ei) {
        if (n == 0 && ei.value >= 1 && ei.value <= 16) {
            bitCount = (int) ei.value;
            setupPins();
            setPoints();
            return;
        }
        super.setChipEditValue(n, ei);
    }

}
    
