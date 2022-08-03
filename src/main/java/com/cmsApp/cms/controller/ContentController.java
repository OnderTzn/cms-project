package com.cmsApp.cms.controller;

import com.cmsApp.cms.exception.TimeWindowException;
import com.cmsApp.cms.model.Content;
import com.cmsApp.cms.model.License;
import com.cmsApp.cms.repository.ContentRepository;
import com.cmsApp.cms.repository.LicenseRepository;
import com.cmsApp.cms.service.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
@CrossOrigin
@AllArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final ContentRepository contentRepository;
    private final LicenseRepository licenseRepository;

    //Get all the contents
    @GetMapping("/all")
    public ResponseEntity<List<Content>> getAllContents(){
        List<Content> contentList = contentService.findAllContents();
        return new ResponseEntity<>(contentList, HttpStatus.OK);
    }

    //Get content by id
    @GetMapping("/find/{id}")
    public ResponseEntity<Content> getContentById (@PathVariable("id") Long contentId) {
        Content content = contentService.findContentById(contentId);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    //Add new content
    @PostMapping("/add")
    public ResponseEntity<Content> addContent(@RequestBody Content content){
        Content addContent = contentService.addContent(content);
        return new ResponseEntity<>(addContent, HttpStatus.CREATED);
    }


    //Update a content
    @PutMapping("/update/{contentId}")
    public ResponseEntity<Content> updateContent(@PathVariable Long contentId, @RequestBody Content content){
        contentService.updateContent(contentId, content);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    //Add a license to a content
    @PutMapping("/{contentId}/license/{licenseId}")
    public Content addLicenseToContent(@PathVariable Long contentId,
                                       @PathVariable Long licenseId) throws TimeWindowException {

        Content content = contentRepository.findContentById(contentId);
        License license = licenseRepository.findLicenseById(licenseId);

        Content addLicenseToContent = contentService.addLicenseToContent(content, license);
        return new ResponseEntity<>(addLicenseToContent, HttpStatus.OK).getBody();

    }


    //Delete a content
    @DeleteMapping("/delete/{contentId}")
    public ResponseEntity<?> deleteContent(@PathVariable("contentId") Long contentId){
        contentService.deleteContent(contentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Delete a license from content
    @DeleteMapping("/delete/{contentId}/license/{licenseId}")
    public ResponseEntity<?> deleteLicenseFromContent(@PathVariable("contentId") Long contentId,
                                                      @PathVariable("licenseId") Long licenseId) {

        Content content = contentRepository.findContentById(contentId);
        License license = licenseRepository.findLicenseById(licenseId);

        contentService.deleteLicenseFromContent(content, license);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}