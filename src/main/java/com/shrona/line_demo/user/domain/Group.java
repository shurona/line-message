package com.shrona.line_demo.user.domain;

import com.shrona.line_demo.common.entity.BaseEntity;
import com.shrona.line_demo.line.domain.Channel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(BaseEntity.DEFAULT_CONDITION)
@Table(name = "custom_group")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserGroup> userGroupList = new ArrayList<>();

    public static Group createGroup(Channel channel, String name, String description) {
        Group group = new Group();
        group.name = name;
        group.description = description;
        group.channel = channel;

        return group;
    }

    public void addUserToGroup(List<UserGroup> userList) {
        userGroupList.addAll(userList);
    }

    // Group 엔티티 내부
    public void updateGroupInfo(String newName, String newDescription) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
        if (newDescription != null && !newDescription.isBlank()) {
            this.description = newDescription;
        }
    }


    public void deleteGroup() {
        this.isDeleted = true;
    }
}
