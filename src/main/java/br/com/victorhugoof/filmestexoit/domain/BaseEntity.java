package br.com.victorhugoof.filmestexoit.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}

