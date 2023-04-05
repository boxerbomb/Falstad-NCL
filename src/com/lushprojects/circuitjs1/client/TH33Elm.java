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

    class TH33Elm extends GateElm {
	public TH33Elm(int xx, int yy) {
	    super(xx, yy); 
	    
	}
	public TH33Elm(int xa, int ya, int xb, int yb, int f,
			  StringTokenizer st) {
	    super(xa, ya, xb, yb, f, st);
	}
	
	String getGateText() { return "TH33"; }
	
	void setPoints() {
	    
	    inputCount=3;
	    
	    super.setPoints();
	 
	    if (useEuroGates()) {
		createEuroGatePolygon();
	    } else {
		createThesholdPolygon();
	    }
	    if (isInverting()) {
		pcircle = interpPoint(point1, point2, .5+(ww+4)/dn);
		lead2 = interpPoint(point1, point2, .5+(ww+8)/dn);
	    }
	}
	String getGateName() { return "TH33 gate"; }
	
	boolean lastState = false;
	int threshold = 3;
	boolean calcFunction() {
	    int i;
	    boolean f = lastState;
	    
	    int count = 0;
	    for (i = 0; i != inputCount; i++){
		if(getInput(i)==true){
		    count++;
		}
	    }
	    if(count >= threshold){
		f = true;
	    }else if(count==0){
		f = false;
	    }
	    
	    lastState = f;
	    return f;
	}
	int getDumpType() { return 429; }
	//int getShortcut() { return '6'; }
    }
