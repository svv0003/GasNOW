package project.gasnow.favorite.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.favorite.model.dto.Favorite;

@Mapper
public interface FavoriteMapper {
    Favorite getFavorite(String userId, String gsId);
    void addFavorite(String userId, String gsId);
    void removeFavorite(String userId, String gsId);

}
