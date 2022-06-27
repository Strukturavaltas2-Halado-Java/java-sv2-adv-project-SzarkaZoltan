package beerwebshopproject.beer.repository;

import beerwebshopproject.beer.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {

    @Query("select b from Beer b where (:brand is null or b.brand = :brand) and (:type is null or b.type= :type)")
    List<Beer> findBeersByBrandAndType(@Param("brand") Optional<String> brand, @Param("type") Optional<String> type);

    @Query("select b.brand from Beer b")
    Set<String> getAllBrands();
}
