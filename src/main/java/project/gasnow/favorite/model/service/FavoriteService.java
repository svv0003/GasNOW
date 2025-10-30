package project.gasnow.favorite.model.service;

import project.gasnow.favorite.model.dto.Favorite;

public interface FavoriteService {
    String getFavorite(Favorite favorite);
    void addFavorite(Favorite favorite);
    void removeFavorite(Favorite favorite);
}
