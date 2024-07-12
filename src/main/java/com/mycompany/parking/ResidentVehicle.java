/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parking;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author LlunaSeguradoPeris
 */
public class ResidentVehicle extends VehicleBase implements Serializable {
    
    private long accumulatedTimeMin;

    public ResidentVehicle(String licensePlate) {
        super(licensePlate);
    }
    
    @Override
    public void registerExit(LocalDateTime exitTime) {
        super.registerExit(exitTime);
        accumulatedTimeMin += getStayDuration().toMinutes();
    }


    @Override
    public double calculateCharge() {
        return accumulatedTimeMin * 0.002;
    }

    public void resetAccumulatedTime() {
        accumulatedTimeMin = 0;
    }

    public int getAccumulatedTime() {
        return (int) accumulatedTimeMin; // Devuelve el tiempo acumulado en minutos como entero
    }
}
