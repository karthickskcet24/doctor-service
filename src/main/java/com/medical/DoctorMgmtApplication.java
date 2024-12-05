package com.medical;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.medical.entity.Organization;
import com.medical.repository.OrganizationRepository;

@SpringBootApplication
public class DoctorMgmtApplication implements CommandLineRunner {

    @Autowired
    OrganizationRepository organizationRepo;

    public static void main(String[] args) {
        SpringApplication.run(DoctorMgmtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Organization> organizations = organizationRepo.findAll();
        if (organizations.isEmpty()) {
        	List<Organization> listOfOrganizations = new ArrayList<>();
        	listOfOrganizations.add(addOrganization("HealthCare Inc.", "123 Health St, Wellness City, HC 12345", "+1234567890"));
        	listOfOrganizations.add(  addOrganization("Wellness Group", "456 Wellness Ave, Healthy Town, HC 67890", "+0987654321"));
        	listOfOrganizations.add(  addOrganization("Care Clinic", "789 Care Blvd, MediCity, HC 23456", "+1122334455"));
        	listOfOrganizations.add( addOrganization("Family Health Center", "321 Family Rd, Healthburg, HC 34567", "+5544332211"));
        	listOfOrganizations.add( addOrganization("Community Medical Center", "654 Community Dr, Wellville, HC 45678", "+9988776655"));
        	listOfOrganizations.add(  addOrganization("Preventive Health Services", "987 Preventive Ln, HealthyTown, HC 56789", "+2233445566"));
        	listOfOrganizations.add(  addOrganization("Elite Medical Group", "135 Elite St, FitCity, HC 67890", "+3322114455"));
        	listOfOrganizations.add( addOrganization("Pediatrics and Family Care", "246 Kids Ave, Family Town, HC 78901", "+4455667788"));
        	listOfOrganizations.add( addOrganization("Senior Wellness Center", "369 Senior Way, OldTown, HC 89012", "+5566778899"));
        	listOfOrganizations.add(  addOrganization("Urgent Care Solutions", "753 Urgent Rd, QuickCare City, HC 90123", "+6677889900"));
        	organizationRepo.saveAll(listOfOrganizations);
        } else {
            System.out.println("Organization data already exists.");
        }
    }

    private Organization addOrganization(String name, String address, String phone) {
        Organization organization = new Organization();
        organization.setName(name);
        organization.setAddress(address);
        organization.setPhone(phone);
        return organization;
    }
}
