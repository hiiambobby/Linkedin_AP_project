//package com.backend.LinkedinServer.Controller;
//
//import com.backend.LinkedinServer.Model.Education;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class EducationController{
//private final List<Education> educations;
//
//public EducationController() {
//    educations = new ArrayList<>();
//    // Add initial contact infos here if needed
//}
//
//public List<Education> getAllContactInfos() {
//    return educations;
//}
//
//public Education getContactInfoByPhoneNumber(String phoneNumber) {
//    return educations.stream()
//            .filter(contactInfo -> contactInfo.getPhoneNumber().equals(phoneNumber))
//            .findFirst()
//            .orElse(null);
//}
//
//public void createContactInfo(Education contactInfo) {
//    educations.add(contactInfo);
//}
//
//public void updateContactInfo(String phoneNumber, Education updatedContactInfo) {
//    for (int i = 0; i < educations.size(); i++) {
//        if (educations.get(i).getPhoneNumber().equals(phoneNumber)) {
//            educations.set(i, updatedContactInfo);
//            return;
//        }
//    }
//}
//
//public void deleteContactInfo(String school) {
//    educations.removeIf(education -> education.getSchool().equals(school));
//}
//}
