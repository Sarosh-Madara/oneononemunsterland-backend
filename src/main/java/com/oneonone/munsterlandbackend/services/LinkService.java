package com.oneonone.munsterlandbackend.services;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.oneonone.munsterlandbackend.db.LinkEntity;
import com.oneonone.munsterlandbackend.models.LinkRequest;
import com.oneonone.munsterlandbackend.models.LinkResponse;
import com.oneonone.munsterlandbackend.respository.LinkRepository;
import com.oneonone.munsterlandbackend.respository.UserRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.oneonone.munsterlandbackend.models.UserKind;
import com.oneonone.munsterlandbackend.enums.LinkStatus;
import com.oneonone.munsterlandbackend.models.LinkDetailsResponse;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final UserRepository users;
    private final LinkRepository links;


    @Transactional
    public LinkDetailsResponse unlink(UUID linkId) {
        String uidStr = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID actorId = UUID.fromString(uidStr);
        LinkEntity link = links.findById(linkId)
                .orElseThrow(() -> new IllegalArgumentException("Link not found"));

        // Only the volunteer or the newcomer in this link can unlink
        if (!(actorId.equals(link.getVolunteerId()) || actorId.equals(link.getNewcomerId()))) {
            throw new IllegalArgumentException("You are not a participant of this link");
        }

        if (link.getStatus() == LinkStatus.UNLINKED || link.getStatus() == LinkStatus.CANCELLED) {
            // idempotent-ish: already unlinked
            return toResponse(link);
        }

        link.setStatus(LinkStatus.UNLINKED);
        link.setEndedAt(OffsetDateTime.now(ZoneOffset.UTC));

        LinkEntity saved = links.save(link);
        return toResponse(saved);
    }

    private static LinkDetailsResponse toResponse(LinkEntity e) {
        return new LinkDetailsResponse(
                e.getId(),
                e.getVolunteerId(),
                e.getNewcomerId(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getEndedAt()
        );
    }

    @Transactional
    public LinkResponse createLink(LinkRequest req) {
        // who is calling (volunteer)
        String uidStr = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID volunteerId = UUID.fromString(uidStr);
    
        var volunteer = users.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found"));
        if (volunteer.getKind() != UserKind.VOLUNTEER) {
            throw new IllegalArgumentException("Only volunteers can create links");
        }
    
        var newcomer = users.findById(req.newcomerId())
                .orElseThrow(() -> new IllegalArgumentException("Newcomer not found"));
        if (newcomer.getKind() != UserKind.NEWCOMER) {
            throw new IllegalArgumentException("Target must be a newcomer");
        }
    
        var existingLinkOpt = links.findByVolunteerIdAndNewcomerId(volunteerId, req.newcomerId());
    
        if (existingLinkOpt.isPresent()) {
            var link = existingLinkOpt.get();
    
            if (link.getStatus() == LinkStatus.ACTIVE) {
                throw new IllegalArgumentException("Already linked");
            }
    
            if (link.getStatus() == LinkStatus.UNLINKED) {
                link.setStatus(LinkStatus.ACTIVE);
                link.setEndedAt(null);
                var saved = links.save(link); // be explicit
                return new LinkResponse(saved.getId(), saved.getVolunteerId(), saved.getNewcomerId());
            }
    
            // Optional: handle other statuses explicitly if you add them later
            throw new IllegalArgumentException("Link exists in status: " + link.getStatus());
        }
    
        // create fresh
        var entity = LinkEntity.builder()
                .volunteerId(volunteerId)
                .newcomerId(req.newcomerId())
                .createdBy(volunteerId)
                .status(LinkStatus.ACTIVE)
                .build();
    
        var saved = links.save(entity);
        return new LinkResponse(saved.getId(), saved.getVolunteerId(), saved.getNewcomerId());
    }
    
}
