package com.mycompany.parking;
import java.io.IOException;
import java.util.Scanner;

public class Parking {
    public static void main(String[] args) throws IOException {
        ParkingManager parkingManager = new ParkingManager();
        Scanner scanner = new Scanner(System.in);
        String option = "";

        while (!option.equals("8")) {
            System.out.println("Parking Management System");
            System.out.println("1. Register Entry");
            System.out.println("2. Register Exit");
            System.out.println("3. Add Official Vehicle");
            System.out.println("4. Add Resident Vehicle");
            System.out.println("5. Start New Month");
            System.out.println("6. Generate Resident Payment Report");
            System.out.println("7. List Vehicles");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Enter license plate: ");
                    String entryLicensePlate = scanner.nextLine();
                    parkingManager.registerEntry(entryLicensePlate);
                    break;

                case "2":
                    System.out.print("Enter license plate: ");
                    String exitLicensePlate = scanner.nextLine();
                    parkingManager.registerExit(exitLicensePlate);
                    break;

                case "3":
                    System.out.print("Enter license plate: ");
                    String officialLicensePlate = scanner.nextLine();
                    try {
                        parkingManager.addOfficialVehicle(officialLicensePlate);
                        System.out.println("Official vehicle added.");
                    } catch (IOException e) {
                        System.err.println("Error adding official vehicle: " + e.getMessage());
                    }
                    break;

                case "4":
                    System.out.print("Enter license plate: ");
                    String residentLicensePlate = scanner.nextLine();
                    try {
                        parkingManager.addResidentVehicle(residentLicensePlate);
                        System.out.println("Resident vehicle added.");
                    } catch (IOException e) {
                        System.err.println("Error adding resident vehicle: " + e.getMessage());
                    }
                    break;

                case "5":
                    try {
                        parkingManager.startNewMonth();
                        System.out.println("New month started.");
                    } catch (IOException e) {
                        System.err.println("Error starting new month: " + e.getMessage());
                    }
                    break;

                case "6":
                    System.out.print("Enter the filename to generate the resident payment report: ");
                    String filename = scanner.nextLine(); // Aquí debes obtener el nombre del archivo desde la entrada del usuario
                    try {
                        parkingManager.generateResidentPaymentReport(filename);
                        System.out.println("Resident payment report generated.");
                    } catch (IOException e) {
                        System.err.println("Error generating report: " + e.getMessage());
                    }
                    break;

                case "7":
                    parkingManager.listVehicles();
                    break;

                case "8":
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }

        scanner.close();
    }
}
