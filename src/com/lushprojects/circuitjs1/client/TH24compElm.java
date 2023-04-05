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

    class TH24compElm extends GateElm {
	public TH24compElm(int xx, int yy) {
	    super(xx, yy); 
	    
	}
	public TH24compElm(int xa, int ya, int xb, int yb, int f,
			  StringTokenizer st) {
	    super(xa, ya, xb, yb, f, st);
	}
	
	String getGateText() { return "TH24comp"; }
	
	void setPoints() {
	    
	    inputCount=4;
	    
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
	String getGateName() { return "TH24comp gate"; }
	
	boolean lastState = false;
	int threshold = 1;
	boolean calcFunction() {
	    int i;
	    boolean f = lastState;
	    
	    int count = 0;
	    for (i = 0; i != inputCount; i++){
		if(getInput(i)==true){
		    count++;
		}
	    }
	    
	    boolean a_in = getInput(3);
	    boolean b_in = getInput(2);
	    boolean c_in = getInput(1);
	    boolean d_in = getInput(0);
	    
	    if( (a_in&c_in) || (b_in && c_in) || (a_in && d_in) || (b_in && d_in) ){
		f = true;
	    }else if(count==0){
		f = false;
	    }
	    
	    lastState = f;
	    return f;
	}
	int getDumpType() { return 449; }
	//int getShortcut() { return '6'; }
    }
