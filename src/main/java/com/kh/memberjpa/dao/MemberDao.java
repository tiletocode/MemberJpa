package com.kh.memberjpa.dao;

import com.kh.memberjpa.entity.Member;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Repository
public class MemberDao {
    private EntityManager entityManager;
    private CriteriaBuilder builder;
    private CriteriaQuery<Member> query;
    private Root<Member> root;

    public MemberDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        builder = entityManager.getCriteriaBuilder();
        query = builder.createQuery(Member.class);
        root = query.from(Member.class);
    }

    public ArrayList<Member> find(String id, String name, String email) {
        query.select(root);
        query.where(
                builder.like(root.get("id"), "%"+id+"%"),
                builder.like(root.get("name"), "%"+name+"%"),
                builder.like(root.get("email"), "%"+email+"%")
        );
        return (ArrayList<Member>) entityManager.createQuery(query).getResultList();
    }
}
