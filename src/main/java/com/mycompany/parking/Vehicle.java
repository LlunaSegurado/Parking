/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.parking;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author LlunaSeguradoPeris
 */
public interface Vehicle {
    
    String getLicensePlate();
    void registerEntry(LocalDateTime entryTime);
    void registerExit(LocalDateTime exitTime);
    Duration getStayDuration();
    double calculateCharge();
    
}
