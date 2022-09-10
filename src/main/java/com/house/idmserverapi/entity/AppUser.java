package com.house.idmserverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.house.idmserverapi.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;


import java.util.UUID;

import static java.util.Objects.isNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class AppUser implements Persistable<UUID> {

    @Id
    private UUID id;

    private String email;

    private String password;

    private String firstName;

    private String nickName;

    private Status status;

    @JsonIgnore
    @Override
    public boolean isNew() {
        boolean result = isNull(id);
        this.id = result ? UUID.randomUUID() : this.id;

        return result;
    }
}
