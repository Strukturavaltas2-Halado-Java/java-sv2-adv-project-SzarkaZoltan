package beerwebshopproject.webshop.repository;

import beerwebshopproject.webshop.model.Webshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebshopRepository extends JpaRepository<Webshop, Long> {

    @Query("select s from Webshop s left join fetch s.beers")
    List<Webshop> findAllWithBeers();

}
