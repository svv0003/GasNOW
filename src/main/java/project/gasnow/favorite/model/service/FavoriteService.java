package project.gasnow.favorite.model.service;

import project.gasnow.favorite.model.dto.Favorite;

import java.util.List;

public interface FavoriteService {
    String getFavorite(String userId, String gsId);
    List<String> getFavoriteList(String userId);
    void addFavorite(Favorite favorite);
    void removeFavorite(Favorite favorite);
}
