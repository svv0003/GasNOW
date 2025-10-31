package project.gasnow.favorite.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gasnow.favorite.model.dto.Favorite;
import project.gasnow.favorite.model.mapper.FavoriteMapper;

@Transactional
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    /* boolean으로 바꾸기 */
    @Override
    public String getFavorite(Favorite favorite) {
        Favorite result = favoriteMapper.getFavorite(favorite.getUserId(), favorite.getGsId());
        if (result != null) {
                 return "1";
        } else {
                return "0";
         }
    }

    @Override
    public void addFavorite(Favorite favorite) {
        Favorite result = favoriteMapper.getFavorite(favorite.getUserId(), favorite.getGsId());
        if (result == null) {
            favoriteMapper.addFavorite(favorite.getGsId(), favorite.getGsId());

        } else {
            removeFavorite(favorite);

        }

    }

    @Override
    public void removeFavorite(Favorite favorite) {
        favoriteMapper.removeFavorite(favorite.getGsId(), favorite.getGsId());

    }

}
