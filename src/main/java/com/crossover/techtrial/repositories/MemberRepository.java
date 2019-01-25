/**
 * 
 */
package com.crossover.techtrial.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;

/**
 * Person repository for basic operations on Person entity.
 * 
 * @author crossover
 */
@RestResource(exported = false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {
	@Query("SELECT m FROM Member m WHERE id=:id")
	Optional<Member> findById(@Param("id") Long id);

	@Query("SELECT m FROM Member m")
	List<Member> findAll();

	@Query("SELECT "
			+ "    NEW com.crossover.techtrial.dto.TopMemberDTO(t.member.id, t.member.name, t.member.email, COUNT(t.member.id)) "
			+ "FROM " + "    Transaction t " + "WHERE " + "    t.dateOfIssue BETWEEN :startTime AND :endTime AND "
			+ "    t.dateOfReturn BETWEEN :startTime AND :endTime " + "GROUP BY "
			+ "    t.member.id, t.member.name, t.member.email " + "ORDER BY " + "    COUNT(t.member.id) DESC")
	List<TopMemberDTO> getTopFiveMembers(@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime, Pageable pageable);
	
	@Query("SELECT m FROM Member m WHERE m.email=:mail")
	List<Member> checkMail(@Param("mail") String mail);
}
