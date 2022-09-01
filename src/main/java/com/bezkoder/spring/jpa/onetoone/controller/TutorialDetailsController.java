package com.bezkoder.spring.jpa.onetoone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.jpa.onetoone.exception.ResourceNotFoundException;
import com.bezkoder.spring.jpa.onetoone.model.TutorialDetails;
import com.bezkoder.spring.jpa.onetoone.model.Tutorial;
import com.bezkoder.spring.jpa.onetoone.repository.TutorialDetailsRepository;
import com.bezkoder.spring.jpa.onetoone.repository.TutorialRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TutorialDetailsController {
  @Autowired
  private TutorialDetailsRepository detailsRepository;

  @Autowired
  private TutorialRepository tutorialRepository;

  @GetMapping({ "/details/{id}", "/tutorials/{id}/details" })
  public ResponseEntity<TutorialDetails> getDetailsById(@PathVariable(value = "id") Long id) {
    TutorialDetails details = detailsRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial Details with id = " + id));

    return new ResponseEntity<>(details, HttpStatus.OK);
  }

  @PostMapping("/tutorials/{tutorialId}/details")
  public ResponseEntity<TutorialDetails> createDetails(@PathVariable(value = "tutorialId") Long tutorialId,
      @RequestBody TutorialDetails detailsRequest) {
    Tutorial tutorial = tutorialRepository.findById(tutorialId)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

    detailsRequest.setCreatedOn(new java.util.Date());
    detailsRequest.setTutorial(tutorial);
    TutorialDetails details = detailsRepository.save(detailsRequest);

    return new ResponseEntity<>(details, HttpStatus.CREATED);
  }

  @PutMapping("/details/{id}")
  public ResponseEntity<TutorialDetails> updateDetails(@PathVariable("id") long id,
      @RequestBody TutorialDetails detailsRequest) {
    TutorialDetails details = detailsRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));

    details.setCreatedBy(detailsRequest.getCreatedBy());

    return new ResponseEntity<>(detailsRepository.save(details), HttpStatus.OK);
  }

  @DeleteMapping("/details/{id}")
  public ResponseEntity<HttpStatus> deleteDetails(@PathVariable("id") long id) {
    detailsRepository.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/tutorials/{tutorialId}/details")
  public ResponseEntity<TutorialDetails> deleteDetailsOfTutorial(@PathVariable(value = "tutorialId") Long tutorialId) {
    if (!tutorialRepository.existsById(tutorialId)) {
      throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
    }

    detailsRepository.deleteByTutorialId(tutorialId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
