package project.gasnow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.gasnow.rating.model.dto.Rating;
import project.gasnow.rating.model.service.RatingService;

import java.util.Date;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MapRestController {

    private final RatingService ratingService;

    @GetMapping("/getRating")
    public double getAvgRating(@RequestParam String gsId) {
        return ratingService.getAvgRating(gsId);
    }

    @GetMapping("/ratingDate")
    public Date checkingInputRating(@RequestParam String gsId, String userId) {
        return ratingService.checkingInputRating(gsId, userId);
    }

    @PostMapping("/addRating")
    public String  addRating(@RequestBody Rating rating){
        return ratingService.addRating(rating);
    }

}


