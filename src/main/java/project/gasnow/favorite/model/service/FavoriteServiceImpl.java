package project.gasnow.favorite.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gasnow.favorite.model.dto.Favorite;
import project.gasnow.favorite.model.mapper.FavoriteMapper;

import java.util.List;

@Transactional
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public String getFavorite(String userId, String gsId) {
        Favorite result = favoriteMapper.getFavorite(userId, gsId);
        if (result != null) {
                 return "1";
        } else {
                return "0";
         }
    }
    @Override
    public List<String> getFavoriteList(String userId){

        return favoriteMapper.getFavoriteList(userId);
    }

    @Override
    public void addFavorite(Favorite favorite) {
        Favorite result = favoriteMapper.getFavorite(favorite.getUserId(), favorite.getGsId());
        if (result == null) {
            favoriteMapper.addFavorite(favorite.getUserId(), favorite.getGsId());

        } else {
            removeFavorite(favorite);

        }

    }

    @Override
    public void removeFavorite(Favorite favorite) {
        favoriteMapper.removeFavorite(favorite.getUserId(), favorite.getGsId());

    }

}
