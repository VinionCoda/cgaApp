/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

/**
 * This class monitors the current state of the program as it
 * transitions from open to saving to closing.
 * 
 * @author takeb
 */
public final class AppState {

    private String state;

    public AppState() {
        this.state = "OPEN";
    }
    public final void OPEN() {
        this.state = "OPEN";
    }
    public final void WIP() {
        this.state = "WIP";
    }
    public final void SAVED() {
        this.state = "SAVED";
    }
    public final void FAIL() {
        this.state = "FAIL";
    }
    public final void CLOSING() {
        this.state = "CLOSING";
    }
    public final String getAppState(){    
    return this.state;
    }
    

}
