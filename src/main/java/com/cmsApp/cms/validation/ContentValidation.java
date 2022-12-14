package com.cmsApp.cms.validation;

import com.cmsApp.cms.exception.TimeWindowException;
import com.cmsApp.cms.global.Global;
import com.cmsApp.cms.model.Content;
import com.cmsApp.cms.model.License;
import org.springframework.stereotype.Component;

@Component
public class ContentValidation extends Global {

    public boolean isLicenseValidForContent(Content content, License newLicense) throws TimeWindowException {
        //If the license is already added
        if (content.getLicensesOfContent().contains(newLicense)) {
            throw new IllegalArgumentException("The content already has the license.");
        }
        //License is not added, yet
        else {
            //Controls if the license to be added conflicts with other licences.
            for (License existedLicense : content.getLicensesOfContent()) {
                //Conflict in startTime
                if ((newLicense.getStartTime() < existedLicense.getStartTime())
                        && (newLicense.getEndTime() > existedLicense.getStartTime())) {
                   throw new TimeWindowException("Time windows is overlapped.");
                }
                //Conflict in the middle
                else if ((newLicense.getStartTime() > existedLicense.getStartTime())
                        && (newLicense.getEndTime() < existedLicense.getEndTime())) {
                    throw new TimeWindowException("Time windows is overlapped.");
                }
                //Conflict in the endTime
                else if ((newLicense.getStartTime() < existedLicense.getEndTime())
                        && (newLicense.getEndTime() > existedLicense.getEndTime())) {
                    throw new TimeWindowException("Time windows is overlapped.");
                }
                //Conflict in the startTime or endTime
                else if (newLicense.getStartTime().equals(existedLicense.getStartTime())     //Conflict if startTime
                        || newLicense.getEndTime().equals(existedLicense.getEndTime())) {    //OR endTime is equal
                    throw new TimeWindowException("Time windows is overlapped.");
                }
                //No conflict with other licenses' timeframe
                else {
                    return true;
                }
            }
            //Content's license list is empty
            return true;
        }
    }

    public boolean isLicenseInList(Content content, License license) {
        //if license is in the list, return true
        return content.getLicensesOfContent().contains(license);
    }

    public boolean isTimeAvailableToPublish(Long startTime, Long endTime) {
        Long localTime = Global.getLocalTime();
        //If current time is in the license's time frame, return true
        return (startTime < localTime) && (endTime > localTime);
    }
}
