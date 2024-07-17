package example.datajpa.repository;

import example.datajpa.entity.Member;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findMemberCustom();
}
