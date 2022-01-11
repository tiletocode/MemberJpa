package com.kh.memberjpa.repository;

import com.kh.memberjpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    public Optional<Member> findByNum(Integer num);
}
