/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parking;

/**
 *
 * @author LlunaSeguradoPeris
 */
public class LicensePlateValidator {
    
    //Eliminar espacios en blanco al inicio y final, valida matrícula y convierte todas las letras a mayúsculas
    public static String validateAndNormalize(String licensePlate) throws IllegalArgumentException {
        licensePlate = licensePlate.trim();

        if (!isValidLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Invalid license plate format: " + licensePlate);
        }
        licensePlate = licensePlate.toUpperCase();
        
        return licensePlate;
    }

    // Método para validar el formato de matrícula
    // Verificar si tiene exactamente 7 caracteres, los primeros 4 caracteres (números) y los últimos 3 caracteres (letras)
    private static boolean isValidLicensePlate(String licensePlate) {
        // 
        if (licensePlate.length() != 7) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            char c = licensePlate.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        for (int i = 4; i < 7; i++) {
            char c = licensePlate.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        
        return true;
    }
}
