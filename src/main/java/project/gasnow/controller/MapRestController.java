package project.gasnow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.gasnow.favorite.model.dto.Favorite;
import project.gasnow.favorite.model.service.FavoriteService;
import project.gasnow.rating.model.dto.Rating;
import project.gasnow.rating.model.mapper.RatingMapper;
import project.gasnow.rating.model.service.RatingService;

import java.util.Date;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor

public class MapRestController{
    public final RatingService ratingService;
    public final FavoriteService favoriteService;

    @GetMapping("getRating")
    public double getAvgRating(@RequestParam String gsId) {
        return  ratingService.getAvgRating(gsId);
    }

    @GetMapping("ratingDate")
    public Date checkingInputRating(@RequestParam String gsId, String userId) {
        return  ratingService.checkingInputRating(gsId, userId);
    }

    @PostMapping("addRating")
    public String  addRating(@RequestBody Rating rating) {
        return  ratingService.addRating(rating);
    }

    @GetMapping("isFav")
    public String getFavorite(@RequestParam String userId, String gsId){
        return favoriteService.getFavorite(userId, gsId);
    }

    @PostMapping("addFav")
    public void addFavorite(@RequestBody Favorite favorite) {
        favoriteService.addFavorite(favorite);
    }

}
