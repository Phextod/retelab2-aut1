package hu.bme.aut.retelab2.repository;

import hu.bme.aut.retelab2.domain.Ad;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AdRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Ad save(Ad feedback) {
        return em.merge(feedback);
    }

    public List<Ad> findByPriceBetween(int minPrice, int maxPrice) {
        return em.createQuery("SELECT a FROM Ad a WHERE a.price BETWEEN ?1 AND ?2", Ad.class)
                .setParameter(1, minPrice)
                .setParameter(2, maxPrice)
                .getResultList();
    }

    public List<Ad> findAll() {
        return em.createQuery("SELECT a FROM Ad a", Ad.class).getResultList();
    }

    @Transactional
    public Ad change(Ad newAd){
        Ad oldAd = em.find(Ad.class, newAd.getId());
        if(oldAd == null)
            return null;
        if(oldAd.getSecretCode().equals(newAd.getSecretCode())){
            newAd.setId(oldAd.getId());
            return em.merge(newAd);
        }
        return null;
    }

    public List<Ad> findByTag(String tag) {
        return em.createQuery("SELECT a FROM Ad a WHERE :tag in elements(a.tags)", Ad.class)
                .setParameter("tag", tag)
                .getResultList();
    }

    @Scheduled(fixedDelay=6000)
    @Transactional
    @Modifying
    public void removeExpiredAds(){
        try {
            em.createQuery("DELETE FROM Ad a").executeUpdate();
        } catch(Exception e){
            System.out.println(e);
        }
    }
//    public Ad findById(long id) {
//        return em.find(Ad.class, id);
//    }
//
//
//    @Transactional
//    public void deleteById(long id) {
//        Ad ad = findById(id);
//        em.remove(ad);
//    }
}
