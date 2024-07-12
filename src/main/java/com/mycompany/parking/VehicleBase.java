/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parking;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author LlunaSeguradoPeris
 */
public abstract class VehicleBase implements Vehicle, Serializable{
    
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    
    public VehicleBase(String licensePlate) {
        this.licensePlate = licensePlate;
    }
            

    @Override
    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public void registerEntry(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        this.exitTime = null; // Reiniciar el tiempo de salida en una nueva entrada
    }

    @Override
    public void registerExit(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    @Override
    public Duration getStayDuration() {
        return Duration.between(entryTime, exitTime);
    }

    @Override
    public abstract double calculateCharge();
    
    public boolean isParked() {
        return entryTime != null && exitTime == null;
    }
    
}
