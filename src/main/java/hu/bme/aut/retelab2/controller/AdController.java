package hu.bme.aut.retelab2.controller;

import hu.bme.aut.retelab2.SecretGenerator;
import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private AdRepository adRepository;

    @GetMapping
    public List<Ad> getAll(
            @RequestParam(required = false, defaultValue = "0") int minPrice,
            @RequestParam(required = false, defaultValue = "10000000") int maxPrice) {
        List<Ad> ads = adRepository.findByPriceBetween(minPrice, maxPrice);
        ads.forEach(ad -> ad.setSecretCode(""));
        return ads;
    }

    @GetMapping("{tag}")
    public List<Ad> getByTags(@PathVariable String tag){
        List<Ad> ads = adRepository.findByTag(tag);
        ads.forEach(ad -> ad.setSecretCode(""));
        return ads;
    }

    @PostMapping
    public Ad create(@RequestBody Ad ad) {
        ad.setId(null);
        ad.setCreatedDate(null);
        ad.setSecretCode(SecretGenerator.generate());
        return adRepository.save(ad);
    }

    @PutMapping
    public ResponseEntity<Ad> update(@RequestBody Ad ad) {
        Ad a = adRepository.change(ad);
        if (a == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(a);
    }
}