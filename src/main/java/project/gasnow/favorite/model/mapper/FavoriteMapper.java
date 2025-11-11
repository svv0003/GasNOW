package project.gasnow.favorite.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.favorite.model.dto.Favorite;
import project.gasnow.rating.model.dto.Rating;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    Favorite getFavorite(String userId, String gsId);
    List<String> getFavoriteList(String userId);
    void addFavorite(String userId, String gsId);
    void removeFavorite(String userId, String gsId);

}
