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

    class NullConventionReg extends ChipElm {
	final int FLAG_RESET = 2;
	final int FLAG_SET = 4;
	final int FLAG_INVERT_SET_RESET = 8;
	boolean hasReset() { return (flags & FLAG_RESET) != 0 || hasSet(); }
	boolean hasSet() { return (flags & FLAG_SET) != 0; }
	boolean invertSetReset() { return (flags & FLAG_INVERT_SET_RESET) != 0; }
	
	private static final int HS_W_O_PIN = 15;
	private static final int HS_W_I_PIN = 14;
	
	private static final int HS_E_O_PIN = 13;
	private static final int HS_E_I_PIN = 12;
	private static final int RST_PIN = 16;

	
	boolean[] latched_value = {false,true,false,true,false,true};
	
	boolean justLoaded;
	
	public enum states {STABLE, NULL_RECEIVED, WAIT_FOR_RESPONSE, WAIT_FOR_DATA }
	states current_state;
	
	public NullConventionReg(int xx, int yy) {
            super(xx, yy);
            current_state = states.STABLE;
            
            //This was for Q and Q inverse
	    pins[13].value = true;
	    CirSim.console("Loaded1");
        }
	public NullConventionReg(int xa, int ya, int xb, int yb, int f,StringTokenizer st) {
	    super(xa, ya, xb, yb, f, st);
	    
	  //This was for Q and Q inverse
	  //pins[2].value = !pins[1].value;
	    justLoaded = true;
	    
	    current_state = states.STABLE;
	    CirSim.console("Loaded2");
	    
	}
	String getChipName() { return "NCL Reg"; }
	void setupPins() {
	    sizeX = 7;
	    sizeY = 9;
	    pins = new Pin[getPostCount()];
	    pins[0] = new Pin(0, SIDE_W, "D0_1");
	    pins[1] = new Pin(1, SIDE_W, "D0_0");
	    pins[2] = new Pin(2, SIDE_W, "D1_1");
	    pins[3] = new Pin(3, SIDE_W, "D1_0");
	    pins[4] = new Pin(4, SIDE_W, "D2_1");
	    pins[5] = new Pin(5, SIDE_W, "D2_0");
	    
	    
	    pins[6] = new Pin(0, SIDE_E, "Q0_1");
	    pins[6].output = true;
	    pins[6].state = false;
	    
	    pins[7] = new Pin(1, SIDE_E, "Q0_0");
	    pins[7].output = true;
	    pins[7].state = false;
	    
	    pins[8] = new Pin(2, SIDE_E, "Q1_1");
	    pins[8].output = true;
	    pins[8].state = false;
	    
	    pins[9] = new Pin(3, SIDE_E, "Q1_0");
	    pins[9].output = true;
	    pins[9].state = false;
	    
	    pins[10] = new Pin(4, SIDE_E, "Q2_1");
	    pins[10].output = true;
	    pins[10].state = false;
	    
	    pins[11] = new Pin(5, SIDE_E, "Q2_0");
	    pins[11].output = true;
	    pins[11].state = false;
	    
	    
	    pins[12] = new Pin(6, SIDE_E, "HS_E_I");
	    pins[13] = new Pin(7, SIDE_E, "HS_E_O");
	    pins[13].output = true;
	    pins[13].state = false;
	    
	    pins[14] = new Pin(7, SIDE_W, "HS_W_I");
	    pins[15] = new Pin(6, SIDE_W, "HS_W_O");
	    pins[15].output = true;
	    pins[15].state = false;
	    
	    
	    
	    pins[16] = new Pin(0, SIDE_S, "RST");
	    pins[17] = new Pin(1, SIDE_S, "R0_1");
	    pins[18] = new Pin(2, SIDE_S, "R0_0");
	    
	    pins[19] = new Pin(3, SIDE_S, "R1_1");
	    pins[20] = new Pin(4, SIDE_S, "R1_0");
	    
	    pins[21] = new Pin(5, SIDE_S, "R2_1");
	    pins[22] = new Pin(6, SIDE_S, "R2_0");
	    
	}
	int getPostCount() {
	    return 23;
	}
	int getVoltageSourceCount() { return 8; }
        void reset() {
            super.reset();
            
            current_state = states.STABLE;
            
            for(int i=0; i<getPostCount()+1; i++){
        	if (pins[i].output){pins[i].value = false;}
            }
            
            
            
	    //volts[2] = highVoltage;
	    //pins[2].value = true;
        }
	void execute() {
            // if we just loaded then the volts[] array is likely to be all zeroes, which might force us to do a reset, so defer execution until the next iteration
            if (justLoaded) {
                justLoaded = false;
                return;
            }
            
            if(pins[RST_PIN].value == true){
        	
        	latched_value[0] = pins[17].value;
        	latched_value[1] = pins[18].value;
        	latched_value[2] = pins[19].value;
        	latched_value[3] = pins[20].value;
        	latched_value[4] = pins[21].value;
        	latched_value[5] = pins[22].value;
        	
        	current_state = states.NULL_RECEIVED;
            }
            
            switch(current_state) {
                case STABLE:            
                    pins[HS_W_O_PIN].value = false;
                    boolean null_input = true;
                    for(int i=0; i<5+1; i++){
                	if(pins[i].value != false){
                	    null_input = false;
                	    break;
                	}
                    }
                    
                    if(null_input==true || pins[HS_W_I_PIN].value == true){
                	current_state = states.NULL_RECEIVED;
                    }
                    
                  break;
                case NULL_RECEIVED:
                    
                    // Hold out outputs as NULL as we wait
                    for(int i=6; i<11+1; i++){
                	writeOutput(i,false);
                    }
                    
                    current_state = states.WAIT_FOR_RESPONSE;
                  
                  break;
                case WAIT_FOR_RESPONSE:
                    
                    // Wait with out outputs as NULL until a handshake from the right is received
                    // Then we flip to the data we had originally.
                    if(pins[HS_E_I_PIN].value == true){
                	
                	writeOutput(6, latched_value[0]);
                	writeOutput(7, latched_value[1]);
                	writeOutput(8, latched_value[2]);
                	writeOutput(9, latched_value[3]);
                	writeOutput(10, latched_value[4]);
                	writeOutput(11, latched_value[5]);
                	
                	pins[HS_W_O_PIN].value = true;
                	current_state = states.WAIT_FOR_DATA;
                    }
                    
                    
                    break;
                case WAIT_FOR_DATA:
                    
                    null_input=true;
                    
                    if ((pins[0].value || pins[1].value) && (pins[2].value || pins[3].value) && (pins[4].value || pins[5].value)){
                	null_input = false;
                    }
                    
                    
                    if(null_input == false){
                	
                	latched_value[0] = pins[0].value;
                	latched_value[1] = pins[1].value;
                	latched_value[2] = pins[2].value;
                	latched_value[3] = pins[3].value;
                	latched_value[4] = pins[4].value;
                	latched_value[5] = pins[5].value;
                	
                    	writeOutput(6,latched_value[0]);
                    	writeOutput(7,latched_value[1]);
                    	writeOutput(8,latched_value[2]);
                    	writeOutput(9,latched_value[3]);
                    	writeOutput(10,latched_value[4]);
                    	writeOutput(11,latched_value[5]);
                    	
                    	
                    	pins[HS_W_O_PIN].value = false;
                    	current_state = states.STABLE;
                    }
                    
                    

                    
                    
                    
                    break;
                default:
                  // code block
          }
	    
	    //if (pins[3].value && !lastClock)
	//	writeOutput(1, pins[0].value);
	    //if(hasSet() && pins[5].value != invertSetReset())
	//	writeOutput(1, true);
	    //if(hasReset() && pins[4].value != invertSetReset())
	//	writeOutput(1, false);
	    //writeOutput(2, !pins[1].value);
	   // lastClock = pins[3].value;
	}
	
	
	int getDumpType() { return 451; }
	
	
	
	
	public EditInfo getChipEditInfo(int n) {
	    if (n == 0) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Reset Pin", hasReset());
		return ei;
	    }
	    if (n == 1) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Set Pin", hasSet());
		return ei;
	    }
	    if (n == 2) {
		EditInfo ei = new EditInfo("", 0, -1, -1);
		ei.checkbox = new Checkbox("Invert Set/Reset", invertSetReset());
		return ei;
	    }
	    return super.getChipEditInfo(n);
	}
	public void setChipEditValue(int n, EditInfo ei) {
	    if (n == 0) {
		if (ei.checkbox.getState())
		    flags |= FLAG_RESET;
		else
		    flags &= ~FLAG_RESET|FLAG_SET;
		setupPins();
		allocNodes();
		setPoints();
	    }
	    if (n == 1) {
		if (ei.checkbox.getState())
		    flags |= FLAG_SET;
		else
		    flags &= ~FLAG_SET;
		setupPins();
		allocNodes();
		setPoints();
	    }
	    if (n == 2) {
		flags = ei.changeFlag(flags, FLAG_INVERT_SET_RESET);
		setupPins();
		setPoints();
	    }
	    super.setChipEditValue(n, ei);
	}
    }
