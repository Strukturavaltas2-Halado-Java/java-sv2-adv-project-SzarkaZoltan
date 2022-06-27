package beerwebshopproject.webshop.model;

import beerwebshopproject.beer.model.Beer;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "webshops")
public class Webshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "email_address")
    private String emailAddress;
    @ManyToMany(mappedBy = "webshops", cascade = CascadeType.PERSIST)
    private List<Beer> beers = new ArrayList<>();

    public Webshop(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public Webshop(String name, String emailAddress, List<Beer> beers) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.beers = beers;
    }

    public void addBeer(Beer beer){
        beers.add(beer);
    }

    public void removeBeer(Beer beer){
        beers.remove(beer);
    }

    public void removeAllBeers(){
        beers = new ArrayList<>();
    }
}
