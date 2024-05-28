package com.backend.LinkedinServer.Controller;

import com.backend.LinkedinServer.Model.ContactInfo;
import com.backend.LinkedinServer.Model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactInfoController {
    private final List<ContactInfo> contactInfos;

    public ContactInfoController() {
        contactInfos = new ArrayList<>();
        // Add initial contact infos here if needed
    }

    public List<ContactInfo> getAllContactInfos() {
        return contactInfos;
    }

    public ContactInfo getContactInfoByPhoneNumber(String phoneNumber) {
        return contactInfos.stream()
                .filter(contactInfo -> contactInfo.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(null);
    }

    public void createContactInfo(ContactInfo contactInfo) {
        contactInfos.add(contactInfo);
    }

    public void updateContactInfo(String phoneNumber, ContactInfo updatedContactInfo) {
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