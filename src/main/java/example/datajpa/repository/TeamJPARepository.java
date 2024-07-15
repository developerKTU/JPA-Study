package example.datajpa.repository;

import example.datajpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamJPARepository {

    @PersistenceContext
    EntityManager em;

    // CREATE
    public Team save(Team team){
        em.persist(team);
        return team;
    }

    // DELETE
    public void delete(Team team){
        em.remove(team);
    }

    // UPDATE : JPA는 기본적으로 Entity 변경할때 '변경감지'라는 기능으로 데이터를 변경

    // READ : 전체 조회
    public List<Team> findAll(){
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    // READ : 조건 조회
    public Optional<Team> findById(long id){
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    // READ : COUNT 조회
    public long count(){
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }


}
