/**
 * 
 */
package com.crossover.techtrial.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;

/**
 * @author crossover
 *
 */
@Service
public class MemberServiceImpl implements MemberService{

  @Autowired
  MemberRepository memberRepository;
  
  public Member save(Member member) {
    return memberRepository.save(member);
  }
  
  public Member findById(Long memberId) {
    Optional<Member> optionalMember = memberRepository.findById(memberId);
    if (optionalMember.isPresent()) {
      return optionalMember.get();
    }else return null;
  }
  
  public List<Member> findAll() {
    return memberRepository.findAll();
  }

@Override
public List<TopMemberDTO> getTopFiveMembers(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
	// TODO Auto-generated method stub
	return memberRepository.getTopFiveMembers(startTime, endTime, pageable);
}

@Override
public List<Member> checkMail(String mail) {
	// TODO Auto-generated method stub
	return memberRepository.checkMail(mail);
}

}
