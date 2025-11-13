package project.gasnow.user.model.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavGsMapper {
    /**
     * 회원 탈퇴 시 즐겨찾기 삭제 메서드
     * @param userId
     */
    void deleteUserFavorites(String userId);
}
