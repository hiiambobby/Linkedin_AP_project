package com.backend.LinkedinServer.Controller;

import com.backend.LinkedinServer.Model.Education;

import java.util.ArrayList;
import java.util.List;

public class EducationController{
private final List<Education> contactInfos;

public ContactInfoController() {
    contactInfos = new ArrayList<>();
    // Add initial contact infos here if needed
}

public List<Education> getAllContactInfos() {
    return contactInfos;
}

public Education getContactInfoByPhoneNumber(String phoneNumber) {
    return contactInfos.stream()
            .filter(contactInfo -> contactInfo.getPhoneNumber().equals(phoneNumber))
            .findFirst()
            .orElse(null);
}

public void createContactInfo(Education contactInfo) {
    contactInfos.add(contactInfo);
}

public void updateContactInfo(String phoneNumber, Education updatedContactInfo) {
    for (int i = 0; i < contactInfos.size(); i++) {
        if (contactInfos.get(i).getPhoneNumber().equals(phoneNumber)) {
            contactInfos.set(i, updatedContactInfo);
            return;
        }
    }
}

public void deleteContactInfo(String phoneNumber) {
    contactInfos.removeIf(contactInfo -> contactInfo.getPhoneNumber().equals(phoneNumber));
}
}
