package com.medical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medical.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{


	List<Message> findAllBySenderIdAndRecipientIdOrSenderIdAndRecipientId(long senderId, long recipientId,
			long recipientId2, long senderId2);

	List<Message> findAllByRecipientId(long practitionerId);

	List<Message> findAllBySenderId(long patientId);

	List<Message> findAllBySenderIdAndRecipientId(long sendId, long recipientId);

	List<Message> findAllByRecipientIdAndStatus(long patientId, boolean b);

}
