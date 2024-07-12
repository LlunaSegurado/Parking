/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parking;

/**
 *
 * @author LlunaSeguradoPeris
 */
public class NonResidentVehicle extends VehicleBase{
    
    public NonResidentVehicle(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public double calculateCharge() {
        return getStayDuration().toMinutes() * 0.02; // Cargo por minuto para no residentes
    }
    
}
