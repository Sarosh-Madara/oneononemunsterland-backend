package com.oneonone.munsterlandbackend.respository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oneonone.munsterlandbackend.db.LinkEntity;
import com.oneonone.munsterlandbackend.enums.LinkStatus;

public interface LinkRepository extends JpaRepository<LinkEntity,UUID>{

    Optional<LinkEntity> findByVolunteerIdAndNewcomerId(UUID volunteerId, UUID newcomerId);
    boolean existsByVolunteerIdAndNewcomerIdAndStatus(UUID volunteerId, UUID newcomerId, LinkStatus status);
    
} 
