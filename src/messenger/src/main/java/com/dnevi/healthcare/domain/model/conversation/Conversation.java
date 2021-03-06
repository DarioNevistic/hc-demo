package com.dnevi.healthcare.domain.model.conversation;

import com.dnevi.healthcare.domain.model.user.User;
import com.nsoft.chiwava.core.persistence.PersistenceTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "conversation")
@EntityListeners(PersistenceTimestampableListener.class)
@Getter
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class Conversation implements PersistenceTimestampable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(100)")
    private String title;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User creator;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Participant> participants = new HashSet<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<InstantMessage> messages = new ArrayList<>();

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Setter
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Version
    @Column(nullable = false)
    protected Long version;

    public Conversation(String title, User creator) {
        this.title = title;
        this.creator = creator;
    }

    public void markConversationAsDeleted() {
        this.setDeleted(true);
        this.setDeletedAt(LocalDateTime.now());
    }

    public void addMessage(InstantMessage instantMessage) {
        this.messages.add(instantMessage);
        instantMessage.setConversation(this);
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
        participant.setConversation(this);
    }
}
