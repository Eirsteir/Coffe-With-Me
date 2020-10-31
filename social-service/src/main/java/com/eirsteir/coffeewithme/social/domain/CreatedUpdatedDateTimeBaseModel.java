package com.eirsteir.coffeewithme.social.domain;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@ToString
@SuperBuilder
@MappedSuperclass
public abstract class CreatedUpdatedDateTimeBaseModel {

  @CreationTimestamp private Date createdDateTime;

  @UpdateTimestamp private Date updatedDateTime;

  public CreatedUpdatedDateTimeBaseModel() {}
}
