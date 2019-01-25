/**
 * 
 */
package com.crossover.techtrial.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Transaction;

/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	// @Query("SELECT t FROM Transaction t WHERE t.book.id =:bookId AND
	// t.dateOfReturn=''")

	Optional<Transaction> getByBookIdAndDateOfReturnIsNull(Long bookId);

	Optional<Transaction> getByIdAndDateOfReturnNotNull(Long transactionId);

	long countByMemberIdAndDateOfReturnIsNull(Long memberId);

}
