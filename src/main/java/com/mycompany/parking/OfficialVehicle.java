/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parking;

import java.io.Serializable;

/**
 *
 * @author LlunaSeguradoPeris
 */
public class OfficialVehicle extends VehicleBase implements Serializable{
    
    
    public OfficialVehicle(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public double calculateCharge() {
        return 0.0; 
    }

    /*public void clearEntries() {
        entries.clear();
    }*/
}
