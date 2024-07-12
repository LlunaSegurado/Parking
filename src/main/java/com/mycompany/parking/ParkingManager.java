package com.mycompany.parking;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;

public class ParkingManager implements Serializable {
    
    
    private static final String DATA_DIRECTORY = "data/";
    private static final String OFFICIAL_VEHICLES_FILE = DATA_DIRECTORY + "official_vehicles.data";
    private static final String RESIDENT_VEHICLES_FILE = DATA_DIRECTORY + "resident_vehicles.data";
    private static final String NON_RESIDENT_ENTRIES_FILE = DATA_DIRECTORY + "non_resident_entries.data";

    
    private Map<String, LocalDateTime> nonResidentEntries = new HashMap<>();
    private Map<String, OfficialVehicle> officialVehicles = new HashMap<>();
    private Map<String, ResidentVehicle> residentVehicles = new HashMap<>();
    
    
    public ParkingManager() {
        
        initializeDataDirectory();
        loadData();
    }
    
    private void initializeDataDirectory() {
        File dataDirectory = new File(DATA_DIRECTORY);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
    }
    
    private void loadData() {
        officialVehicles = loadVehicles(OFFICIAL_VEHICLES_FILE, new HashMap<>());
        residentVehicles = loadVehicles(RESIDENT_VEHICLES_FILE, new HashMap<>());
        nonResidentEntries = loadNonResidentEntries();
    }

    @SuppressWarnings("unchecked")
    private <T extends VehicleBase> Map<String, T> loadVehicles(String filename, Map<String, T> defaultMap) {
        File file = new File(filename);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (Map<String, T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return defaultMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, LocalDateTime> loadNonResidentEntries() {
        File file = new File(NON_RESIDENT_ENTRIES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NON_RESIDENT_ENTRIES_FILE))) {
                return (Map<String, LocalDateTime>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    public void registerEntry(String licensePlate) throws IOException {
        if (officialVehicles.containsKey(licensePlate)) {
            registerOfficialVehicleEntry(licensePlate);
        } else if (residentVehicles.containsKey(licensePlate)) {
            registerResidentVehicleEntry(licensePlate);
        } else {
            registerNonResidentEntry(licensePlate);
        }
    }

    private void registerOfficialVehicleEntry(String licensePlate) throws IOException {
        OfficialVehicle officialVehicle = officialVehicles.get(licensePlate);
        officialVehicle.registerEntry(LocalDateTime.now());
        showMessage("Official vehicle with license plate " + licensePlate + " entered.", "Vehicle Entry");
        saveVehicles(OFFICIAL_VEHICLES_FILE, officialVehicles);
    }

    private void registerResidentVehicleEntry(String licensePlate) throws IOException {
        ResidentVehicle residentVehicle = residentVehicles.get(licensePlate);
        residentVehicle.registerEntry(LocalDateTime.now());
        showMessage("Resident vehicle with license plate " + licensePlate + " entered.", "Vehicle Entry");
        saveVehicles(RESIDENT_VEHICLES_FILE, residentVehicles);
    }

    private void registerNonResidentEntry(String licensePlate) throws IOException {
        nonResidentEntries.put(licensePlate, LocalDateTime.now());
        saveNonResidentEntries();
        showMessage("Non-resident vehicle with license plate " + licensePlate + " entered.", "Vehicle Entry");
    }

    public void registerExit(String licensePlate) throws IOException {
        if (officialVehicles.containsKey(licensePlate)) {
            registerOfficialVehicleExit(licensePlate);
        } else if (residentVehicles.containsKey(licensePlate)) {
            registerResidentVehicleExit(licensePlate);
        } else if (nonResidentEntries.containsKey(licensePlate)) {
            registerNonResidentExit(licensePlate);
        } else {
            throw new IOException("Vehicle with license plate " + licensePlate + " not found inside the parking.");
        }
    }

    private void registerOfficialVehicleExit(String licensePlate) throws IOException {
        OfficialVehicle officialVehicle = officialVehicles.get(licensePlate);
        officialVehicle.registerExit(LocalDateTime.now());
        showMessage("Official vehicle with license plate " + licensePlate + " exited.", "Vehicle Exit");
        saveVehicles(OFFICIAL_VEHICLES_FILE, officialVehicles);
    }

    private void registerResidentVehicleExit(String licensePlate) throws IOException {
        ResidentVehicle residentVehicle = residentVehicles.get(licensePlate);
        residentVehicle.registerExit(LocalDateTime.now());
        showMessage("Resident vehicle with license plate " + licensePlate + " exited.", "Vehicle Exit");
        saveVehicles(RESIDENT_VEHICLES_FILE, residentVehicles);
    }

    private void registerNonResidentExit(String licensePlate) throws IOException {
        LocalDateTime entryTime = nonResidentEntries.remove(licensePlate);
        LocalDateTime exitTime = LocalDateTime.now();
        Duration duration = Duration.between(entryTime, exitTime);
        double charge = duration.toMinutes() * 0.02;
        String formattedCharge = String.format("%.2f", charge);
        showMessage("Amount to pay for non-resident vehicle with license plate " + licensePlate + ": " + formattedCharge  + " EUR", "Payment Information");
        showMessage("Non-resident vehicle with license plate " + licensePlate + " exited.", "Vehicle Exit");
        saveNonResidentEntries();
    }

    public void addOfficialVehicle(String licensePlate) throws IOException {
        if (residentVehicles.containsKey(licensePlate)) {
            showError("Vehicle with license plate " + licensePlate + " is already registered as a resident vehicle.");
        } else if (officialVehicles.containsKey(licensePlate)) {
            showError("Vehicle with license plate " + licensePlate + " is already registered as an official vehicle.");
        } else {
            officialVehicles.put(licensePlate, new OfficialVehicle(licensePlate));
            saveVehicles(OFFICIAL_VEHICLES_FILE, officialVehicles);
            showMessage("Official vehicle with license plate " + licensePlate + " added successfully.", "Success");
        }
    }

    public void addResidentVehicle(String licensePlate) throws IOException {
        if (officialVehicles.containsKey(licensePlate)) {
            showError("Vehicle with license plate " + licensePlate + " is already registered as an official vehicle.");
        } else if (residentVehicles.containsKey(licensePlate)) {
            showError("Vehicle with license plate " + licensePlate + " is already registered as a resident vehicle.");
        } else {
            residentVehicles.put(licensePlate, new ResidentVehicle(licensePlate));
            saveVehicles(RESIDENT_VEHICLES_FILE, residentVehicles);
            showMessage("Resident vehicle with license plate " + licensePlate + " added successfully.", "Success");
        }
    }

    private void saveVehicles(String filename, Map<String, ? extends VehicleBase> vehicles) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(vehicles);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    
    private void saveNonResidentEntries() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NON_RESIDENT_ENTRIES_FILE))) {
            oos.writeObject(nonResidentEntries);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void startNewMonth() throws IOException {
        for (ResidentVehicle resident : residentVehicles.values()) {
            resident.resetAccumulatedTime();
        }
        saveVehicles(RESIDENT_VEHICLES_FILE, residentVehicles);
    }
    
    public void generateResidentPaymentReport(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String header = String.format("%-15s %-25s %-15s", "License Plate", "Accumulated Time (min)", "Amount to Pay (EUR)");
            writer.println(header);

            for (ResidentVehicle resident : residentVehicles.values()) {
                String line = String.format("%-15s %-25d %-15.2f",
                        resident.getLicensePlate(),
                        resident.getAccumulatedTime(),
                        resident.calculateCharge());
                writer.println(line);
            }
        }
    }
    
    public void exit() throws IOException {
        saveVehicles(OFFICIAL_VEHICLES_FILE, officialVehicles);
        saveVehicles(RESIDENT_VEHICLES_FILE, residentVehicles);
    }

    public void listVehicles() {
        System.out.println("Official Vehicles:");
        for (String plate : officialVehicles.keySet()) {
            System.out.println(" - " + plate);
        }

        System.out.println("Resident Vehicles:");
        for (String plate : residentVehicles.keySet()) {
            System.out.println(" - " + plate);
        }

        System.out.println("Non-Resident Entries:");
        for (String plate : nonResidentEntries.keySet()) {
            System.out.println(" - " + plate);
        }
    }
    
    public String getOfficialVehicleList() {
        return getVehicleList("Official Vehicles", officialVehicles.keySet());
    }

    public String getResidentVehicleList() {
        return getVehicleList("Resident Vehicles", residentVehicles.keySet());
    }
    
    public String getParkedVehicleList() {
        StringBuilder vehicleList = new StringBuilder();

        vehicleList.append("Official Vehicles Parked:\n");
        for (Map.Entry<String, OfficialVehicle> entry : officialVehicles.entrySet()) {
            if (entry.getValue().isParked()) {
                vehicleList.append(" - ").append(entry.getKey()).append("\n");
            }
        }

        vehicleList.append("Resident Vehicles Parked:\n");
        for (Map.Entry<String, ResidentVehicle> entry : residentVehicles.entrySet()) {
            if (entry.getValue().isParked()) {
                vehicleList.append(" - ").append(entry.getKey()).append("\n");
            }
        }

        vehicleList.append("Non-Resident Vehicles Parked:\n");
        for (String plate : nonResidentEntries.keySet()) {
            vehicleList.append(" - ").append(plate).append("\n");
        }

        return vehicleList.toString();
    }
    
    private String getVehicleList(String title, Set<String> plates) {
        StringBuilder vehicleList = new StringBuilder();
        vehicleList.append(title).append(":\n");
        plates.forEach(plate -> vehicleList.append(" - ").append(plate).append("\n"));
        return vehicleList.toString();
    }
    
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}